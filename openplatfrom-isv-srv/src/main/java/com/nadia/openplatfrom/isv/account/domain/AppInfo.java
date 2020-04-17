package com.nadia.openplatfrom.isv.account.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nadia.openplatfrom.isv.manage.utils.JsonDateDeserializer;
import com.nadia.openplatfrom.isv.manage.utils.JsonDateSerializer;

import java.util.Date;

public class AppInfo {
    private Long id;

    private Long isvId;

    private String name;

    private String appKey;

    private Long businessPackageId;

    private String signType;

    private String status;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date createdAt;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using= JsonDateDeserializer.class)
    private Date updatedAt;

    private String pubKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public Long getBusinessPackageId() {
        return businessPackageId;
    }

    public void setBusinessPackageId(Long businessPackageId) {
        this.businessPackageId = businessPackageId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType == null ? null : signType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey == null ? null : pubKey.trim();
    }
}