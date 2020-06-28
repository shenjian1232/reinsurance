package com.minsheng.reinsurance.dao;



import com.minsheng.reinsurance.bean.entity.Menu;
import com.minsheng.reinsurance.bean.view.MenuView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by panwei on 16/10/17.
 */
public interface MenuDao extends Daos<Menu> {
    List<MenuView> findAllMenu(HashMap<String, Object> params);
}
