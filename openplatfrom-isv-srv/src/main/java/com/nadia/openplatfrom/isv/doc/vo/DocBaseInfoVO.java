package com.nadia.openplatfrom.isv.doc.vo;

import lombok.Data;

import java.util.List;

@Data
public class DocBaseInfoVO {
    private String urlTest;
    private String urlProd;
    private List<DocInfoVO> docInfoList;
}
