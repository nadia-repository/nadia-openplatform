package com.nadia.openplatfrom.isv.account.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BusinessBackageApiResponse {
    private List<BusinessBackageDto> businessBackageInfos;

    @Data
    public static class BusinessBackageDto {
        private Long id;
        private String name;
        private String status;
        private List<ApiInfoDto> ApiInfos;
    }

    @Data
    public static class ApiInfoDto {
        private Long id;
        private String name;
        private String srvName;
        private String status;
    }
}
