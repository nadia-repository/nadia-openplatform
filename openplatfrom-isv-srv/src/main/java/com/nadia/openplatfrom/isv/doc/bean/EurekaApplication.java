package com.nadia.openplatfrom.isv.doc.bean;

import lombok.Data;

import java.util.List;

@Data
public class EurekaApplication {
    private String name;
    private List<EurekaInstance> instance;
}
