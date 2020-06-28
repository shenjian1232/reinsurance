package com.minsheng.reinsurance.service;


import com.minsheng.reinsurance.bean.entity.Menu;
import com.minsheng.reinsurance.bean.view.MenuView;
import com.minsheng.reinsurance.dao.MenuDao;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by panwei on 16/10/17.
 */
@Service
public class MenuService extends Services<Menu> {
    @Autowired
    private MenuDao menuDaor;

    /**
     * treeTable
     *
     * @param params
     * @return
     */
    public List<MenuView> findAllMenu(HashMap<String, Object> params) {
        return menuDaor.findAllMenu(params);
    }

    public void getMenuTreeTable(List<MenuView> menuTreeTable, List<MenuView> allMenu, Integer parentId) {
        for (MenuView parent : allMenu) {
            parent.setFullIcon("fa " + parent.getIcon());
            if (parent.getParentId().equals(parentId)) {
                menuTreeTable.add(parent);
                for (MenuView child : allMenu) {
                    if (child.getParentId().equals(parent.getId())) {
                        getMenuTreeTable(menuTreeTable, allMenu, parent.getId());
                        break;
                    }
                }
            }
        }
    }

    /**
     * tree 菜单树
     *
     * @param parentId:父id
     * @return
     */
    public List<HashMap<String, Object>> getAllMenu(Integer parentId) {
        List<HashMap<String, Object>> menus = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        if (parentId != null) {
            params.put("parentId", parentId);
        }

        List<Menu> items = menuDaor.findBy(params);
        for (Menu item : items) {
            HashMap<String, Object> m = new HashMap<>();//菜单、权限
            Integer id = item.getId();
            m.put("id", id);
            m.put("name", item.getName());
            m.put("label", item.getName());
            m.put("open", true);
            List<HashMap<String, Object>> child = getAllMenu(item.getId());
            m.put("children", child.size() > 0 ? child : null);
            menus.add(m);
        }
        return menus;
    }

    public boolean validateMenuParams(HashMap<String, Object> rtn, Menu menu) {
        if (menu != null) {
            String icon = menu.getIcon();
            String name = menu.getName();
            String href = menu.getHref();
            String permission = menu.getPermission();
            Integer parentId = menu.getParentId();
            Integer isShow = menu.getIsShow();
            Integer sort = menu.getSort();
            if (StringUtils.isBlank(icon) || parentId == null || StringUtils.isBlank(name)
                    || isShow == null || StringUtils.isBlank(permission) || sort == null) {
                rtn.put("msg", "请填写完整");
                return false;
            }
            HashMap<String, Object> params;
            List<Menu> menuListForPath = new ArrayList<>();
            if (!StringUtils.isBlank(href)) {
                params = new HashMap<>();
                params.put("href", href);
                menuListForPath = menuDaor.findBy(params);
            }
            params = new HashMap<>();
            params.put("permission", permission);
            List<Menu> menuListForPermission = menuDaor.findBy(params);
            Integer id = menu.getId();
            if (id == null) { // add
                if (menuListForPath.size() > 0) {
                    rtn.put("msg", "该路径已存在");
                    return false;
                }
                if (menuListForPermission.size() > 0) {
                    rtn.put("msg", "该权限已存在");
                    return false;
                }
            } else { // update
                if (menuListForPath.size() > 0) {
                    if (!Objects.equals(id, menuListForPath.get(0).getId())) {
                        rtn.put("msg", "该路径已存在");
                        return false;
                    }
                }
                if (menuListForPermission.size() > 0) {
                    if (!Objects.equals(id, menuListForPermission.get(0).getId())) {
                        rtn.put("msg", "该权限已存在");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 算出该菜单的深度值
     *
     * @param menuId
     * @return
     */
    public Integer getDepth(Integer menuId) {
        Integer depth = 0;
        if (menuId != null) {
            depth = 1;
            for (int i = 0; i < 5; i++) {
                Integer parentId = menuDaor.getEntityById(menuId).getParentId();
                if (parentId.equals(0)) {
                    return depth;
                }
                menuId = parentId;
                depth++;
            }
        }
        return depth;
    }



}
