package com.minsheng.reinsurance.controller.manage;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minsheng.reinsurance.bean.entity.LoginLog;
import com.minsheng.reinsurance.bean.entity.User;
import com.minsheng.reinsurance.controller.BaseController;
import com.minsheng.reinsurance.service.LoginLogService;
import com.minsheng.reinsurance.service.UserService;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import com.minsheng.reinsurance.utils.ObjMapConvertUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panwei on 16/10/17.
 */
@Controller
@RequestMapping("${adminPath}/manage/loginLog")
public class LoginLogController extends BaseController {
    @Autowired
    LoginLogService loginLogService;
    @Autowired
    UserService userService;

    private final static String TEMPLATEPRE = "/manage/loginLog/";

    @RequiresPermissions("sysLog:loginLog:index")
    @RequestMapping("/index")
    public String index() {
        return getViews(TEMPLATEPRE, "index");
    }

    @RequiresPermissions("sysLog:loginLog:index")
    @RequestMapping("/get_loginLogs_json")
    @ResponseBody
    public Object getLoginLogsJson(Integer currentPage, Integer pageSize, LoginLog loginLog) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params = ObjMapConvertUtil.objectToMap(loginLog);
        String loginName = loginLog.getLoginName();
        if (StringUtils.isNotBlank(loginName)) {
            Integer userId = userService.getUserId(loginName);
            if (userId != null) {
                params.put("userId", userId);
            } else {
                params.put("userId", 0);
            }
        }
        params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
        PageHelper.startPage(currentPage,pageSize);
        List<LoginLog> list = loginLogService.findBy(params);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (LoginLog item : list) {
            Integer userId = item.getUserId();
            if (userId != null) {
                User user = userService.getEntityById(userId);
                if (user != null) {
                    item.setLoginName(user.getLoginName());
                }
            }
            lists.add(ObjMapConvertUtil.objectToMap(item));
        }
        PageInfo pageInfo = new PageInfo(list);

        result.put("count", pageInfo.getTotal());
        result.put("lists", lists);
        return result;
    }

}
