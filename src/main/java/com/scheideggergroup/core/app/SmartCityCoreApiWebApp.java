package com.scheideggergroup.core.app;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.scheideggergroup.core"})
public class SmartCityCoreApiWebApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SmartCityCoreApiWebApp.class, args);
    }
    
}