package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.model.entities.Captcha;
import main.model.repositories.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class CaptchaService {

    @Value("${blogNew.killCaptureTimeout}")
    private long killCaptureTimeout;

    @Autowired
    private CaptchaRepository captchaRepository;

    public CaptchaResponse getCaptchaResponse() {

        this.captchaRepository.deleteByTimeBefore(new Date().getTime() - this.killCaptureTimeout);

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
        captchaEntity.setTime(new Date().getTime());

        captchaRepository.save(captchaEntity);

        CaptchaResponse response = new CaptchaResponse();
        response.setSecret(secreteCodeKey);
        response.setImage(imageText);

        return response;
    }

    private String generateSecretCode(int length) {

        Random random = new Random();
        String secretCode = "";
        int index;

        for (int i = 0; i < length; i++) {
            index = random.nextInt('z' - 'a') + 'a';
            secretCode += Character.toString(index);
        }
        return secretCode;
    }

    public boolean isCaptchaValid(String captcha, String captchaSecret) {
        Optional<Captcha> optionalCaptcha = this.captchaRepository.findBySecretCode(captchaSecret);
        if (optionalCaptcha.isEmpty()) {
            return false;
        }
        return optionalCaptcha.get().getCode().equals(captcha);
    }
}
