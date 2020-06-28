package com.minsheng.reinsurance.service;

import com.minsheng.reinsurance.bean.entity.ExcelDataDto;
import com.minsheng.reinsurance.dao.ExcelDataDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class ExcelDataService extends Services<ExcelDataDto>{
    @Autowired
    ExcelDataDao excelDataDao;

    public boolean validateExceldataParams(HashMap<String, Object> rtn, ExcelDataDto excelDataDto) {
        if (excelDataDto != null) {
            String name = excelDataDto.getName();
            String loginName = excelDataDto.getLoginName();
            Integer officeId = excelDataDto.getOfficeId();
            if (StringUtils.isBlank(name) || StringUtils.isBlank(loginName) || officeId == null ) {
                rtn.put("msg", "请填写完整");
                return false;
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", name);
            List<ExcelDataDto> excelDatanameList = excelDataDao.findBy(params);
            params = new HashMap<>();
            params.put("loginName", loginName);
            List<ExcelDataDto> excelDataloginName = excelDataDao.findBy(params);
            List<ExcelDataDto> id = excelDataDao.findBy(params);
            if (id == null) { // add
                if (excelDatanameList.size() > 0) {
                    rtn.put("msg", name + " 账号已存在");
                    return false;
                }
                if (excelDataloginName.size() > 0) {
                    rtn.put("msg", loginName +" 该账号已被注册过");
                    return false;
                }
            } else { //update
                if (excelDatanameList.size() > 0) {
                    if (!Objects.equals(id, excelDatanameList.get(0).getId())) {
                        rtn.put("msg", name + " 该姓名已存在");
                        return false;
                    }
                }
                if (excelDataloginName.size() > 0) {
                    if (!Objects.equals(id, excelDataloginName.get(0).getId())) {
                        rtn.put("msg", loginName +" 该账号已被注册过");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }



}
