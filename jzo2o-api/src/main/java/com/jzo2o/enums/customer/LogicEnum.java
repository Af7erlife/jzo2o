package com.jzo2o.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicEnum {
    NO(0, "否"),
    YES(1, "是");

    /**
     * 状态值
     */
    private final int status;

    /**
     * 描述
     */
    private final String description;
}
