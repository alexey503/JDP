package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {
    //обрабатывает все запросы /api/auth/*

    //TODO GET /api/auth/check

    @GetMapping("/api/auth/check")
    public String authCheck()
    {
        return "@GetMapping(\"/api/auth/check\")";
    }
}
