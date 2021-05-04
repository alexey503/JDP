package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    //для обычных запросов не через API (главная страница - /, в частности)

    @GetMapping("/")
    public String index()
    {
        return "/";
    }

    @GetMapping("/a")
    public String indexA()
    {
        return "/a";
    }
}
