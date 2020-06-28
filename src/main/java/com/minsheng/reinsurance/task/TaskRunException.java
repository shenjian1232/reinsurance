package com.minsheng.reinsurance.task;

import com.minsheng.reinsurance.bean.entity.TaskRunLog;
import com.minsheng.reinsurance.service.TaskRunLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 *
 */
public class TaskRunException extends Exception {
    @Autowired
    TaskRunLogService taskRunLogService = new TaskRunLogService();
    TaskRunLog taskRunLog = new TaskRunLog();
    private Map<String,String> map;
    public TaskRunException(Map<String,String> map){
        this.map = map;
        System.out.println("====1====map:"+map);
        taskRunLog.setJobName(map.get("jobName"));
        taskRunLog.setExecuteState(map.get("executeState"));

        taskRunLog.setSerialno(map.get("uuid"));
        System.out.println("====2====map:"+taskRunLog.getJobName());
        taskRunLogService.addTaskLog(taskRunLog);
        //super(map);
        //System.out.println("chengg");


    }

}
