package com.nadia.openplatfrom.isv.doc.bean;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

public enum EurekaUri {

    /**
     * 查询所有实例 Query for all instances
     */
    QUERY_APPS(RequestMethod.GET, "/apps"),
    /**
     * 下线 Take instance out of service
     */
    OFFLINE_SERVICE(RequestMethod.PUT, "/apps/%s/%s/status?value=OUT_OF_SERVICE"),
    /**
     * 上线 Move instance back into service (remove override)
     */
    ONLINE_SERVICE(RequestMethod.DELETE, "/apps/%s/%s/status?value=UP"),
    ;
    public static final String URL_PREFIX = "/";

    String uri;
    RequestMethod requestMethod;

    EurekaUri(RequestMethod httpMethod, String uri) {
        if (!uri.startsWith(URL_PREFIX)) {
            uri = "/" + uri;
        }
        this.uri = uri;
        this.requestMethod = httpMethod;
    }

    public String getUri(String... args) {
        if (ArrayUtils.isEmpty(args)) {
            return uri;
        }
        Object[] param = ArrayUtils.clone(args);
        return String.format(uri, param);
    }

    public Request getRequest(String url, String... args) {
        String requestUrl = url + getUri(args);
        Request request = this.getBuilder()
                .url(requestUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        return request;
    }

    public Request.Builder getBuilder() {
        String method = requestMethod.name();
        RequestBody requestBody = null;
        if (HttpMethod.requiresRequestBody(method)) {
            MediaType contentType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
            requestBody = RequestBody.create(contentType, "{}");
        }
        return new Request.Builder().method(requestMethod.name(), requestBody);
    }
}
