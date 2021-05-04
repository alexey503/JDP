package main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {
    //для прочих запросов к API.

    @GetMapping("/api/init")
    public String apiInit()
    {
        return "@GetMapping(\"/api/init\")";
    }

    @GetMapping("/api/settings")
    public String apiSettings()
    {
        return "@GetMapping(\"/api/settings\")";
    }

    @GetMapping("/api/tag")
    public String apiTag()
    {
        return "@GetMapping(\"/api/tag\")";
    }

}
