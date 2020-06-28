package com.minsheng.reinsurance.service;

import com.minsheng.reinsurance.bean.entity.TaskInfo;
import com.minsheng.reinsurance.dao.TaskInfoDao;
import com.minsheng.reinsurance.enums.DeleteFlagEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Service
public class TaskService extends Services<TaskInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private static final String PATH = "com.minsheng.reinsurance.task.";

    @Autowired
    private Scheduler scheduler;

    @Autowired
    TaskInfoDao taskInfoDao;

    /**
     * 校验实体类中各字段值
     * @param rtn
     * @param taskInfo
     * @return
     */
    public boolean validateTaskParams(HashMap<String, Object> rtn, TaskInfo taskInfo,String dos) {
        if (taskInfo != null) {
            String jobName = taskInfo.getJobName();
            String jobGroup = taskInfo.getJobGroup();
            String jobDescription = taskInfo.getJobDescription();
            String jobStatus = taskInfo.getJobStatus();
            String cronExpression = taskInfo.getCronExpression();
            String startDate = taskInfo.getStartDate();
            String endDate = taskInfo.getEndDate();
            if (StringUtils.isBlank(jobName) || StringUtils.isBlank(jobDescription) || StringUtils.isBlank(jobStatus)
                    || StringUtils.isBlank(cronExpression) || StringUtils.isBlank(startDate) ) {
                rtn.put("msg", "请填写完整");
                return false;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setLenient(false);
            try {
                format.parse(startDate);
            } catch (ParseException e) {
                rtn.put("msg", "开始日期格式不正确");
                return false;
                //e.printStackTrace();
            }
            if(!StringUtils.isBlank(endDate)){
                try {
                    format.parse(endDate);
                } catch (ParseException e) {
                    rtn.put("msg", "结束日期格式不正确");
                    return false;
                    //e.printStackTrace();
                }
            }
            //当操作为添加任务时，需判断是否已存在相同任务
            if(dos.equals("add")){
                HashMap<String, Object> params = new HashMap<>();
                params.put("jobName", jobName);
                params.put("delFlag", DeleteFlagEnum.NORMAL.getCode());
                List<TaskInfo> taskInfoList = taskInfoDao.findBy(params);
                if(taskInfoList.size()>0){
                    rtn.put("msg", "该方法名称定时任务已存在，请先删除同名服务再进行添加操作");
                    return false;
                }
            }

            return true;
        }
        return false;
    }
    /**
     *
     * @Title: list
     * @Description: 任务列表
     * @param @return    参数
     * @return List<TaskInfo>    返回类型
     * @throws
     */
    public List<TaskInfo> queryJobList() {
	LOGGER.info("TaskService--data-s-->queryJobList()");
	List<TaskInfo> list = new ArrayList<>();
	try {
            for (String groupJob : scheduler.getJobGroupNames()) {
		for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey> groupEquals(groupJob))) {
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			String cronExpression = "";
			String createTime = "";
			String milliSeconds = "";
			String repeatCount = "";
			String startDate = "";
			String endDate = "";
			if (trigger instanceof CronTrigger) {
			CronTrigger cronTrigger = (CronTrigger) trigger;
			cronExpression = cronTrigger.getCronExpression();
			createTime = cronTrigger.getDescription();
			} else if (trigger instanceof SimpleTrigger) {
                            SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                            milliSeconds = simpleTrigger.getRepeatInterval()+ "";
                            repeatCount = simpleTrigger.getRepeatCount() + "";
                            startDate = "2019-01-01";
                            endDate = "2019-12-01";;
			}
                        TaskInfo info = new TaskInfo();
                        info.setJobName(jobKey.getName());
                        info.setJobGroup(jobKey.getGroup());
                        info.setJobDescription(jobDetail.getDescription());
                        info.setJobStatus(triggerState.name());
                        info.setCronExpression(cronExpression);
                        info.setCreateTime(createTime);

                        info.setRepeatCount(repeatCount);
                        info.setStartDate(startDate);
                        info.setMilliSeconds(milliSeconds);
                        info.setEndDate(endDate);
                        list.add(info);
                    }
                }
            }
            LOGGER.info("任务的数量为：---------------->" + list.size());
	} catch (SchedulerException e) {
            LOGGER.info("查询任务失败，原因是：------------------>" + e.getMessage());
            e.printStackTrace();
	}
	return list;
}

    /**
     *
     * @Title: setSimpleTrigger
     * @Description: 简单调度
     * @param @param inputMap
     * @param @return    参数
     * @return Boolean    返回类型
     * @throws
     */
    @SuppressWarnings({ "unchecked" })
    public void setSimpleTriggerJob(TaskInfo info) {
        LOGGER.info("TaskService--data-s-->setSimpleTriggerJob()" + info);
        String jobName = PATH+info.getJobName();
        String jobGroup = info.getJobGroup();
        String jobDescription = info.getJobDescription();
        Long milliSeconds = Long.parseLong(info.getMilliSeconds());
        Integer repeatCount = Integer.parseInt(info.getRepeatCount());
        //Date start = DateUtils.parseDate(info.getStartDate());
        Date startDate = null;
        try {
            startDate = toDate("2019-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = toDate("2019-12-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);// 触发器的key值
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);// job的key值
            if (checkExists(jobName, jobGroup)) {
                LOGGER.info(
                        "===> AddJob fail, job already exist, jobGroup:{}, jobName:{}",
                        jobGroup, jobName);
//                throw new ServiceException(String.format(
//                        "Job已经存在, jobName:{%s},jobGroup:{%s}", jobName,
//                        jobGroup));
            }
            /* 简单调度 */
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(startDate)
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInMilliseconds(milliSeconds)
                                    .withRepeatCount(repeatCount))
                    .endAt(endDate).build();
            Class<? extends Job> clazz = (Class<? extends Job>) Class
                    .forName(jobName);
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey)
                    .withDescription(jobDescription).build();
            System.out.println(scheduler+"====="+jobDetail+"======"+trigger);
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            LOGGER.info("任务添加失败！--->简单调度" + e.getMessage());
            //throw new ServiceException("任务添加失败！--->简单调度");
        }
    }

    /**
     *
     * @Title: addJob
     * @Description: 保存定时任务
     * @param @param info    参数
     * @return void    返回类型
     * @throws
     */
    @SuppressWarnings("unchecked")
    public void addJob(HashMap<String, Object> rtn,TaskInfo info) {
        LOGGER.info("TaskService--data-s-->addJob()" + info);

        //int i = taskInfoDao.insert(info);
        //System.out.println("=========I==:"+i);
        String jobName = PATH+info.getJobName();
        String jobGroup = info.getJobGroup();
        String cronExpression = info.getCronExpression();
        String jobDescription = info.getJobDescription();
        String createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        String startDate1 = info.getStartDate();
        Date startDate = null;
        try {
            startDate = toDate(startDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        String endDate1 = info.getEndDate();
        //没有结束日期处理为2999-12-31 00:00:00
        if(!"".equals(endDate1)){
            try {
                endDate = toDate(endDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            try {
                endDate = toDate("2999-12-31 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            if (checkExists(jobName, jobGroup)) {
                LOGGER.info(
                        "===> AddJob fail, job already exist, jobGroup:{}, jobName:{}",
                        jobGroup, jobName);
                System.out.println(String.format(
                        "Job已经存在, jobName:{%s},jobGroup:{%s}", jobName,
                        jobGroup));
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            CronScheduleBuilder schedBuilder = CronScheduleBuilder
                    .cronSchedule(cronExpression)
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey).startAt(startDate).withDescription(createTime)
                    .withSchedule(schedBuilder).endAt(endDate).build();

            Class<? extends Job> clazz = (Class<? extends Job>) Class
                    .forName(jobName);
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey)
                    .withDescription(jobDescription).build();
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("定时任务 启动成功");
        } catch (SchedulerException | ClassNotFoundException e) {
            LOGGER.info("保存定时任务-->类名不存在或执行表达式错误--->复杂调度" + e.getMessage());
            //throw new ServiceException("类名不存在或执行表达式错误");
        }
    }

    /**
     *
     * @Title: edit
     * @Description: 修改定时任务
     * @param @param info    参数
     * @return void    返回类型
     * @throws
     */
    public void editJob(TaskInfo info) {
        LOGGER.info("修改批处理" + info);
        String jobName = PATH+info.getJobName();
        String jobGroup = info.getJobGroup();
        String cronExpression = info.getCronExpression();
        String jobDescription = info.getJobDescription();
        String createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            HashMap<String, Object> rtn = new HashMap<>();
            if (!checkExists(jobName, jobGroup)) {
                addJob(rtn,info);
//                System.out.println(
//                        String.format("Job不存在, jobName:{%s},jobGroup:{%s}",
//                                jobName, jobGroup));
            }else{
                TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
                JobKey jobKey = new JobKey(jobName, jobGroup);
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                        .cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionDoNothing();
                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey).withDescription(createTime)
                        .withSchedule(cronScheduleBuilder).build();

                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                jobDetail.getJobBuilder().withDescription(jobDescription);
                HashSet<Trigger> triggerSet = new HashSet<>();
                triggerSet.add(cronTrigger);

                scheduler.scheduleJob(jobDetail, triggerSet, true);
            }

        } catch (SchedulerException e) {
            LOGGER.info("修改定时任务-->类名不存在或执行表达式错误--->复杂调度" + e.getMessage());
            //throw new ServiceException("类名不存在或执行表达式错误");
        }
    }

    /**
     *
     * @Title: delete
     * @Description: 删除定时任务
     * @param @param jobName
     * @param @param jobGroup    参数
     * @return void    返回类型
     * @throws
     */
    public void deleteJob(String jobName, String jobGroup) {
        LOGGER.info("TaskService--data-s-->deleteJob()--jobName:" + jobName
                + "jobGroup:" + jobGroup);
        jobName= PATH+jobName;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                LOGGER.info("===> delete, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            LOGGER.info("删除定时任务-->复杂调度" + e.getMessage());
            //throw new ServiceException(e.getMessage());
        }
    }

    /**
     *
     * @Title: pause
     * @Description: 暂停定时任务
     * @param @param jobName
     * @param @param jobGroup    参数
     * @return void    返回类型
     * @throws
     */
    public void pauseJob(String jobName, String jobGroup) {
        System.out.println("TaskService--data-s-->pauseJob()--jobName:" + jobName
                + "jobGroup:" + jobGroup);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                System.out.println("===> Pause success, triggerKey:{}"+triggerKey);
            }
        } catch (SchedulerException e) {
            LOGGER.info("暂停定时任务-->复杂调度" + e.getMessage());
            //throw new ServiceException(e.getMessage());
        }
    }

    /**
     *
     * @Title: resume
     * @Description: 恢复暂停任务
     * @param @param jobName
     * @param @param jobGroup    参数
     * @return void    返回类型
     * @throws
     */
    public void resumeJob(String jobName, String jobGroup) {
        LOGGER.info("TaskService--data-s-->resumeJob()--jobName:" + jobName
                + "jobGroup:" + jobGroup);
        jobName = PATH + jobName;
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
                LOGGER.info("===> Resume success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            System.out.println("重新开始任务-->复杂调度" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @Title: checkExists
     * @Description: 验证任务是否存在
     * @param @param jobName
     * @param @param jobGroup
     * @param @return
     * @param @throws SchedulerException    参数
     * @return boolean    返回类型
     * @throws
     */
    private boolean checkExists(String jobName, String jobGroup)
            throws SchedulerException {
        System.out.println("TaskService--data-s-->checkExists()--jobName:" + jobName
                + "jobGroup:" + jobGroup);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }

    public Date toDate(String date) throws ParseException {
        //String string = "2016-10-24 21:59:06";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(date);
    }


}
