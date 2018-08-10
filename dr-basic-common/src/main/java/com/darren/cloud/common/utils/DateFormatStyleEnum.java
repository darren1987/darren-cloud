package com.darren.cloud.common.utils;

import lombok.Getter;

/**
 * 时间格式化枚举
 *
 * @author darren.ouyang
 * @version 2018/8/8 17:57
 */
@Getter
public enum DateFormatStyleEnum {

    //
    CN_DATE_BASIC_STYLE("yyyy-MM-dd HH:mm:ss"),
    //
    CN_DATE_BASIC_STYLE2("yyyy/MM/dd HH:mm:ss"),
    //
    CN_DATE_BASIC_STYLE3("yyyy/MM/dd"),
    //
    CN_DATE_BASIC_STYLE4("yyyy-MM-dd"),
    //
    CN_DATE_BASIC_STYLE5("yyyyMMdd"),
    //
    CN_DATE_BASIC_STYLE6("yyyy-MM"),
    //
    CN_DATE_BASIC_STYLE7("yyyyMM"),
    //
    DATE_TIMESTAMP_STYLE("yyyyMMddHHmmss"),
    //
    DATE_TIMESTAMPS_STYLE("yyyyMMddHHmmssSSS"),
    //
    DATE_TIMESTAMPS_STYLE_HOUS("yyyy-MM-dd HH");

    private String dateStyle;

    DateFormatStyleEnum(String dateStyle) {
        this.dateStyle = dateStyle;
    }


}
