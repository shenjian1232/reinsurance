package com.minsheng.reinsurance.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class MyJobFactory extends AdaptableJobFactory {
    private AutowireCapableBeanFactory factory;

    public MyJobFactory(AutowireCapableBeanFactory factory) {
        this.factory = factory;
    }

    /**
     * 创建Job实例
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

        // 实例化对象
        Object job = super.createJobInstance(bundle);
        // 进行注入（Spring管理该Bean）
        factory.autowireBean(job);
        //返回对象
        return job;
    }
}
