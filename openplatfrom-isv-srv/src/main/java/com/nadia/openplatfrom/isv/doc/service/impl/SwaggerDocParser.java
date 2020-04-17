package com.nadia.openplatfrom.isv.doc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.nadia.openplatform.common.utils.StringUtils;
import com.nadia.openplatfrom.isv.doc.bean.DocParserContext;
import com.nadia.openplatfrom.isv.doc.domain.DocInfo;
import com.nadia.openplatfrom.isv.doc.domain.DocItem;
import com.nadia.openplatfrom.isv.doc.domain.DocModule;
import com.nadia.openplatfrom.isv.doc.domain.DocParameter;
import com.nadia.openplatfrom.isv.doc.service.DocParser;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


public class SwaggerDocParser implements DocParser {
    @Override
    public DocInfo parseJson(JSONObject docRoot) {
        String title = docRoot.getJSONObject("info").getString("title");
        List<DocItem> docItems = new ArrayList<>();

        JSONObject paths = docRoot.getJSONObject("paths");
        Set<String> pathNameSet = paths.keySet();
        for (String apiPath : pathNameSet) {
            JSONObject pathInfo = paths.getJSONObject(apiPath);
            // key: get,post,head...
            Collection<String> httpMethodList = getHttpMethods(pathInfo);
            Optional<String> first = httpMethodList.stream().findFirst();
            if (first.isPresent()) {
                String method = first.get();
                JSONObject docInfo = pathInfo.getJSONObject(method);
                DocItem docItem = buildDocItem(docInfo, docRoot,null);
                if (docItem.isUploadRequest()) {
                    docItem.setHttpMethodList(Sets.newHashSet("post"));
                } else {
                    docItem.setHttpMethodList(httpMethodList);
                }
                docItems.add(docItem);
            }
        }

        List<DocModule> docModuleList = docItems.stream()
                .collect(Collectors.groupingBy(DocItem::getModule))
                .entrySet()
                .stream()
                .map(entry -> {
                    DocModule docModule = new DocModule();
                    docModule.setModule(entry.getKey());
                    docModule.setDocItems(entry.getValue());
                    return docModule;
                })
                .collect(Collectors.toList());


        DocInfo docInfo = new DocInfo();
        docInfo.setTitle(title);
        docInfo.setDocModuleList(docModuleList);
        return docInfo;
    }

    @Override
    public void parseJson(Map<String, DocItem> apiDefinitionMap, JSONObject docRoot) {
        JSONObject paths = docRoot.getJSONObject("paths");
        Set<String> pathNameSet = paths.keySet();
        for (String pathName : pathNameSet) {
            JSONObject pathInfo = paths.getJSONObject(pathName);
            Set<String> pathSet = pathInfo.keySet();
            Optional<String> first = pathSet.stream().findFirst();
            if (first.isPresent()) {
                String path = first.get();
                String buildPath = buildName(pathName);
                JSONObject docInfo = pathInfo.getJSONObject(path);
                DocItem docItem = buildDocItem(docInfo, docRoot,buildPath);
                apiDefinitionMap.put(buildPath,docItem);
            }
        }
    }


    protected String buildName(String path) {
        path = org.springframework.util.StringUtils.trimLeadingCharacter(path, '/');
        path = org.springframework.util.StringUtils.trimTrailingCharacter(path, '/');
        path = path.replace("/", ".");
        return path;
    }

    protected Collection<String> getHttpMethods(JSONObject pathInfo) {
        // key: get,post,head...
        List<String> retList;
        Set<String> httpMethodList = pathInfo.keySet();
        if (httpMethodList.size() <= 2) {
            retList = new ArrayList<>(httpMethodList);
        } else {
            Set<String> ignoreHttpMethods = DocParserContext.ignoreHttpMethods;
            retList = httpMethodList.stream()
                    .filter(method -> !ignoreHttpMethods.contains(method.toLowerCase()))
                    .collect(Collectors.toList());
        }
        Collections.sort(retList);
        return retList;
    }

    protected DocItem buildDocItem(JSONObject docInfo, JSONObject docRoot,String buildPath) {
        DocItem docItem = new DocItem();
        StringBuilder name = new StringBuilder();
        name.append(buildPath);
        if(StringUtils.isNotBlank(docInfo.getString("summary"))){
            name.append("(");
            name.append(docInfo.getString("summary"));
            name.append(")");
        }
        docItem.setName(name.toString());
        docItem.setVersion(docInfo.getString("sop_version"));
        docItem.setSummary(docInfo.getString("summary"));
        docItem.setDescription(docInfo.getString("description"));
        docItem.setMultiple(docInfo.getString("multiple") != null);
        String moduleName = this.buildModuleName(docInfo, docRoot);
        docItem.setModule(moduleName);
        List<DocParameter> docParameterList = this.buildRequestParameterList(docInfo, docRoot);
        docItem.setRequestParameters(docParameterList);

        List<DocParameter> responseParameterList = this.buildResponseParameterList(docInfo, docRoot);
        docItem.setResponseParameters(responseParameterList);

        return docItem;
    }

