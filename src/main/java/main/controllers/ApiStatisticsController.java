package main.controllers;

import main.api.response.StatisticsResponse;
import main.service.AuthService;
import main.service.SettingsService;
import main.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private SettingsService settingsService;



    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticsResponse> getMyStat() {
        return ResponseEntity.ok(statisticsService.getMyStatistics(authService.getAuthUser().getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<StatisticsResponse> getStatAll() {

        if((settingsService.getGlobalSettings().containsKey(SettingsService.KEY_STATISTICS_IS_PUBLIC) &&
                settingsService.getGlobalSettings().get(SettingsService.KEY_STATISTICS_IS_PUBLIC)) ||
                (authService.getAuthUser() != null && authService.getAuthUser().getIsModerator() == 1)){
            return ResponseEntity.ok(statisticsService.getAllStatistics());
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
