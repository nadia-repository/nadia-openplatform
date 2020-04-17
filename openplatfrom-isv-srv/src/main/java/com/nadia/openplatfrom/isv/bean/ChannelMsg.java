package com.nadia.openplatfrom.isv.bean;

import lombok.Data;

@Data
public class ChannelMsg {
    public ChannelMsg(String operation, Object data) {
        this.operation = operation;
        this.data = data;
    }

    private String operation;
    private Object data;
}
