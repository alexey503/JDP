package main.service;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.LoginResponse;
import main.api.response.PostDataResponse;
import main.api.response.UserLoginResponse;
import main.model.entities.User;
import main.model.repositories.UserRepository;
import main.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CaptchaService captchaService;

    public PostDataResponse registration(RegisterRequest registerRequest){

        Map<String, String> errors = new HashMap<>();

        Optional<User> userEntity = userRepository.findByEmail(registerRequest.getEmail());
        if (userEntity.isPresent()) {
            errors.put(PostDataResponse.ERR_TYPE_EMAIL,
                    PostDataResponse.ERROR_EMAIL_ENGAGED);
        }

        Matcher matcher = Pattern.compile("^[a-zA-Zа-яА-Я0-9_]{2,}$").matcher(registerRequest.getName());
        if (!matcher.find()) {
            errors.put(PostDataResponse.ERR_TYPE_NAME,
                    PostDataResponse.ERROR_WRONG_NAME);
        }

        if (registerRequest.getPassword().length() < 6) {
            errors.put(PostDataResponse.ERR_TYPE_PASSWORD,
                    PostDataResponse.ERROR_SHORT_PASSWORD);
        }

        if (!captchaService.isCaptchaValid(registerRequest.getCaptcha(), registerRequest.getCaptchaSecretCode())) {
            errors.put(PostDataResponse.ERR_TYPE_CAPTCHA,
                    PostDataResponse.ERROR_WRONG_CAPTURE);
        }

        PostDataResponse response = new PostDataResponse();

        if (errors.size() > 0) {
            response.setResult(false);
            response.setErrors(errors);
            return response;
        }

        main.model.entities.User newUser = new main.model.entities.User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setName(registerRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setReg_time(new Date().getTime()/1000);

        this.userRepository.save(newUser);

        response.setResult(true);

        return response;
    }

    public LoginResponse login(LoginRequest loginRequest){

        Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        ));
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return getLoginResponse(user.getUsername());
    }

    public ResponseEntity<LoginResponse> check() {

        try {
            SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LoginResponse loginResponse = getLoginResponse(securityUser.getUsername());
            return ResponseEntity.ok(loginResponse);
        }catch(UsernameNotFoundException ex){
            System.out.println("Пользователь не найден.");
        }catch (Exception e){
            System.out.println("Ошибка идентификации пользователя.");
        }
        return ResponseEntity.ok(new LoginResponse());
    }

    public AuthCheckResponse logout() {
        SecurityContextHolder.clearContext();
        return new AuthCheckResponse(true);
    }

    public String getAuthUserName(){
        org.springframework.security.core.userdetails.User userDetailsUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsUser.getUsername();
    }

    private LoginResponse getLoginResponse(String email) throws UsernameNotFoundException{
        main.model.entities.User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setModeration(currentUser.getIsModerator() == 1);
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setPhoto(currentUser.getPhoto());


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }
}
