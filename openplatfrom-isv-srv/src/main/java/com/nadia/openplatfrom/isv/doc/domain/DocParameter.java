package com.nadia.openplatfrom.isv.doc.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.nadia.openplatform.common.utils.StringUtils;
import lombok.Data;

import java.util.List;

@Data
public class DocParameter {
    private String module;
    private String name;
    private String type;
    private String maxLength = "-";
    private boolean required;
    private String description;
    private String example = "";

    @JSONField(name = "x-example")
    private String x_example = "";

    private List<DocParameter> refs;

    public String getParamExample() {
        return StringUtils.isBlank(example) ? x_example : example;
    }
}
