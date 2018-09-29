package com.darren.cloud.common.transaction;

import lombok.Getter;

/**
 * 事务事件状态
 *
 * @author darren.ouyang
 * @version 2018/9/26 16:16
 */
@Getter
public enum TransactionEventStatusEnum {

    NOT_EXIST ("NOT_EXIST", "错误的状态值,不存在的状态"),

    UNKNOWN("UNKNOWN", "未知"),
    SUCCEED("SUCCEED", "执行成功"),
    FAIL("FAIL", "执行失败")
    ;

    private final String code;
    private final String comment;

    TransactionEventStatusEnum (String code, String comment){
        this.code = code;
        this.comment = comment;
    }


    /**
     * 检测code是否相等
     *
     * @param code code
     * @return 结果
     */
    public boolean equalsCode (String code){
        return this.code.equals(code);
    }

    /**
     * 通过code 获取枚举对象
     *
     * @param code code
     * @return 举对象
     */
    public static TransactionEventStatusEnum getEnum(String code){
        for (TransactionEventStatusEnum enumValue : TransactionEventStatusEnum.values()){
            if (enumValue.equalsCode(code)){
                return enumValue;
            }
        }

        return NOT_EXIST;
    }
}