package com.minsheng.reinsurance.service;


import com.minsheng.reinsurance.dao.Daos;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr giraffe on 15/9/8.
 */
public class Services<T> {
    @Autowired
    Daos<T> daos;

    /**
     * 插入
     *
     * @param t
     */
    public Integer insert(T t) {
        return daos.insert(t);
    }

    /**
     * 更新
     *
     * @param t
     */
    public void update(T t) {
        daos.update(t);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Integer id) {
        daos.delete(id);
    }

    /**
     * 计数
     *
     * @param params
     * @return
     */
    public Integer countBy(HashMap<?, ?> params) {
        return daos.countBy(params);
    }

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    public List<T> findBy(HashMap<?, ?> params) {
        return daos.findBy(params);
    }

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    public T getEntityById(Integer id) {
        return daos.getEntityById(id);
    }
}
