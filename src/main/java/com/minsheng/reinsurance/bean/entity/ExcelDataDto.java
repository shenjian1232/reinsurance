package com.minsheng.reinsurance.bean.entity;

import com.minsheng.reinsurance.bean.BaseModel;

import java.util.List;

/**
 *
 */
public class ExcelDataDto extends BaseModel<ExcelDataDto> {
    private Integer id; //编号
    private String loginName; // 登录名
    private String officeName; // 部门名称
    private Integer officeId; //归属部门
    private String name; // 姓名
    private String roleNameStr; //该用户所拥有的角色名称,以逗号隔开
    private List<Integer> roleIds; // 该用户所拥有的角色id
    private String salt; // 佐料

    private  String userMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleNameStr() {
        return roleNameStr;
    }

    public void setRoleNameStr(String roleNameStr) {
        this.roleNameStr = roleNameStr;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }
}
