package com.example.controllers;

import com.example.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class FormStudentController {

    @RequestMapping("/form3")
    public String showForm(Model theModel) {
        StudentModel student = new StudentModel();
        theModel.addAttribute("student", student);
        return "form3";
    }

    @RequestMapping("/submission3")
    public String processForm(@ModelAttribute("student") StudentModel theStudent) {
        return "submission3";
    }

}
