package com.nadia.openplatfrom.isv.doc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.nadia.openplatform.common.utils.StringUtils;
import com.nadia.openplatfrom.isv.doc.bean.*;
import com.nadia.openplatfrom.isv.doc.domain.DocInfo;
import com.nadia.openplatfrom.isv.doc.domain.DocItem;
import com.nadia.openplatfrom.isv.doc.service.DocManager;
import com.nadia.openplatfrom.isv.doc.service.DocParser;
import com.nadia.openplatfrom.isv.doc.vo.ServiceInfoVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocManagerImpl implements DocManager {

    // key:title
    Map<String, DocInfo> docDefinitionMap = new HashMap<>();

    Map<String, DocItem> apiDefinitionMap = new HashMap<>();

    // key: name+version
    Map<String, DocItem> docItemMap = new HashMap<>();


    OkHttpClient client = new OkHttpClient();

    RestTemplate restTemplate = new RestTemplate();

    DocParser swaggerDocParser = new SwaggerDocParser();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

//    DelayQueue<Msg> queue = new DelayQueue<>();

    private String secret = "b749a2ec000f4f29";

    @Autowired
    private Environment environment;

    private String eurekaUrl;

    private volatile boolean listenInited;

    @Value("${address.ip.port}")
    private String host;

    @Override
    public void load() {
        try {
            // {"STORY-SERVICE":[{"ipAddr":"10.1.30.54","name":"STORY-SERVICE","serverPort":"2222"}],"API-GATEWAY":[{"ipAddr":"10.1.30.54","name":"API-GATEWAY","serverPort":"8081"}]}
            Map<String, List<ServiceInfoVO>> listMap = this.getAllServiceList();
            log.info("服务列表：{}", JSON.toJSONString(listMap.keySet()));
            listMap.entrySet()
                    .stream()
                    // 网关没有文档提供，需要排除
                    .filter(entry -> !"API-GATEWAY".equalsIgnoreCase(entry.getKey()))
                    .forEach(entry -> {
                        ServiceInfoVO serviceInfoVo = entry.getValue().get(0);
                        loadDocInfo(serviceInfoVo);
                    });
        } catch (Exception e) {
            log.error("加载失败", e);
        }
    }

    protected void loadDocInfo(ServiceInfoVO serviceInfoVo) {
        String path = DocParserContext.serviecs.get(serviceInfoVo.getName());
        if(StringUtils.isBlank(path)){
            return;
        }
        String address = StringUtils.isNotBlank(host)?host:serviceInfoVo.getIpAddr() + ":" + serviceInfoVo.getServerPort();
        String url = "http://" + address  + "/" + path + "/v2/api-docs";
        log.info("loadDocInfo url:{}",url);
        try {
            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            log.info("loadDocInfo >:{}",JSONObject.toJSONString(entity));

            if (entity.getStatusCode() != HttpStatus.OK) {
                throw new IllegalAccessException("无权访问");
            }
            String docInfoJson = entity.getBody();
            JSONObject docRoot = JSON.parseObject(docInfoJson, Feature.OrderedField, Feature.DisableCircularReferenceDetect);
            DocParser docParser = this.buildDocParser(docRoot);
//            DocInfo docInfo = docParser.parseJson(docRoot);
//            docDefinitionMap.put(docInfo.getTitle(), docInfo);
            docParser.parseJson(apiDefinitionMap,docRoot);
        } catch (Exception e) {
            // 这里报错可能Yes因为有些微服务没有配置swagger文档，导致404访问不到
            // 这里catch跳过即可
            log.warn("读取文档失败, url:{}, msg:{}", url, e.getMessage());
        }
    }

    protected String buildQuery() {
        String time = String.valueOf(System.currentTimeMillis());
        String source = secret + time + secret;
        String sign = DigestUtils.md5DigestAsHex(source.getBytes());
        return "?time=" + time + "&sign=" + sign;
    }

    protected DocParser buildDocParser(JSONObject rootDoc) {
        return swaggerDocParser;
    }

    @Override
    public DocItem get(String method, String version) {
        return docItemMap.get(method + version);
    }

    @Override
    public DocItem get(String method) {
        return apiDefinitionMap.get(method);
    }

    @Override
    public Collection<DocItem> get() {
        return apiDefinitionMap.values();
    }

    @Override
    public DocInfo getByTitle(String title) {
        return docDefinitionMap.get(title);
    }

    @Override
    public Collection<DocInfo> listAll() {
        return docDefinitionMap.values();
    }

    protected Map<String, List<ServiceInfoVO>> getAllServiceList() throws IOException {
        String json = this.requestEurekaServer(EurekaUri.QUERY_APPS);
        EurekaApps eurekaApps = JSON.parseObject(json, EurekaApps.class);

        List<ServiceInfoVO> serviceInfoVoList = new ArrayList<>();
        List<EurekaApplication> applicationList = eurekaApps.getApplications().getApplication();
        applicationList.stream()
                .forEach(eurekaApplication -> {
                    List<EurekaInstance> instanceList = eurekaApplication.getInstance();
                    for (EurekaInstance instance : instanceList) {
                        if ("UP".equals(instance.getStatus())) {
                            ServiceInfoVO vo = new ServiceInfoVO();
                            vo.setName(eurekaApplication.getName());
                            vo.setIpAddr(instance.getIpAddr());
                            vo.setServerPort(instance.fetchPort());
                            vo.setPath(instance.fetchPath());
                            serviceInfoVoList.add(vo);
                        }
                    }
                });

        Map<String, List<ServiceInfoVO>> listMap = serviceInfoVoList.stream()
                .collect(Collectors.groupingBy(ServiceInfoVO::getName));

        return listMap;
    }

    protected String requestEurekaServer(EurekaUri eurekaUri, String... args) throws IOException {
        Request request = eurekaUri.getRequest(this.eurekaUrl, args);
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            log.error("操作失败，url:{}, msg:{}, code:{}", eurekaUri.getUri(args), response.message(), response.code());
            throw new RuntimeException("操作失败");
        }
    }

    @PostConstruct
    protected void after() throws Exception {
        String eurekaUrls = environment.getProperty("eureka.client.serviceUrl.defaultZone");
        log.info("eureka defaultZone:{}",eurekaUrls);
        if (StringUtils.isBlank(eurekaUrls)) {
            throw new IllegalArgumentException("未指定eureka.client.serviceUrl.defaultZone参数");
        }
        String url = eurekaUrls.split("\\,")[0];
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        log.info("eureka url:{}",url);

        this.eurekaUrl = url;

//        this.listenServiceId();
    }

}
