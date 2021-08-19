package main.controllers;

import main.api.response.CalendarDto;
import main.api.response.InitResponse;
import main.api.response.PostDataResponse;
import main.api.response.TagResponse;
import main.service.CalendarService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public InitResponse init() {
        return this.initResponse;
    }

    @GetMapping("/settings")
    public Map<String, Boolean> settings() {
        return settingsService.getGlobalSettings();
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostDataResponse> putSettings(@RequestBody Map<String, Boolean> paramsMap) {
        return ResponseEntity.ok().body(settingsService.saveGlobalSettings(paramsMap));
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
