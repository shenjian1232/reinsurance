package com.minsheng.reinsurance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by panwei on 2017/6/5.
 */

@Primary
@Configuration
@PropertySource(value = "classpath:conf.properties", ignoreResourceNotFound = true)
public class CurrencyConfig {
    private String ver;
    private String menuTreeKey;
    private String userOfficeKey;
    private String officeTreeKey;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getMenuTreeKey() {
        return menuTreeKey;
    }

    public void setMenuTreeKey(String menuTreeKey) {
        this.menuTreeKey = menuTreeKey;
    }

    public String getUserOfficeKey() {
        return userOfficeKey;
    }

    public void setUserOfficeKey(String userOfficeKey) {
        this.userOfficeKey = userOfficeKey;
    }

    public String getOfficeTreeKey() {
        return officeTreeKey;
    }

    public void setOfficeTreeKey(String officeTreeKey) {
        this.officeTreeKey = officeTreeKey;
    }
}
