package com.minsheng.reinsurance.dao;


import com.minsheng.reinsurance.bean.entity.RoleMenu;
import com.minsheng.reinsurance.bean.view.RoleMenuView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 16/10/17.
 */
public interface RoleMenuDao extends Daos<RoleMenu> {
    List<RoleMenuView> findViewBy(HashMap<String, Object> params);
    void deleteByRoleId(Integer roleId);
}
