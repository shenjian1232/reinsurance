package com.minsheng.reinsurance.filter;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 *
 */
@Component
public class LoginFilter implements Filter {

    private static final Logger LOG =Logger.getLogger(LoginFilter.class);

    //不需要拦截的路径   正则表达式
//    private static final String NO_INTERCEPTOR_PATH = ".*/css/|/js/|/login/.*";
    Properties prop = new Properties();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            prop.load(this.getClass().getResourceAsStream("/filterexcludeURL.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        boolean flag = true; //校验标志

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestPath = request.getRequestURI();

        LOG.info("======开始过滤=====");
        LOG.info("请求地址为"+requestPath);

        boolean excludeUrl = false;
        Set<Object> keys = prop.keySet();//返回属性key的集合
        for (Object key : keys) {
            if(requestPath.contains((String)prop.get(key))){

                excludeUrl = true;
            }
        }
//        不对匹配的路径进行拦截
        if(excludeUrl){
            LOG.info("该请求地址无需拦截");
        }else{
            Subject currentUser = SecurityUtils.getSubject();
            if (null != currentUser) {
                Session session = currentUser.getSession();
                if (session.getAttribute("userId") == null) {
                    LOG.info("用户没有登陆！！！！");
                    response.sendRedirect("/api/login");
                    flag = false;
                }else{
                    LOG.info("用户已经登陆，放行");
                }
            }else{
                LOG.info("currentUser为空！！");
            }
        }

        if(flag){
// 放行
            filterChain.doFilter(request,response);
        }


    }

    @Override
    public void destroy() {

    }
}
