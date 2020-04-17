package com.nadia.openplatfrom.isv.account.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private List<AppDto> apps;

    @Data
    public static class AppDto {
        private Long id;
        private String name;
        private List<BusinessPackageDto> businessPackageDtos;
    }

    @Data
    public static class BusinessPackageDto {
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
