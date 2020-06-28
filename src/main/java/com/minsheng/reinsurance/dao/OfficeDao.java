package com.minsheng.reinsurance.dao;


import com.minsheng.reinsurance.bean.entity.Office;
import com.minsheng.reinsurance.bean.view.OfficeView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 16/10/17.
 */
public interface OfficeDao extends Daos<Office> {
    List<OfficeView> findAllOffice(HashMap<String, Object> params);
}
