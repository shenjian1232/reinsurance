package com.minsheng.reinsurance.controller.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minsheng.reinsurance.bean.entity.Email;
import com.minsheng.reinsurance.controller.BaseController;
import com.minsheng.reinsurance.enums.EmailTypeEnum;
import com.minsheng.reinsurance.service.EmailService;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import com.minsheng.reinsurance.utils.ObjMapConvertUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by panwei on 16/10/17.
 */
@Controller
@RequestMapping("${adminPath}/manage/email")
public class EmailController extends BaseController {
    private static Logger logger = Logger.getLogger(EmailController.class);
    @Autowired
    EmailService emailService;

    private final static String TEMPLATEPRE = "/manage/email/";

    @RequiresPermissions("sysSetup:email:index")
    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        logger.info("开始邮箱首页");
        List<Map<String, Object>> emailTypeList = EmailTypeEnum.getEmailTypeList();
        modelMap.put("emailTypeList", emailTypeList);
        logger.info("结束邮箱首页");
        return getViews(TEMPLATEPRE, "index");
    }

    @RequiresPermissions("sysSetup:email:index")
    @RequestMapping("/get_emails_json")
    @ResponseBody
    public Object getEmailsJson(Integer currentPage, Integer pageSize, Email email) throws Exception {
        logger.info("开始获取sys_email的数据");
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params = ObjMapConvertUtil.objectToMap(email);
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        PageHelper.startPage(currentPage,pageSize);
        List<Email> list = emailService.findBy(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (Email item : list) {
            Integer type = item.getType();
            if (type.equals(1)) {
                item.setComment(EmailTypeEnum.SUPERMANAGER.getName());
            }
            lists.add(ObjMapConvertUtil.objectToMap(item));
        }
        PageInfo pageInfo = new PageInfo(list);
        result.put("count", pageInfo.getTotal());
        result.put("lists", lists);
        logger.info("结束获取sys_email的数据");
        return result;
    }

    @RequiresPermissions("sysSetup:email:add")
    @RequestMapping("/addPage")
    public String addPage(ModelMap modelMap) {
        Map<String, String> emailTypeMap = EmailTypeEnum.getKeysMapSS();
        modelMap.put("emailTypeMap", emailTypeMap);
        return getViews(TEMPLATEPRE, "add");
    }

    @RequiresPermissions("sysSetup:email:edit")
    @RequestMapping("/editPage")
    public String editPage(Integer id, ModelMap modelMap) {
        Email email = new Email();
        if (id != null && !id.equals(0)) {
            email = emailService.getEntityById(id);
        }
        modelMap.put("model", email);
        Map<String, String> emailTypeMap = EmailTypeEnum.getKeysMapSS();
        modelMap.put("emailTypeMap", emailTypeMap);
        return getViews(TEMPLATEPRE, "edit");
    }

    @RequiresPermissions("sysSetup:email:add")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(Email email) {
        HashMap<String, Object> rtn = new HashMap<>();

        if (emailService.validateEmailParams(rtn, email)) {
            email.setCreateBy(getSessionCache("userId").toString());
            email.setCreateDate(new Date());
            emailService.insert(email);
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysSetup:email:edit")
    @RequestMapping("/update")
    @ResponseBody
    public Object update(Email email) {
        HashMap<String, Object> rtn = new HashMap<>();

        if (emailService.validateEmailParams(rtn, email)) {
            email.setUpdateBy(getSessionCache("userId").toString());
            email.setUpdateDate(new Date());
            emailService.update(email);
            return true;
        }
        return rtn;
    }

    @RequiresPermissions("sysSetup:email:changeStatus")
    @RequestMapping("/changeStatus")
    @ResponseBody
    public Object changeStatus(Integer id, Integer status) {
        Email email = emailService.getEntityById(id);
        if (email != null) {
            email.setStatus(status);
            emailService.update(email);
        }
        return true;
    }

    @RequiresPermissions("sysSetup:email:del")
    @RequestMapping("/del")
    @ResponseBody
    public Object delete(Integer id) {
        HashMap<String, Object> rtn = new HashMap<>();
        if (id == null || id.equals(0)) {
            rtn.put("msg", "删除失败");
            return rtn;
        } else {
            Email email = emailService.getEntityById(id);
            if (email != null) {
                email.setDelFlag(DeleteFlagEnum.DELETED.getCode());
                emailService.update(email);
            }
            return true;
        }
    }

    @RequiresPermissions("sysSetup:email:batchDel")
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
