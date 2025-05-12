package com.jzo2o.foundations.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {
    YES(1,"是"),
    NO(0,"否");

    private int code;
    private String desc;
    public boolean equals(Integer code) {
        return this.code == code;
    }
    public boolean equals(StatusEnum statusEnum) {
        return statusEnum != null && statusEnum.code == this.getCode();
    }
}
