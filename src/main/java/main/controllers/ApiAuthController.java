package main.controllers;

import main.api.response.AuthCheckResponse;
import main.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/auth/check")
    public AuthCheckResponse authCheck() {
        return authService.getAuthCheckResponse();
    }

    //TODO @GetMapping("/api/auth/captcha) page 10

    //TODO @PostMapping("/api/auth/register") 11
}
