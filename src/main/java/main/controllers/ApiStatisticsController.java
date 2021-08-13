package main.controllers;

import main.api.response.MyStatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/statistics")
public class ApiStatisticsController {

    //TODO моя статистика Api page 23
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<MyStatisticsResponse> getMyStat(Principal principal) {

        return ResponseEntity.ok(new MyStatisticsResponse());
    }

    //TODO статистика по всему блогу Api page 23
    @GetMapping("/all")
    public ResponseEntity<MyStatisticsResponse> getStatAll() {

        return ResponseEntity.ok(new MyStatisticsResponse());
    }
}
