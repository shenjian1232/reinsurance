# reinsurance

再保系统

性能监控网址 （通过javamelody实现的）
http://localhost:8080/api/monitoring

邮件功能实现 （通过JavaMailSender实现的邮件发送功能）
需在application.properties文件配置发送邮箱的账号密码（如果是qq邮箱，需到qq邮箱设置-账户里启用smtp开关，密码为qq邮箱生成的授权码）
参考：https://jingyan.baidu.com/article/8065f87fbc12682330249844.html

log4j日记添加：
用org.apache.log4j包下的Logger,通过Logger log =Logger.getLogger(XXX.class)方式获取日志对象，
在log4j.properties配置日志输出格式和输出文件名，输出路径在启动类中设置,rescources文件夹下自
动生成logs文件夹，输出日志按级别分别记录在logs文件夹中不同的文件中（info.log warn.log debug.log error.log）

过滤器配置：
在filterexcludeURL.properties配置文件中配置不过拦截的URL，如添加login_url = /login ,请求路
径中含/login 字符串的请求便不会被拦截。其余访问的URL,如果处于未登录状态，则会重定向到登陆界面


在保系统中添加druid连接池
并配置监控统计功能，DruidConfiguration 
连接池监控页面网址:http://ip:port/druid/index.html