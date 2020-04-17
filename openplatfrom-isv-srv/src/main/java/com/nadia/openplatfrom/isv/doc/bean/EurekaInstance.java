package com.nadia.openplatfrom.isv.doc.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class EurekaInstance {
    private String instanceId;
    private String ipAddr;
    private Map<String, Object> port;

    private String status;
    private String statusPageUrl;
    private String healthCheckUrl;
    private String lastUpdatedTimestamp;
    private String homePageUrl;

    public String fetchPort() {
        if (CollectionUtils.isEmpty(port)) {
            return "";
        }
        return String.valueOf(port.getOrDefault("$", ""));
    }

    public String fetchPath(){
        if (StringUtils.isBlank(statusPageUrl)) {
            return "";
        }

        String rgex = homePageUrl + "(.*?)" + "/info";
        String subUtilSimple = getSubUtilSimple(statusPageUrl, rgex);
        if (StringUtils.isBlank(subUtilSimple)) {
            return "";
        }
        return "/" + subUtilSimple;
    }


    public String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";
    }


}
