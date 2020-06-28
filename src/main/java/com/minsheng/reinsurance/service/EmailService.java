package com.minsheng.reinsurance.service;


import com.minsheng.reinsurance.bean.entity.Email;
import com.minsheng.reinsurance.dao.EmailDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by panwei on 16/10/17.
 */
@Service
public class EmailService extends Services<Email> {
    @Autowired
    private EmailDao emailDao;


    public boolean validateEmailParams(HashMap<String, Object> rtn, Email email) {
        if (email != null) {
            String emailName = email.getEmailName();
            String remarks = email.getRemarks();
            Integer type = email.getType();
            Integer status = email.getStatus();
            if (StringUtils.isBlank(emailName) || StringUtils.isBlank(remarks)
                    || type == null || status == null) {
                rtn.put("msg", "请填写完整");
                return false;
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("emailName", emailName);
            params.put("type", type);
            List<Email> emailList = emailDao.findBy(params);
            Integer id = email.getId();
            if (id == null) { // add
                if (emailList.size() > 0) {
                    rtn.put("msg", "该类型的邮箱已存在");
                    return false;
                }
            } else { // update
                if (emailList.size() > 0) {
                    if (!Objects.equals(id, emailList.get(0).getId())) {
                        rtn.put("msg", "该类型的邮箱已存在");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
