package main.controllers;

import main.api.response.CalendarDto;
import main.api.response.InitResponse;
import main.api.response.TagResponse;
import main.service.CalendarService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = {"application/json; charset=UTF-8"})
public class ApiGeneralController {

    @Autowired
    private InitResponse initResponse;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private CalendarService calendarService;


    @GetMapping(value = "/init", produces = {"application/json; charset=UTF-8"})
    private InitResponse init() {
        return this.initResponse;
    }

    @GetMapping("/settings")
    private Map<String, Boolean> settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    public Map<String, List<TagResponse>> tags(@RequestParam(name = "query", required = false) String tagRequest) {
        Map<String, List<TagResponse>> response = new HashMap<>();
        response.put("tags", tagsService.getTags(tagRequest != null ? tagRequest : ""));
        return response;
    }

    @GetMapping("/calendar")
    public CalendarDto calendar(@RequestParam(name = "year", required = false) String yearRequest) {
        if (yearRequest == null) {
            yearRequest = String.valueOf(LocalDate.now().getYear());
        }
        return calendarService.getCalendarDto(yearRequest);
    }
}
