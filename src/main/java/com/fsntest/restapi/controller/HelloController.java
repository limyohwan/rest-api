package com.fsntest.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping(value = "/helloworld")
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }
}
