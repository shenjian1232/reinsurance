package com.minsheng.reinsurance.task;

import com.minsheng.reinsurance.bean.entity.TaskRunLog;
import com.minsheng.reinsurance.service.TaskRunLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
@Component
public class ReportMailBatch implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(ReportMailBatch.class);

    //@Autowired
   // private ReportMailService service;
    private Map<String,String> map = new HashMap();

    @Autowired
    TaskRunLogService taskRunLogService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        LOG.info("ReportMailBatch--data-c->execute()");
        String uuid = UUID.randomUUID().toString().replace("-","");
        map.put("uuid",uuid);
        map.put("startTime",new Date().toString());
        map.put("jobName","ReportMailBatch");
        map.put("executeState","1");
        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setJobName(map.get("jobName"));
        taskRunLog.setStartTime(map.get("startTime"));
        taskRunLog.setExecuteState(map.get("executeState"));
        taskRunLog.setSerialno(map.get("uuid"));
        taskRunLogService.insert(taskRunLog);

//        try {
//            throw new TaskRunException(map);
//        } catch (TaskRunException e) {
//            //e.printStackTrace();
//        }
        // service.sendReportMail();// 可换成自己业务逻辑
//        try {
//            throw new TaskRunException(map);
//        } catch (TaskRunException e) {
//            //e.printStackTrace();
//        }
        LOG.info("--本次邮件汇报批处理结束--");

        //return "";

    }
}
