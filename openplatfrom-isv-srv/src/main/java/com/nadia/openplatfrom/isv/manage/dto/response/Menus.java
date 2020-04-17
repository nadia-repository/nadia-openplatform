package com.nadia.openplatfrom.isv.manage.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class Menus {
    private Long id;

    private String title;

    private String path;

    private String icon;

    private List<Menus> children;
}
