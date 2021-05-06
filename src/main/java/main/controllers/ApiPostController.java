package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {
    //обрабатывает все запросы /api/post/*

    //TODO GET /api/post
    @GetMapping("/api/post")
    public String apiPost()
    {
        return "@GetMapping(\"/api/post\")";
    }
}
