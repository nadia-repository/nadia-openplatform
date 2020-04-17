package com.nadia.openplatfrom.isv.doc.service;

import com.alibaba.fastjson.JSONObject;
import com.nadia.openplatfrom.isv.doc.domain.DocInfo;
import com.nadia.openplatfrom.isv.doc.domain.DocItem;

import java.util.Map;

public interface DocParser {
    DocInfo parseJson(JSONObject docRoot);

    void parseJson(Map<String, DocItem> apiDefinitionMap, JSONObject docRoot);
}
