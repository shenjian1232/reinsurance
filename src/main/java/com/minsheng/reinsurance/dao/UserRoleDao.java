package com.minsheng.reinsurance.dao;


import com.minsheng.reinsurance.bean.entity.UserRole;
import com.minsheng.reinsurance.bean.view.UserRoleView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 16/10/17.
 */
public interface UserRoleDao extends Daos<UserRole> {
    List<UserRoleView> findViewBy(HashMap<String, Object> params);
    void deleteByUserId(Integer userId);
}
