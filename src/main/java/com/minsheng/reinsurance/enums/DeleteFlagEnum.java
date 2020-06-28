package com.minsheng.reinsurance.enums;

/**
 *
 */
public enum DeleteFlagEnum {

    NORMAL(1), DELETED(2) ;

    private int code;

    private DeleteFlagEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isRest() {
        return false;
    }
}
