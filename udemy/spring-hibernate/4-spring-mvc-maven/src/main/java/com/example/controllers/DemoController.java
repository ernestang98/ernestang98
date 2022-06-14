package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    // Return JSP, index page
    @GetMapping("/")
    public String index(Model m) {
        m.addAttribute("someAttribute", "Hello World - Index.jsp");
        return "index";
    }

    // Return JSP, test page
    @RequestMapping(value="/test")
    public String testJSP() {
        return "test";
    }

    // Return JSP, home page
    @RequestMapping("/home")
    public String Home() {
        return "home";
    }

    // Return Plain Text
    @RequestMapping("/text")
    @ResponseBody
    public String welcomeText() {
        return "Welcome to my application! :)";
    }

    // return Plain Text with parameters
    @RequestMapping(value="/hello")
    @ResponseBody
    public String helloWorld(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

}
