package com.nadia.openplatform.common.rest;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Basic api header information ,no contains body
 * @author tony
 */
@Getter
@Setter
@ToString
@ApiModel(description = "RestResponse")
public class RestHeader {
    private long errorCode = 0;
    private String msg = "success";

}