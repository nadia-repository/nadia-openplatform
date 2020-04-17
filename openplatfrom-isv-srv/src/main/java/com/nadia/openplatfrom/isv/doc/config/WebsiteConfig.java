package com.nadia.openplatfrom.isv.doc.config;

import com.nadia.openplatfrom.isv.doc.service.DocManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebsiteConfig implements ApplicationRunner {

    @Autowired
    DocManager docManager;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        docManager.load();
    }
}
