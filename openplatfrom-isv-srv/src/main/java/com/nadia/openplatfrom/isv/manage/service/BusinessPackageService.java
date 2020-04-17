package com.nadia.openplatfrom.isv.manage.service;


import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.manage.domain.BusinessPackage;
import com.nadia.openplatfrom.isv.manage.dto.request.BusinessPackageRequest;
import com.nadia.openplatfrom.isv.manage.dto.response.BusinessPackagesAndApisResponse;

import java.util.List;

public interface BusinessPackageService {
    List<BusinessPackage> getBusinessPackageById(List<Long> ids);

    BusinessPackage getBusinessPackageById(Long id);

    List<BusinessPackage> getBusinessPackageByName(String name);

    List<BusinessPackage> getBusinessPackageByIsv(Long isvId);

    List<BusinessPackage> getBusinessPackages();

    void addBusinessPackage(BusinessPackageRequest businessPackageRequest);

    void modifyBusinessPackage(BusinessPackageRequest businessPackageRequest);

    List<ApiResponse.BusinessPackageDto> getBusinessPackagesAndApis();

    List<BusinessPackagesAndApisResponse> getBusinessPackagesAndApisById(Long id) throws Exception;
}
