package com.darren.cloud.common.transaction;

import lombok.Getter;

/**
 * 事务执行器状态
 *
 * @author darren.ouyang
 * @version 2018/9/26 15:19
 */
@Getter
public enum TransactionStatusEnum {

    NOT_EXIST ("NOT_EXIST", "错误的状态值,不存在的状态"),

    START("START", "启动事务"),
    COMMIT("COMMIT", "提交事务"),
    FINISH("FINISH", "事务完成"),
    ERROR("ERROR", "事务最终执行失败,需人工介入"),

    // ROLLBACK("COMMIT", "回滚事务"),
    ;

    private final String code;
    private final String comment;

    TransactionStatusEnum(String code, String comment){
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
    public static TransactionStatusEnum getEnum(String code){
        for (TransactionStatusEnum enumValue : TransactionStatusEnum.values()){
            if (enumValue.equalsCode(code)){
                return enumValue;
            }
        }

        return NOT_EXIST;
    }
}
