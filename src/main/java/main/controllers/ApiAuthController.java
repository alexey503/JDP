package main.controllers;


import main.api.response.LoginResponse;
import main.model.repositories.UserRepository;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
public class ApiAuthController {

    private final AuthService authService;

    //private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    @Autowired
    public ApiAuthController(AuthService authService, /*AuthenticationManager authenticationManager, */UserRepository userRepository) {

        this.authService = authService;

        //this.authenticationManager = authenticationManager;

        this.userRepository = userRepository;
    }
/*
    @GetMapping("/api/auth/check")
    public ResponseEntity<LoginResponse>check(Principal principal){
        if(principal == null){
            return ResponseEntity.ok(new LoginResponse());
        }

        //return ResponseEntity.ok(this.getLoginResponse(principal.getName()));
        return ResponseEntity.ok(new LoginResponse());
    }
*/
    @GetMapping("/api/auth/captcha")
    public Map<String, String> getCapture() {

        return authService.getCapture();

    }

    @PostMapping("/api/auth/register")
    public Map<String, Object> addNewUser(
            @RequestParam(name = "e_mail") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "captcha") String captcha,
            @RequestParam(name = "captcha_secret") String captchaSecret) {


        return authService.addNewUser(email, password, name, captcha, captchaSecret);

    }
/*
    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Login: " + loginRequest.getEmail() + " " + loginRequest.getPassword());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        System.out.println("Token name: " + usernamePasswordAuthenticationToken.getName());

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok(this.getLoginResponse(user.getUsername()));
    }

    private LoginResponse getLoginResponse(String email){
        main.model.entities.User currentUser = userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setModeration(currentUser.getIsModerator() == 1 );
        userLoginResponse.setId(currentUser.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }


 */
}
