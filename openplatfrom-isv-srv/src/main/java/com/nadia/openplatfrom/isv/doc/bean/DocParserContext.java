package com.nadia.openplatfrom.isv.doc.bean;


import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocParserContext {

    /**
     * 忽略显示
     */
    public static volatile Set<String> ignoreHttpMethods = Sets.newHashSet("put", "options", "head", "put", "delete", "patch");

    public static volatile Set<String> ignoreParameters = Sets.newHashSet("accessToken", "Nadia-Agent");

//    public static volatile Map<String,String> serviecs = new HashedMap(){
//        {
//            put("TRADE-BIZ","trade");
//            put("MPF-BIZ","mpf");
//            put("OAUTH-SRV","oauth");
//            put("ACCOUNT-BIZ","account");
//        }
//    };

    public static volatile Map<String, String> serviecs = new HashMap<>();

}
