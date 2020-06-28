package com.minsheng.reinsurance.task;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 路径：com.example.demo.service
 * 类名：
 * 功能：任务类
 * 备注：
 * 创建人：typ
 * 创建时间：2018/10/10 12:03
 * 修改人：
 * 修改备注：
 * 修改时间：
 */
@Component
public class ScheduledTask implements Job {
    /**
     * dealDate方法名称不可改变，为执行的方法
     */
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("Hello world, i'm the king of the world!!!");
    }
}
