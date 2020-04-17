package com.nadia.openplatfrom.isv.doc.domain;

import lombok.Data;

import java.util.List;

@Data
public class DocModule {
    private String module;
    private List<DocItem> docItems;
}
