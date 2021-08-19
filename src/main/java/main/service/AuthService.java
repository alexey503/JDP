package main.service;

import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.LoginResponse;
import main.api.response.PostDataResponse;
import main.api.response.UserLoginResponse;
import main.model.entities.User;
import main.model.repositories.PostsRepository;
import main.model.repositories.UserRepository;
import main.security.SecurityUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Value("${email.mailSubject}")
    private String subject;

    @Value("${email.siteAddress}")
    private String siteAddress;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SettingsService settingsService;



    public ResponseEntity<PostDataResponse> registration(RegisterRequest registerRequest){

        if (!settingsService.getSettingValue(SettingsService.KEY_MULTIUSER_MODE) ||
                registerRequest == null) {
            return ResponseEntity.notFound().build();
        }

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
            return ResponseEntity.ok(response);
        }

        main.model.entities.User newUser = new main.model.entities.User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setName(registerRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setReg_time(new Date().getTime()/1000);

        this.userRepository.save(newUser);

        response.setResult(true);

        return ResponseEntity.ok(response);
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
            LoginResponse loginResponse = getLoginResponse(getAuthUserEmail());
            return ResponseEntity.ok(loginResponse);
        }catch(UsernameNotFoundException ex){
            System.out.println("Пользователь не найден.");
        }catch (Exception e){
            System.out.println("Пользователь не авторизован.");
        }
        return ResponseEntity.ok(new LoginResponse());
    }

    public AuthCheckResponse logout() {
        SecurityContextHolder.clearContext();
        return new AuthCheckResponse(true);
    }

    public String getAuthUserEmail(){
        try {
            org.springframework.security.core.userdetails.User userDetailsUser =
                    (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetailsUser.getUsername();
        }catch (Exception e){
            return "";
        }
    }

    public User getAuthUser(){
        //return userRepository.findByEmail(getAuthUserEmail()).orElse(new User());
        return userRepository.findByEmail(getAuthUserEmail()).orElse(null);
    }

    private LoginResponse getLoginResponse(String email) throws UsernameNotFoundException{
        main.model.entities.User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());

        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setPhoto(currentUser.getPhoto());
        if(currentUser.getIsModerator() == 1){
            userLoginResponse.setModeration(true);
            userLoginResponse.setModerationCount(postsRepository.getModerationCount());
        }else{
            userLoginResponse.setModeration(false);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }

    public PostDataResponse restore(Map<String, String> requestMap) {

        if(requestMap == null){
            return new PostDataResponse();
        }
        String email = requestMap.get("email");
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            String code = RandomStringUtils.randomAlphanumeric(45);

            try {
                emailService.sendEmail(email, subject, "For restore password go to\n" +
                        siteAddress + "/login/change-password/" + code);

            } catch (Exception e) {
                System.out.println("Ошибка при отправке email с кодоом восстановления пароля:");
                System.out.println(e.toString());
                return new PostDataResponse();
            }

            //saving code to base
            User user = optionalUser.get();
            user.setCode(code);
            userRepository.save(user);

            return new PostDataResponse(true);
        }else{
            return new PostDataResponse();
        }

    }

    public PostDataResponse changePassword(ChangePasswordRequest changePasswordRequest) {

        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByCode(changePasswordRequest.getCode()).orElse(null);

        if(user == null){
            errors.put(PostDataResponse.ERR_TYPE_CODE,
                    PostDataResponse.ERROR_ARH_CODE);
        }

        if (changePasswordRequest.getPassword().length() < 6) {
            errors.put(PostDataResponse.ERR_TYPE_PASSWORD,
                    PostDataResponse.ERROR_SHORT_PASSWORD);
        }

        if (!captchaService.isCaptchaValid(changePasswordRequest.getCaptcha(), changePasswordRequest.getCaptchaSecretCode())) {
            errors.put(PostDataResponse.ERR_TYPE_CAPTCHA,
                    PostDataResponse.ERROR_WRONG_CAPTURE);
        }

        if (errors.size() > 0) {
            PostDataResponse postDataResponse = new PostDataResponse();
            postDataResponse.setErrors(errors);
            return postDataResponse;
        }

        user.setCode("");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));

        this.userRepository.save(user);

        return new PostDataResponse(true);
    }
}
