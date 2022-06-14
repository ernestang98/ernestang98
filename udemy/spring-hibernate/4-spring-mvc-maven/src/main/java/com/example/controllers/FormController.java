package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FormController {
    // need a controller method to show the initial HTML form
    @RequestMapping("/form")
    public String Form() {
        return "form";
    }

    // need a controller method to process the HTML form
//    @RequestMapping("/submission")
//    public String Submission() {
//        return "submission";
//    }

    // need a controller method to process the HTML form 2
    @RequestMapping(value="/submission", method = RequestMethod.GET)
    public String letsShoutDude(HttpServletRequest request, Model model) {
        String theName = request.getParameter("studentName");
        if (theName != null) {
            theName = theName.toUpperCase();
            String result = "Yo! " + theName;
            model.addAttribute("message", result);
        }
        return "submission";
    }

    // need a controller method to process the HTML form 3
//    @RequestMapping(value="/submission", method = RequestMethod.GET)
//    public String letsShoutDude(@RequestParam("studentName") String theName,
//            Model model) {
//        if (theName != null) {
//            theName = theName.toUpperCase();
//            String result = "Yo! " + theName;
//            model.addAttribute("message", result);
//        }
//        return "submission";
//    }

}
