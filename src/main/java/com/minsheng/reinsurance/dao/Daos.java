package com.minsheng.reinsurance.dao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr giraffe on 15/9/8.
 */

public interface Daos<T> {
    Integer insert(T t);

    void update(T t);

    void delete(Integer id);

    List<T> findBy(HashMap<?, ?> params);

    Integer countBy(HashMap<?, ?> params);

    T getEntityById(Integer id);
}
