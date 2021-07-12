package main.controllers;

import main.api.response.AuthCheckResponse;
import main.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    @GetMapping("/api/auth/captcha")
    public AuthCheckResponse getCapture() {

        return authService.getAuthCheckResponse();
    }



    //TODO @PostMapping("/api/auth/register") 11
    @PostMapping("/api/auth/register")
    public Map<String, Object> addNewUser(
            @RequestParam(name = "e_mail") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "captcha") String captcha,
            @RequestParam(name = "captcha_secret") String captchaSecret) {

        return authService.addNewUser(email, password, name, captcha, captchaSecret);
    }

}
