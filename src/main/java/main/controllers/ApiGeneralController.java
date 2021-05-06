package main.controllers;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    //для прочих запросов к API.

    private final InitResponse initResponse;
    private final SettingsResponse settingsResponse;

    public ApiGeneralController(InitResponse initResponse, SettingsResponse settingsResponse) {
        this.initResponse = initResponse;
        this.settingsResponse = settingsResponse;
    }

    @GetMapping("/init")
    private InitResponse init(){
        return this.initResponse;
    }

    @GetMapping("/settings")
    private Map<String, Boolean> settings(){
        return settingsResponse.getGlobalSettings();
    }

    //TODO GET /api/tag
    @GetMapping("/tag")
    public String apiTag()
    {
        return "@GetMapping(\"/api/tag\")";
    }

}
