package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    //для обычных запросов не через API (главная страница - /, в частности)

    @GetMapping("/a")
    public String index()
    {
        return "/a";
    }
}
