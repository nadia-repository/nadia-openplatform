package com.nadia.openplatfrom.isv.doc.domain;

import lombok.Data;

import java.util.List;

@Data
public class DocInfo {
    private String title;
    private List<DocModule> docModuleList;
}
