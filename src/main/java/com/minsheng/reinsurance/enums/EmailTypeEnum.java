package com.minsheng.reinsurance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public enum EmailTypeEnum {
    SUPERMANAGER(1, "超级管理员邮件列表"),;
    Integer code;
    String name;

    private static Map<Integer, String> keysMap = new HashMap<>();
    private static Map<String, String> keysMapss = new HashMap<>();
    private static List<Map<String, Object>> emailTypeList = new ArrayList<>();

    static {
        for (EmailTypeEnum item : EmailTypeEnum.values()) {
            keysMap.put(item.code, item.name);
            keysMapss.put(item.code.toString(), item.name);
            Map<String, Object> map = new HashMap<>();
            map.put("value", item.code);
            map.put("label", item.getName());
            emailTypeList.add(map);
        }
    }

    public static Map<Integer, String> getKeysMap() {
        return keysMap;
    }

    public static Map<String, String> getKeysMapSS() {
        return keysMapss;
    }

    public static  List<Map<String, Object>> getEmailTypeList() {
        return emailTypeList;
    }

    EmailTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
