package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.AuthCheckResponse;
import main.model.Captcha;
import main.model.CaptchaRepository;
import main.model.UserEntity;
import main.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Value("${BlogEngine.killCaptureTimeout}")
    private long killCaptureTimeout;

    @Autowired
    private CaptchaRepository captchaRepository;
    @Autowired
    private UserRepository userRepository;

    public AuthCheckResponse getAuthCheckResponse() {
        return new AuthCheckResponse();
    }

    public Map<String, Object> addNewUser(String email, String password, String name, String captcha, String captchaSecret) {

        Map<String, String> errors = new HashMap<>();
        if (this.isEmailExist(email)) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if (!this.isNameValid(name)) {
            errors.put("name", "Имя указано неверно");
        }
        if (!this.isPasswordValid(password)) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!this.isCaptchaValid(captcha, captchaSecret)) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        Map<String, Object> resultMap = new HashMap<>();
        if (errors.size() > 0) {
            resultMap.put("result", false);
            resultMap.put("errors", errors);
            return resultMap;
        }

        if (this.addUserToBase(email, name, password)) {
            resultMap.put("result", true);
            return resultMap;
        } else {
            errors.put("error", "Ошибка создания пользователя в базе");
            resultMap.put("result", false);
            resultMap.put("errors", errors);
            return resultMap;
        }
    }

    private boolean addUserToBase(String email, String name, String password) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setReg_time(new Date());

        this.userRepository.save(newUser);
        return true;
    }

    private boolean isCaptchaValid(String captcha, String captchaSecret) {
        Optional<Captcha> optionalCaptcha = this.captchaRepository.findByCode(captcha);
        if (optionalCaptcha.isEmpty()) {
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
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        System.out.println(userEntity.isPresent());
        return userEntity.isPresent();
    }

    public Map<String, String> getCapture() {

        this.captchaRepository.deleteByTimeBefore(new Date(new Date().getTime() - this.killCaptureTimeout));

        Cage cage = new GCage();

        String text = cage.getTokenGenerator().next();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            cage.draw(text, byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageText = "data:image/png;base64, " + Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        String secreteCodeKey = this.generateSecretCode(20);

        Captcha captchaEntity = new Captcha();
        captchaEntity.setSecretCode(secreteCodeKey);
        captchaEntity.setCode(text);
        captchaEntity.setTime(new Date());

        captchaRepository.save(captchaEntity);

        Map<String, String> map = new HashMap<>();
        map.put("secret", secreteCodeKey);
        map.put("image", imageText);

        return map;
    }

    private String generateSecretCode(int length) {

        Random random = new Random();
        String secretCode = "";
        int index = 0;

        for (int i = 0; i < length; i++) {
            index = random.nextInt('z' - 'a') + 'a';
            secretCode += Character.toString(index);
        }
        return secretCode;
    }
}
