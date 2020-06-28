package com.minsheng.reinsurance.service;


import com.minsheng.reinsurance.bean.entity.UserRole;
import com.minsheng.reinsurance.bean.view.UserRoleView;
import com.minsheng.reinsurance.dao.UserRoleDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 16/10/17.
 */
@Service
public class UserRoleService extends Services<UserRole>   {
    @Autowired
    UserRoleDao userRoleDao;
    @Autowired
    RoleService roleService;

    public List<UserRoleView> findViewBy(HashMap<String, Object> params) {
        return userRoleDao.findViewBy(params);
    }

    public void deleteByUserId(Integer userId) {
        userRoleDao.deleteByUserId(userId);
    }

    public void insertUserRole(Integer userId, Integer[] roleIds) {
        userRoleDao.deleteByUserId(userId);
        for (Integer item : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(item);
            userRoleDao.insert(userRole);
        }
    }

    public List<Integer> getRoleIdsByUserId(Integer userId) {
        List<Integer> roleIds = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserRole> userRoleList = userRoleDao.findBy(params);
        for (UserRole item : userRoleList) {
            roleIds.add(item.getRoleId());
        }
        return roleIds;
    }

    public List<String> getRoleNamesByUserId(Integer userId) {
        List<String> roleNames = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserRole> userRoleList = userRoleDao.findBy(params);
        for (UserRole item : userRoleList) {
            Integer roleId = item.getRoleId();
            String roleName = roleService.getRoleNameByRoleId(roleId);
            if (StringUtils.isNotBlank(roleName)) {
                roleNames.add(roleName);
            }
        }
        return roleNames;
    }

}
