--  批处理表
 create table task_info(
    Id int primary key auto_increment,
    jobName varchar(100),
     jobGroup varchar(100),
		 jobDescription varchar(100),
		 jobStatus varchar(1),
		 cronExpression varchar(100),
		 createTime varchar(100),
		 milliSeconds varchar(100),
		 repeatCount varchar(100),
		 startDate varchar(100),
		 endDate varchar(100),
		 del_flag varchar(1)
 )COMMENT = '批处理表';

alter TABLE task_info MODIFY COLUMN Id int(11) COMMENT 'id，自增';
alter TABLE task_info MODIFY COLUMN jobName varchar(100) COMMENT '批处理方法名';
alter TABLE task_info MODIFY COLUMN jobGroup varchar(100) COMMENT '任务组';
alter TABLE task_info MODIFY COLUMN jobDescription varchar(100) COMMENT '批处理描述';
alter TABLE task_info MODIFY COLUMN jobStatus varchar(1) COMMENT '状态，1：启动，2：停止';
alter TABLE task_info MODIFY COLUMN cronExpression varchar(100) COMMENT '定时表达式';
alter TABLE task_info MODIFY COLUMN milliSeconds varchar(100) COMMENT '任务间隔时间';
alter TABLE task_info MODIFY COLUMN repeatCount varchar(100) COMMENT '任务执行次数';
alter TABLE task_info MODIFY COLUMN startDate varchar(100) COMMENT '任务开始时间';
alter TABLE task_info MODIFY COLUMN endDate varchar(100) COMMENT '任务结束时间';
alter TABLE task_info MODIFY COLUMN del_flag varchar(1) COMMENT '是否删除标志，2：删除';
-- 批处理日志表
create table task_run_log
(
	id int(32) primary key auto_increment,
  serialno         VARCHAR(32) not null,
  taskId         int(11),
	jobName         varchar(100),
  startTime      VARCHAR(100),
  finishTime       VARCHAR(8),
  executeState     VARCHAR(1) not null,
  executeResult    VARCHAR(600)
)COMMENT = '批处理日志表';
alter TABLE task_run_log MODIFY COLUMN id int(11) COMMENT 'id，自增';
alter TABLE task_run_log MODIFY COLUMN serialno varchar(32) COMMENT '序列好，uuid';
alter TABLE task_run_log MODIFY COLUMN taskId int(11) COMMENT '任务id';
alter TABLE task_run_log MODIFY COLUMN jobName varchar(100) COMMENT '方法名';
alter TABLE task_run_log MODIFY COLUMN startTime varchar(1) COMMENT '开始时间';
alter TABLE task_run_log MODIFY COLUMN finishTime varchar(100) COMMENT '结束时间';
alter TABLE task_run_log MODIFY COLUMN executeState varchar(1) COMMENT '执行状态1：执行中，2：成功，3：失败';
alter TABLE task_run_log MODIFY COLUMN executeResult varchar(600) COMMENT '执行结果描述';