package com.example.controllers;

import com.example.models.*;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;

import javax.validation.Valid;

@Controller
@RequestMapping("/validation")
public class FormValidationController {

    private static final Logger LOGGER = Logger.getLogger(FormValidationController.class);

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @RequestMapping("/form4")
    public String showForm(Model theModel) {
        StudentValidationModel student = new StudentValidationModel();
        theModel.addAttribute("student", student);
        return "form4";
    }

    @RequestMapping("/submission4")
    public String processForm(
            @Valid @ModelAttribute("student") StudentValidationModel student,
            BindingResult theBindingResult) {

        if (theBindingResult.hasErrors()) {
            return "form4";
        }
        else {
            LOGGER.info("printHello started.");
            return "submission4";
        }
    }

}
