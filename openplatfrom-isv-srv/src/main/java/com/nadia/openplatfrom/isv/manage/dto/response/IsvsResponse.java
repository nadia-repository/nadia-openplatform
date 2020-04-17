package com.nadia.openplatfrom.isv.manage.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nadia.openplatfrom.isv.manage.utils.JsonDateDeserializer;
import com.nadia.openplatfrom.isv.manage.utils.JsonDateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class IsvsResponse {
    private Long id;

    private String name;

    private String mobile;

    private String email;

    private String status;

    private Long roleId;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date createdAt;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date updatedAt;
}
