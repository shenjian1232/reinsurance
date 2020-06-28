package com.minsheng.reinsurance.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minsheng.reinsurance.bean.entity.ExcelDataDto;
import com.minsheng.reinsurance.bean.entity.Office;
import com.minsheng.reinsurance.controller.BaseController;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import com.minsheng.reinsurance.service.ExcelDataService;
import com.minsheng.reinsurance.service.OfficeService;
import com.minsheng.reinsurance.service.UserRoleService;
import com.minsheng.reinsurance.utils.ImportExcel;
import com.minsheng.reinsurance.utils.ObjMapConvertUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("${adminPath}/manage/exceldata")
public class ExcelDataControllor extends BaseController {
    @Autowired
    ExcelDataService dataService;
    @Autowired
    OfficeService officeService;
    @Autowired
    UserRoleService userRoleService;


    private final static String TEMPLATEPRE = "/manage/exceldata/";

    @RequiresPermissions("sys:excel:index")
    @RequestMapping("/index")
    public String index() {
        return getViews(TEMPLATEPRE, "index");
    }

    //从服务器获取数据
    @RequiresPermissions("sys:excel:index")
    @RequestMapping("/get_data_json")
    @ResponseBody
    public Object getdatajson(Integer currentPage, Integer pageSize, ExcelDataDto data) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params = ObjMapConvertUtil.objectToMap(data);
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        Integer icount = dataService.countBy(params);
        PageHelper.startPage(currentPage, pageSize);
        List<ExcelDataDto> list = dataService.findBy(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (ExcelDataDto item : list) {
            // officeName
            Integer officeId = item.getOfficeId();
            if (officeId != null) {
                Office office = officeService.getEntityById(officeId);
                if (office != null) {
                    item.setOfficeName(office.getName());
                }
            }
            // roleNameStr
            List<String> roleNamesByUserId = userRoleService.getRoleNamesByUserId(item.getId());
            String roleNameStr = StringUtils.join(roleNamesByUserId, ",");
            item.setRoleNameStr(roleNameStr);
            // roleIds
            List<Integer> roleIds = userRoleService.getRoleIdsByUserId(item.getId());
            item.setRoleIds(roleIds);
            lists.add(ObjMapConvertUtil.objectToMap(item));
        }
        PageInfo pageInfo = new PageInfo(list);
        result.put("count", pageInfo.getTotal());
        result.put("lists", lists);
        return result;

    }


    @RequiresPermissions("sysExcel:data:add")
    @RequestMapping("/addPage")
    public String addPage() {
        return getViews(TEMPLATEPRE, "add");
    }


    @RequiresPermissions("sysExcel:data:add")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(ExcelDataDto excelDataDto) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (dataService.validateExceldataParams(rtn, excelDataDto)) {
            dataService.insert(excelDataDto);
            Integer id = excelDataDto.getId();
            putSessionCache("addEntity", dataService.getEntityById(id));
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysExcel:data:edit")
    @RequestMapping("/editPage")
    public String editPage(Integer id, ModelMap modelMap) {
        ExcelDataDto excelDataDto = new ExcelDataDto();
        if (id != null) {
            excelDataDto = dataService.getEntityById(id);
            Integer officeId = excelDataDto.getOfficeId();
            if (officeId != null) {
                ExcelDataDto excelData = dataService.getEntityById(officeId);
                if (excelData != null) {
                    excelDataDto.setOfficeName(excelData.getName());
                }
            }
        }
        modelMap.put("model", excelDataDto);
        return getViews(TEMPLATEPRE, "edit");
    }

    @RequiresPermissions("sysExcel:data:edit")
    @RequestMapping("/update")
    @ResponseBody
    public Object update(ExcelDataDto excelDataDto) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (dataService.validateExceldataParams(rtn, excelDataDto)) {
            dataService.update(excelDataDto);
            Integer id = excelDataDto.getId();
            putSessionCache("updateEntity", dataService.getEntityById(id));
            dataService.update(excelDataDto);
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysExcel:data:del")
    @RequestMapping("/del")
    @ResponseBody
    public Object delete(Integer id) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (id == null || id.equals(0)) {
            rtn.put("msg", "删除失败");
            return rtn;
        } else {
            ExcelDataDto excelDataDto = dataService.getEntityById(id);
            if (excelDataDto != null) {
                dataService.delete(id);
            }
            return true;
        }
    }

    @RequiresPermissions("sysExcel:data:upload")
    @RequestMapping("/upload")
    @ResponseBody
    public Object excelUpload(MultipartFile file) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        Boolean bool = ImportExcel.checkFile(file);
        if (!bool) {
            map.put("msg", "文件类型不正确或为空");
            return map;
        } else {
            List<ExcelDataDto> list = ImportExcel.importData(file);
            for(ExcelDataDto server : list){
                if (dataService.validateExceldataParams(map, server) == false) {
                    return map;
                }
            }
            for (ExcelDataDto server : list){
                dataService.insert(server);
            }
        }
        return true;
    }

}
