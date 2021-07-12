package main.service;

import main.api.response.AuthCheckResponse;
import main.model.Captcha;
import main.model.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private CaptchaRepository captchaRepository;

    public AuthCheckResponse getAuthCheckResponse() {
        return new AuthCheckResponse();
    }

    public Map<String, Object> addNewUser(String email, String password, String name, String captcha, String captchaSecret) {

        Map<String, String> errors = new HashMap<>();
        if(this.isEmailExist(email)){
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if(!this.isNameValid(name)){
            errors.put("name", "Имя указано неверно");
        }
        if(!this.isPasswordValid(password)){
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if(!this.isCaptchaValid(captcha, captchaSecret)){
            errors.put("captcha", "Код с картинки введён неверно");
        }

        Map <String, Object> resultMap = new HashMap<>();
        if(errors.size() > 0){
            resultMap.put("result", false);
            resultMap.put("errors", errors);
            return resultMap;
        }

        if(this.addUserToBase(email, name, password)) {
            resultMap.put("result", true);
            return resultMap;
        }else{
            errors.put("error", "Ошибка создания пользователя в базе");
            resultMap.put("result", false);
            resultMap.put("errors", errors);
            return resultMap;
        }
    }

    private boolean addUserToBase(String email, String name, String password) {
        //TODO add user into base
        return false;
    }

    private boolean isCaptchaValid(String captcha, String captchaSecret) {
        Optional<Captcha> optionalCaptcha = this.captchaRepository.findByCode(captcha);
        if(optionalCaptcha.isEmpty()){
            return false;
        }

        return optionalCaptcha.get().getSecretCode().equals(captchaSecret);

    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean isNameValid(String name) {
        Matcher matcher = Pattern.compile("^[a-zA-Zа-яА-Я_]{2,}$").matcher(name);
        return matcher.find();
    }

    private boolean isEmailExist(String email) {
        //TODO check in base
        return true;
    }

}
