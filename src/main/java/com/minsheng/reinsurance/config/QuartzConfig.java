package com.minsheng.reinsurance.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 *
 */
@Configuration
public class QuartzConfig {
    /**
     * 方法名：
     * 功能：配置定时任务
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/10 12:06
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    //@Bean(name = "jobDetail")
//    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduledTask task){
//        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//        /*
//         * 是否并发执行
//         * 例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
//         * 如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
//         */
//        jobDetail.setConcurrent(false);
//        //设置定时任务的名字
//        jobDetail.setName("srd-demo");
//        //设置任务的分组，这些属性都可以在数据库中，在多任务的时候使用
//        jobDetail.setGroup("srd");
//
//        //为需要执行的实体类对应的对象
//        jobDetail.setTargetObject(task);
//
//        /*
//         * dealDate为需要执行的方法
//         * 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的dealDate方法
//         */
//        jobDetail.setTargetMethod("dealDate");
//        System.out.println("jobDetail 初始化成功！");
//        return jobDetail;
//    }

    /**
     * 方法名：
     * 功能：配置定时任务的触发器，也就是什么时候触发执行定时任务
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/10 12:14
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
   // @Bean(name = "jobTrigger")
//    public CronTriggerFactoryBean cronTriggerFactoryBean(MethodInvokingJobDetailFactoryBean jobDetail){
//        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
//        trigger.setJobDetail(jobDetail.getObject());
//        //初始化的cron表达式(每天上午10:15触发)
//        trigger.setCronExpression("0 0/2 * * * ?");
//        //trigger的name
//        trigger.setName("srd-demo");
//        System.out.println("jobTrigger 初始化成功！");
//        return trigger;
//    }

    /**
     * 方法名：
     * 功能：定义quartz调度工厂
     * 描述：
     * 创建人：typ
     * 创建时间：2018/10/10 14:06
     * 修改人：
     * 修改描述：
     * 修改时间：
     */
    //@Bean
//    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger){
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        //用于quartz集群，QuartzScheduler启动时更新已存在的job
//        factoryBean.setOverwriteExistingJobs(true);
//        //延时启动，应用启动1秒后
//        factoryBean.setStartupDelay(1);
//        //注册触发器
//        factoryBean.setTriggers(trigger);
//        System.out.println("scheduler 初始化成功！");
//        return factoryBean;
//    }

//    @Autowired
//    private SchedulerFactoryBean schedulerFactoryBean;
//
//    public void createNewTask(String expression, int taskId) throws SchedulerException {
//        TriggerKey triggerKey =TriggerKey.triggerKey("TASK-" + taskId, "JOB-" +taskId);
//        CronTrigger trigger = null;
//        // 不存在，创建一个
//        JobKey jobKey = new JobKey("TASK-" + taskId, "JOB-" + taskId);
//        JobDetail jobDetail = JobBuilder.newJob(SpringQuartzJob.class).withIdentity(jobKey).build();
//        // 稽核任务基础信息
//        jobDetail.getJobDataMap().put("taskId",taskId);
//        // 表达式调度构建器
//        CronScheduleBuilder cronScheduleBuilder = null;
//        cronScheduleBuilder =CronScheduleBuilder.cronSchedule(expression);
//        // 按cronExpression表达式构建一个新的trigger
//        trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(newDate()).withSchedule(cronScheduleBuilder).build();
//        // 加入任务队列
//        Scheduler scheduler =schedulerFactoryBean.getScheduler();
//        scheduler.scheduleJob(jobDetail,trigger);
//        scheduler.rescheduleJob(triggerKey,trigger);
//    }


//    @Autowired
//    private MyJobFactory myFactory;  //自定义的factory
    private MyJobFactory jobFactory;

    public QuartzConfig(MyJobFactory jobFactory){
        this.jobFactory = jobFactory;
    }
//    @Value("${spring.datasource.url}")
//    private String url;// 数据源地址
//
//    @Value("${spring.datasource.username}")
//    private String userName;// 用户名
//
//    @Value("${spring.datasource.password}")
//    private String password;// 密码

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        // Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 设置自定义Job Factory，用于Spring管理Job bean
        factory.setJobFactory(jobFactory);
        return factory;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
    /**
     *
     * @Title: quartzProperties
     * @Description: 设置quartz属性
     * @param @return
     * @param @throws IOException    参数
     * @return Properties    返回类型
     * @throws
     */
    public Properties quartzProperties() throws IOException {
        Properties prop = new Properties();
//        prop.put("org.quartz.scheduler.instanceName", "quartzScheduler");// 调度器的实例名
//        prop.put("org.quartz.scheduler.instanceId", "AUTO");// 实例的标识
//            prop.put("org.quartz.scheduler.skipUpdateCheck", "true");// 检查quartz是否有版本更新（true 不检查）
//            prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
//        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
//        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");// 表名前缀
//        prop.put("org.quartz.jobStore.isClustered", "false");// 集群开关
//        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");// 线程池的名字
//            prop.put("org.quartz.threadPool.threadCount", "10");// 指定线程数量
//           prop.put("org.quartz.threadPool.threadPriority", "5");// 线程优先级（1-10）默认为5
//           prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
//           prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
//
//            prop.put("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.jdbc.Driver");
//            prop.put("org.quartz.dataSource.quartzDataSource.URL", url);
//        prop.put("org.quartz.dataSource.quartzDataSource.user", userName);
//        prop.put("org.quartz.dataSource.quartzDataSource.password", password);
//        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "50");
            return prop;
    }



}
