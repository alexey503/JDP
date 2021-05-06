package main.controllers;

import main.api.response.InitResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    //для прочих запросов к API.

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagsService tagsService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagsService tagsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagsService = tagsService;
    }

    @GetMapping("/init")
    private InitResponse init(){
        return this.initResponse;
    }

    @GetMapping("/settings")
    private Map<String, Boolean> settings(){
        return settingsService.getGlobalSettings();
    }

    //TODO GET /api/tag
    @GetMapping("/tag")
    public List<TagResponse> tags()
    {
        return tagsService.getTags();
    }

}
