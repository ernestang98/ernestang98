package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class FormApiController {

    @RequestMapping("/form")
    public String form() {
        return "form";
    }

    @RequestMapping(value="/submission", method = RequestMethod.GET)
    public String submission(HttpServletRequest request, Model model) {
        String theName = request.getParameter("studentName");
        if (theName != null) {
            theName = theName.toUpperCase();
            String result = "Yo! " + theName;
            model.addAttribute("message", result);
        }
        return "submission";
    }

}
