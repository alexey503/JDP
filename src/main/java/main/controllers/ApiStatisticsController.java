package main.controllers;

import main.api.response.StatisticsResponse;
import main.service.AuthService;
import main.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class ApiStatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private AuthService authService;

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticsResponse> getMyStat() {
        return ResponseEntity.ok(statisticsService.getMyStatistics(authService.getAuthUser().getId()));
    }

    //TODO статистика по всему блогу Api page 23
    @GetMapping("/all")
    public ResponseEntity<StatisticsResponse> getStatAll() {

        return ResponseEntity.ok(new StatisticsResponse());
    }
}
