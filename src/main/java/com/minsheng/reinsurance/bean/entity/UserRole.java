package com.minsheng.reinsurance.bean.entity;

import java.io.Serializable;

/**
 * Created by panwei on 16/10/17.
 */
public class UserRole implements Serializable {
    private Integer userId; // 用户id
    private Integer roleId; // 角色id

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
