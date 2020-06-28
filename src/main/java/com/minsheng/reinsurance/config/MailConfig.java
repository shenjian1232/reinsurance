package com.minsheng.reinsurance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by panwei on 2016/6/1.
 */

@Configuration
@PropertySource(value = "classpath:mail.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "email")
public class MailConfig {
    private String serverHost;
    private String serverFrom;
    private String serverUserName;
    private String serverPassWord;
    private String superAdministrator;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerFrom() {
        return serverFrom;
    }

    public void setServerFrom(String serverFrom) {
        this.serverFrom = serverFrom;
    }

    public String getServerUserName() {
        return serverUserName;
    }

    public void setServerUserName(String serverUserName) {
        this.serverUserName = serverUserName;
    }

    public String getServerPassWord() {
        return serverPassWord;
    }

    public void setServerPassWord(String serverPassWord) {
        this.serverPassWord = serverPassWord;
    }

    public String getSuperAdministrator() {
        return superAdministrator;
    }

    public void setSuperAdministrator(String superAdministrator) {
        this.superAdministrator = superAdministrator;
    }
}
