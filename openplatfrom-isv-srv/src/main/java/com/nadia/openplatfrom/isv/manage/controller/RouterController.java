package com.nadia.openplatfrom.isv.manage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage")
public class RouterController {

    @RequestMapping(value = "/router/apis", method = RequestMethod.GET)
    public void getApis(){

    }

    @RequestMapping(value = "router/modify/apis", method = RequestMethod.POST)
    public void updateApis(){

    }
}
