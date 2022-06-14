package com.example.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@PropertySource(value= {"classpath:application.properties"})
public class DemoInjectionController {

    @Value("${welcome.message:test}")
    private String messageI;

    private String messageNoI = "Hello World";

    @RequestMapping("/welcomeI")
    public String welcomeIJSP(Map<String, Object> model) {
        System.out.println("XXXXXXXXXXX "+this.messageI);
        model.put("message", this.messageI);
        return "welcome";
    }

    @RequestMapping("/welcomeNoI")
    public String welcomeNoIJSP(Map<String, Object> model) {
        model.put("message", this.messageNoI);
        return "welcome";
    }
}
