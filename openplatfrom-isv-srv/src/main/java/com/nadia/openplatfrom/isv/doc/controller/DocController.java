package com.nadia.openplatfrom.isv.doc.controller;

import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatfrom.isv.account.dto.response.BusinessBackageApiResponse;
import com.nadia.openplatfrom.isv.doc.domain.DocItem;
import com.nadia.openplatfrom.isv.doc.service.DocManager;
import com.nadia.openplatfrom.isv.doc.vo.DocBaseInfoVO;
import com.nadia.openplatfrom.isv.doc.vo.DocInfoVO;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    DocManager docManager;

    @RequestMapping(value = "/businessPackage/apis", method = RequestMethod.GET)
    public RestBody<BusinessBackageApiResponse> getApis(){
        RestBody<BusinessBackageApiResponse> response = new RestBody<>();
        BusinessBackageApiResponse apiInfos = apiInfoService.getApiInfos();
        response.setData(apiInfos);
        return response;
    }


    @RequestMapping(value = "/businessPackage/apiInfo/{method:.+}", method = RequestMethod.GET)
    public RestBody<DocItem> getApiInfo(@PathVariable("method") String method){
        RestBody<DocItem> response = new RestBody<>();
        DocItem docItem = docManager.get(method);
        response.setData(docItem);
        return response;
    }

    @RequestMapping(value = "/businessPackage/apiInfos", method = RequestMethod.GET)
    public RestBody<Collection<DocItem>> getApiInfo(){
        RestBody<Collection<DocItem>> response = new RestBody<>();
        Collection<DocItem> docItems = docManager.get();
        response.setData(docItems);
        return response;
    }

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public void load(){
        docManager.load();
    }

    @RequestMapping(value = "/getDocBaseInfo", method = RequestMethod.GET)
    public DocBaseInfoVO getDocBaseInfo() {
        List<DocInfoVO> docInfoList = docManager.listAll()
                .stream()
                .map(docInfo -> {
                    DocInfoVO vo = new DocInfoVO();
                    BeanUtils.copyProperties(docInfo, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        DocBaseInfoVO baseInfoVO = new DocBaseInfoVO();
//        baseInfoVO.setUrlTest(urlTest);
//        baseInfoVO.setUrlProd(urlProd);
        baseInfoVO.setDocInfoList(docInfoList);
        return baseInfoVO;
    }


}
