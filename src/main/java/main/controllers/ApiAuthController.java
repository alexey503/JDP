package main.controllers;


import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.PostDataResponse;
import main.service.AuthService;
import main.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CaptchaService captchaService;


    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check() {
        return authService.check();
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCapture() {
        return ResponseEntity.ok(captchaService.getCaptchaResponse());
    }

    @PostMapping("/register")
    public ResponseEntity<PostDataResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registration(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<AuthCheckResponse> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    //TODO изменение пароля Api page 22
    @PostMapping("/password")
    public ResponseEntity<PostDataResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(new PostDataResponse());
    }

    //TODO восстановление пароля Api page 21
    @PostMapping("/restore")
    public ResponseEntity<PostDataResponse> passwordRestore(String email) {
        return ResponseEntity.ok(new PostDataResponse());
    }
}
