package com.minsheng.reinsurance.controller.manage;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minsheng.reinsurance.bean.entity.Menu;
import com.minsheng.reinsurance.bean.entity.Role;
import com.minsheng.reinsurance.bean.entity.RoleMenu;
import com.minsheng.reinsurance.config.CurrencyConfig;
import com.minsheng.reinsurance.controller.BaseController;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import com.minsheng.reinsurance.service.MenuService;
import com.minsheng.reinsurance.service.RoleMenuService;
import com.minsheng.reinsurance.service.RoleService;
import com.minsheng.reinsurance.service.UserService;
import com.minsheng.reinsurance.utils.ObjMapConvertUtil;
import com.minsheng.reinsurance.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by panwei on 16/10/17.
 */
@Controller
@RequestMapping("${adminPath}/manage/role")
public class RoleController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    CurrencyConfig currencyConfig;
    @Autowired
    ShiroUtils shiroUtils;

    private final static String TEMPLATEPRE = "/manage/role/";

    @RequiresPermissions("sysAdministrative:role:index")
    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        List<HashMap<String, Object>> menuTree = menuService.getAllMenu(0);// 获取所有菜单(具有层次结构)
        modelMap.put("menuTree", menuTree);
        return getViews(TEMPLATEPRE, "index");
    }

    @RequestMapping("/index2")
    public String index2(ModelMap modelMap) {
        return getViews(TEMPLATEPRE, "index2");
    }

    @RequiresPermissions("sysAdministrative:role:index")
    @RequestMapping("/get_roles_json")
    @ResponseBody
    public Object getRolesJson(Integer currentPage, Integer pageSize, Role role) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params = ObjMapConvertUtil.objectToMap(role);
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        PageHelper.startPage(currentPage,pageSize);
        List<Role> list = roleService.findBy(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (Role item : list) {
            List<Integer> menuIds = roleMenuService.getMenuIdsByRoleId(item.getId());
            item.setMenuIds(menuIds);
            lists.add(ObjMapConvertUtil.objectToMap(item));
        }
        PageInfo pageInfo = new PageInfo(list);

        result.put("count", pageInfo.getTotal());
        result.put("lists", lists);
        return result;
    }

    @RequiresPermissions("sysAdministrative:role:add")
    @RequestMapping("/addPage")
    public String addPage() {
        return getViews(TEMPLATEPRE, "add");
    }

    @RequiresPermissions("sysAdministrative:role:edit")
    @RequestMapping("/editPage")
    public String editPage(Integer id, ModelMap modelMap) {
        Role role = new Role();
        List<Integer> menuIds = new ArrayList<>();
        if (id != null) {
            role = roleService.getEntityById(id);
            HashMap<String, Object> params = new HashMap<>();
            params.put("roleId", id);
            List<RoleMenu> roleMenuList = roleMenuService.findBy(params);
            menuIds.addAll(roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList()));
        }
        modelMap.put("menuIds", menuIds);
        modelMap.put("model", role);
        return getViews(TEMPLATEPRE, "edit");
    }

    @RequiresPermissions("sysAdministrative:role:add")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(Role role, Integer[] menuAllIds) {
        HashMap<String, Object> rtn = new HashMap<>();

        if (roleService.validateRoleParams(rtn, role, menuAllIds)) {
            role.setCreateBy(getSessionCache("userId").toString());
            role.setCreateDate(new Date());
            roleService.insert(role);
            roleMenuService.insertRoleMenu(role.getId(), menuAllIds);
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysAdministrative:role:edit")
    @RequestMapping("/update")
    @ResponseBody
    public Object update(Role role, Integer[] menuAllIds) {
        HashMap<String, Object> rtn = new HashMap<>();

        if (roleService.validateRoleParams(rtn, role, menuAllIds)) {
            role.setUpdateBy(getSessionCache("userId").toString());
            role.setUpdateDate(new Date());
            roleService.update(role);
            // 删除原来的所有权限
            roleMenuService.deleteByRoleId(role.getId());
            roleMenuService.insertRoleMenu(role.getId(), menuAllIds);
//            shiroUtils.reloadPermissionByRole(roleService.getRoleNameByRoleId(role.getId()));
            return true;
        }
        return rtn;
    }

    private Integer[] getCompleteMenuIds(Integer[] menuIds) {
        Set<Integer> completeMenuIds = new HashSet<>();
        for (Integer menuId : menuIds) {
            List<Integer> fullMenuIdsByCurrentMenuId = getFullMenuIdsByCurrentMenuId(menuId);
            completeMenuIds.addAll(fullMenuIdsByCurrentMenuId);
        }
        return completeMenuIds.toArray(new Integer[] {});
    }

    private List<Integer> getFullMenuIdsByCurrentMenuId(Integer currentMenuId) {
        List<Integer> fullMenuIdList = new ArrayList<>();
        fullMenuIdList.add(currentMenuId);
        Menu menu = menuService.getEntityById(currentMenuId);
        if (menu != null) {
            Integer depth = menu.getDepth();
            Integer parentId = menu.getParentId();
            for (int i = 0; i < 5; i++) { // todo:由于现在的depth还不准,故先写死。以后用depth-1替代
                Menu parentMenu = menuService.getEntityById(parentId);
                if (parentMenu != null) {
                    fullMenuIdList.add(parentMenu.getId());
                    parentId = parentMenu.getParentId();
                }
            }
        }
        return fullMenuIdList;
    }

    @RequiresPermissions("sysAdministrative:role:changeStatus")
    @RequestMapping("/changeStatus")
    @ResponseBody
    public Object changeStatus(Integer id, Integer useable) {
        Role role = roleService.getEntityById(id);
        if (role != null) {
            role.setUseable(useable);
            roleService.update(role);
        }
        return true;
    }

    @RequiresPermissions("sysAdministrative:role:del")
    @RequestMapping("/del")
    @ResponseBody
    public Object delete(Integer id) {
        if (id == null || id.equals(0)) {
            return false;
        } else {
            Role role = roleService.getEntityById(id);
            if (role != null) {
                role.setDelFlag(DeleteFlagEnum.DELETED.getCode());
                roleService.update(role);
            }
            return true;
        }
    }

    @RequiresPermissions("sysAdministrative:role:batchDel")
    @RequestMapping("/batchDel")
    @ResponseBody
    public Object batchDel(Integer[] ids) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (ids != null || ids.length > 0) {
            for (Integer id : ids) {
                this.delete(id);
            }
            return true;
        }
        rtn.put("msg", "删除失败");
        return rtn;
    }

}