    protected String buildModuleName(JSONObject docInfo, JSONObject docRoot) {
        String title = docRoot.getJSONObject("info").getString("title");
        JSONArray tags = docInfo.getJSONArray("tags");
        if (tags != null && tags.size() > 0) {
            return tags.getString(0);
        }
        return title;
    }

    protected List<DocParameter> buildRequestParameterList(JSONObject docInfo, JSONObject docRoot) {
        Optional<JSONArray> parametersOptional = Optional.ofNullable(docInfo.getJSONArray("parameters"));
        JSONArray parameters = parametersOptional.orElse(new JSONArray());
        List<DocParameter> docParameterList = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            JSONObject fieldJson = parameters.getJSONObject(i);
            DocParameter docParameter = fieldJson.toJavaObject(DocParameter.class);
            if(DocParserContext.ignoreParameters.contains(docParameter.getName())){
                continue;
            }
            String ref = getRequestRef(fieldJson);
            List<DocParameter> reqParameterList = Collections.emptyList();
            if (StringUtils.isNotBlank(ref)) {
                reqParameterList = this.buildDocParameters(ref, docRoot);
            }
            docParameter.setRefs(reqParameterList);
            docParameterList.add(docParameter);
        }

        Map<String, List<DocParameter>> collect = docParameterList.stream()
                .filter(docParameter -> docParameter.getName().contains("."))
                .map(docParameter -> {
                    String name = docParameter.getName();
                    int index = name.indexOf('.');
                    String module = name.substring(0, index);
                    String newName = name.substring(index + 1);
                    DocParameter ret = new DocParameter();
                    BeanUtils.copyProperties(docParameter, ret);
                    ret.setName(newName);
                    ret.setModule(module);
                    return ret;
                })
                .collect(Collectors.groupingBy(DocParameter::getModule));

        collect.entrySet().stream()
                .forEach(entry -> {
                    DocParameter moduleDoc = new DocParameter();
                    moduleDoc.setName(entry.getKey());
                    moduleDoc.setType("object");
                    moduleDoc.setRefs(entry.getValue());
                    docParameterList.add(moduleDoc);
                });

        List<DocParameter> ret = docParameterList.stream()
                .filter(docParameter -> !docParameter.getName().contains("."))
                .collect(Collectors.toList());

        return ret;
    }

    protected List<DocParameter> buildResponseParameterList(JSONObject docInfo, JSONObject docRoot) {
        String responseRef = getResponseRef(docInfo);
        List<DocParameter> respParameterList = Collections.emptyList();
        if (StringUtils.isNotBlank(responseRef)) {
            respParameterList = this.buildDocParameters(responseRef, docRoot);
        }
        return respParameterList;
    }

    protected List<DocParameter> buildDocParameters(String ref, JSONObject docRoot) {
        JSONObject responseObject = docRoot.getJSONObject("definitions").getJSONObject(ref);
        JSONObject properties = responseObject.getJSONObject("properties");
        Set<String> fieldNames = properties.keySet();
        List<DocParameter> docParameterList = new ArrayList<>();
        for (String fieldName : fieldNames) {
            /*
            {
                    "description": "分类故事",
                    "$ref": "#/definitions/StoryVO",
                    "originalRef": "StoryVO"
                }
             */
            JSONObject fieldInfo = properties.getJSONObject(fieldName);
            DocParameter respParam = fieldInfo.toJavaObject(DocParameter.class);
            if(!CollectionUtils.isEmpty(responseObject.getJSONArray("required")) &&
                    responseObject.getJSONArray("required").contains(fieldName)){
                respParam.setRequired(true);
            }
            respParam.setName(fieldName);
            docParameterList.add(respParam);
            String originalRef = isArray(fieldInfo) ? getRef(fieldInfo.getJSONObject(fieldName)) : getRef(fieldInfo);
            if (StringUtils.isNotBlank(originalRef)) {
                List<DocParameter> refs = buildDocParameters(originalRef, docRoot);
                respParam.setRefs(refs);
            }
        }
        return docParameterList;
    }

    protected boolean isArray(JSONObject fieldInfo) {
        return "array".equalsIgnoreCase(fieldInfo.getString("type"));
    }

    private String getRef(JSONObject fieldInfo) {
        String ref = Optional.ofNullable(fieldInfo)
                .map(jsonObject -> jsonObject.getString("$ref"))
                .orElse(null);
        return fetchRef(ref);
    }

    protected String getResponseRef(JSONObject docInfo) {
        String ref = Optional.ofNullable(docInfo.getJSONObject("responses"))
                .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getJSONObject("200")))
                .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getJSONObject("schema")))
                .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getString("$ref")))
                .orElse("");
        return fetchRef(ref);
    }

    protected String getRequestRef(JSONObject docInfo) {
        String ref = Optional.ofNullable(docInfo.getJSONObject("schema"))
                .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getString("$ref")))
                .orElse("");
        return fetchRef(ref);
    }

    public String fetchRef(String ref){
        if (org.apache.commons.lang3.StringUtils.isBlank(ref)) {
            return "";
        }

        return ref.split("#/definitions/")[1];
    }


}
