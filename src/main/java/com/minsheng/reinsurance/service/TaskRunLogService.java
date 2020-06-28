package com.minsheng.reinsurance.service;

import com.minsheng.reinsurance.bean.entity.TaskRunLog;
import com.minsheng.reinsurance.dao.TaskRunLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class TaskRunLogService extends Services<TaskRunLog>{
    @Autowired
    TaskRunLogDao taskRunLogDao;

    public void addTaskLog (TaskRunLog taskRunLog){
        System.out.println(taskRunLog.getJobName()+"=job===");
        taskRunLogDao.insert(taskRunLog);
    }
}
