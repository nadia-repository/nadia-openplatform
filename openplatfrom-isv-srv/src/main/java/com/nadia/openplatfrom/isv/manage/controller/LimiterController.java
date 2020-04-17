package com.nadia.openplatfrom.isv.manage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage")
public class LimiterController {

    @RequestMapping(value = "/limiter/apis", method = RequestMethod.GET)
    public void getApis(){
        

    }

    @RequestMapping(value = "limiter/modify/apis", method = RequestMethod.POST)
    public void updateApis(){

    }
}
