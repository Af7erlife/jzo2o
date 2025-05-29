package com.jzo2o.enums.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    COMMON(1, "普通用户"),
    SERVER(2, "服务人员"),
    AGENCY(3, "服务机构");
    /**
     * 状态值
     */
    private final int status;

    /**
     * 描述
     */
    private final String description;
}
