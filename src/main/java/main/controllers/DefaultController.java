package main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    //для остальных запросов не через API (главная страница - /, в частности)

    @RequestMapping(value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }

}
