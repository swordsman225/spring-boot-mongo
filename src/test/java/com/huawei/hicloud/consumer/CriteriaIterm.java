package com.huawei.hicloud.consumer;

import lombok.Data;

import java.util.List;

@Data
public class CriteriaIterm {

    private String key;

    private String cmd;

    private String value;

    private List<Object> values;


}
