package main.service;

import main.api.response.PostDataResponse;
import main.model.entities.User;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    public static final String PROFILE_PARAM_NAME = "name";
    public static final String PROFILE_PARAM_EMAIL = "email";
    public static final String PROFILE_PARAM_PASSWORD = "password";
    public static final String PROFILE_PARAM_PHOTO = "photo";
    public static final String PROFILE_PARAM_REMOVE_PHOTO = "removePhoto";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PostDataResponse editProfile(Map<String, Object> editParams, String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        Map<String, String> errors = new HashMap<>();
        PostDataResponse response = new PostDataResponse();

        if(userOptional.isEmpty()){
            errors.put("email", "Ошибка авторизации. Email отсутствует в базе. Изменение профиля невозможно.");
            response.setErrors(errors);
            return response;
        }
        User userProfile = userOptional.get();

        System.out.println(userProfile.getEmail());
        System.out.println(editParams.size());
        for (Map.Entry<String, Object> entry : editParams.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue());
            String value;
            switch(entry.getKey()){
                case PROFILE_PARAM_NAME:
                    value = (String) entry.getValue();
                    Matcher matcher = Pattern.compile("^[a-zA-Zа-яА-Я_]{2,}$").matcher(value);
                    if (!matcher.find()) {
                        errors.put(PostDataResponse.ERR_TYPE_NAME,
                                PostDataResponse.ERROR_WRONG_NAME);
                    }else{
                        userProfile.setName(value);
                    }
                    break;
                case PROFILE_PARAM_EMAIL:
                    value = (String) entry.getValue();
                    if(!userEmail.equals(value) && userRepository.findByEmail(value).isPresent()){
                        errors.put(PostDataResponse.ERR_TYPE_EMAIL,
                                PostDataResponse.ERROR_EMAIL_ENGAGED);
                    }else{
                        userProfile.setEmail(value);
                    }
                    break;
                case PROFILE_PARAM_PASSWORD:
                    value = (String) entry.getValue();
                    if (value.length() < 6) {
                        errors.put(PostDataResponse.ERR_TYPE_PASSWORD,
                                PostDataResponse.ERROR_SHORT_PASSWORD);
                    }else{
                        userProfile.setPassword(passwordEncoder.encode(value));
                    }

                    break;
                case PROFILE_PARAM_PHOTO:
                    //TODO ParsePhoto
                    System.out.println(entry.getValue().toString());
                    break;
                case PROFILE_PARAM_REMOVE_PHOTO:
                    if((Integer) entry.getValue() == 1){
                        userProfile.setPhoto("");
                    }
                    break;
            }
        }
        if(errors.isEmpty()){
            userRepository.save(userProfile);
            response.setResult(true);
        }else{
            response.setErrors(errors);
        }
        return response;
    }

}
