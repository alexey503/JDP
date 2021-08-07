package main.controllers;


import main.api.request.LoginRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.LoginResponse;
import main.api.response.UserLoginResponse;
import main.model.repositories.UserRepository;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    @Autowired
    public ApiAuthController(AuthService authService, AuthenticationManager authenticationManager, UserRepository userRepository) {

        this.authService = authService;

        this.authenticationManager = authenticationManager;

        this.userRepository = userRepository;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse>check(Principal principal){
        if(principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }

        return ResponseEntity.ok(getLoginResponse(principal.getName()));
    }

    @GetMapping("/captcha")
    public Map<String, String> getCapture() {

        return authService.getCapture();

    }

    @PostMapping("/register")
    public Map<String, Object> addNewUser(
            @RequestParam(name = "e_mail") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "captcha") String captcha,
            @RequestParam(name = "captcha_secret") String captchaSecret) {


        return authService.addNewUser(email, password, name, captcha, captchaSecret);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Login: " + loginRequest.getEmail() + " " + loginRequest.getPassword());
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

        System.out.println("Logout: " + principal.getName());
        SecurityContextHolder.clearContext();

        AuthCheckResponse response = new AuthCheckResponse();
        response.setResult(true);
        return ResponseEntity.ok(response);
    }

}
