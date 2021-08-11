package main.service;

import main.api.response.PostDataResponse;
import main.model.entities.User;
import main.model.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    private static final long MAX_PHOTO_SIZE = 1024 * 1024 * 5;
    private static final String AVATAR_DIRECTORY = "/target/classes/img";
            //"/resources/img";
    private static final int AVATAR_WIDTH = 35;
    private static final int AVATAR_HEIGHT = 35;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private StorageService storageService;


    @Autowired
    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PostDataResponse updateProfile(MultipartFile photo, Byte removePhoto, String name, String newEmail, String password, String userEmail) {

        PostDataResponse response = new PostDataResponse();
        Map<String, String> errors = new HashMap<>();

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            errors.put("email", "Ошибка авторизации. Email отсутствует в базе. Изменение профиля невозможно.");
            response.setErrors(errors);
            return response;
        }

        User userProfile = userOptional.get();

        System.out.println("New Name: " + name);
        if (name != null) {
            if (isNameValid(name)) {
                userProfile.setName(name);
            } else {
                errors.put(PostDataResponse.ERR_TYPE_NAME, PostDataResponse.ERROR_WRONG_NAME);
            }
        }
        if (newEmail != null && !userEmail.equals(newEmail)){
            if (userRepository.findByEmail(newEmail).isEmpty()) {
                userProfile.setEmail(newEmail);
                SecurityContextHolder.clearContext();
            } else {
                errors.put(PostDataResponse.ERR_TYPE_EMAIL,
                        PostDataResponse.ERROR_EMAIL_ENGAGED);
            }
        }
        if (password != null){
            if (password.length() >= 6) {
                userProfile.setPassword(passwordEncoder.encode(password));
            }else {
                errors.put(PostDataResponse.ERR_TYPE_PASSWORD,
                        PostDataResponse.ERROR_SHORT_PASSWORD);
            }
        }
        if(removePhoto != null && removePhoto == 1){
            try {
                if(userProfile.getPhoto() != null) {
                    Files.deleteIfExists(Paths.get(userProfile.getPhoto()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            userProfile.setPhoto("");
        }

        if(photo != null && !photo.isEmpty()) {
            if(photo.getSize() > MAX_PHOTO_SIZE){
                errors.put(PostDataResponse.ERR_TYPE_PHOTO, PostDataResponse.ERROR_OVER_SIZE);
            }else {
                userProfile.setPhoto(savePhoto(photo));
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

    private String savePhoto(MultipartFile photo) {

        File avaDir = new File(System.getProperty("user.dir") + AVATAR_DIRECTORY);
        if (!avaDir.exists()){
            avaDir.mkdirs();
        }

        String newFileName = RandomStringUtils.randomAlphanumeric(10) + ".jpg";
        File outputFile = new File(System.getProperty("user.dir") + AVATAR_DIRECTORY, newFileName);
        try {
            ImageIO.write(getReducedImage(photo.getInputStream()), "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("outputFile.getAbsolutePath(): " + outputFile.getAbsolutePath());
        System.out.println("outputFile.getName(): " + outputFile.getName());
        System.out.println("outputFile.getParent(): " + outputFile.getParent());
        return "img/" + outputFile.getName();
    }

    private boolean isNameValid(String name) {
        Matcher matcher = Pattern.compile("^[a-zA-Zа-яА-Я_][a-zA-Zа-яА-Я0-9_]+").matcher(name);
        return matcher.find();
    }

    private BufferedImage getReducedImage(InputStream inputStream) throws IOException {
        Image scaledImage = ImageIO.read(inputStream)
                .getScaledInstance(AVATAR_WIDTH, AVATAR_HEIGHT, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(AVATAR_WIDTH, AVATAR_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        return bwImg;
    }

}
