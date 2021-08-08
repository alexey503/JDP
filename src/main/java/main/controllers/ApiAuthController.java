package main.controllers;


import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.*;
import main.model.repositories.UserRepository;
import main.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiAuthController(CaptchaService captchaService, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.captchaService = captchaService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse>check(Principal principal){
        if(principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }

        return ResponseEntity.ok(getLoginResponse(principal.getName()));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCapture() {

        return ResponseEntity.ok(captchaService.getCaptchaResponse());

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){

        Map<String, String> errors = new HashMap<>();

        Optional<main.model.entities.User> userEntity = userRepository.findByEmail(registerRequest.getEmail());
        if (userEntity.isPresent()) {
            errors.put(RegisterResponse.ERR_TYPE_EMAIL,
                    RegisterResponse.ERROR_EMAIL_ENGAGED);
        }

        Matcher matcher = Pattern.compile("^[a-zA-Zа-яА-Я_]{2,}$").matcher(registerRequest.getName());
        if (!matcher.find()) {
            errors.put(RegisterResponse.ERR_TYPE_NAME,
                    RegisterResponse.ERROR_WRONG_NAME);
        }

        if (registerRequest.getPassword().length() < 6) {
            errors.put(RegisterResponse.ERR_TYPE_PASSWORD,
                    RegisterResponse.ERROR_SHORT_PASSWORD);
        }

        if (!captchaService.isCaptchaValid(registerRequest.getCaptcha(), registerRequest.getCaptchaSecretCode())) {
            errors.put(RegisterResponse.ERR_TYPE_CAPTCHA,
                    RegisterResponse.ERROR_WRONG_CAPTURE);
        }

        RegisterResponse response = new RegisterResponse();

        if (errors.size() > 0) {
            response.setResult(false);
            response.setErrors(errors);
            return ResponseEntity.ok(response);
        }

        main.model.entities.User newUser = new main.model.entities.User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setName(registerRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setReg_time(new Date().getTime());

        this.userRepository.save(newUser);

        response.setResult(true);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        ));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(getLoginResponse(user.getUsername()));
    }

    private LoginResponse getLoginResponse(String email) {
        main.model.entities.User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setModeration(currentUser.getIsModerator() == 1);
        userLoginResponse.setId(currentUser.getId());


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }


    @GetMapping("/logout")
    public ResponseEntity<AuthCheckResponse> logout(Principal principal) {

        SecurityContextHolder.clearContext();

        AuthCheckResponse response = new AuthCheckResponse();
        response.setResult(true);
        return ResponseEntity.ok(response);
    }

}
