package main.service;

import main.api.response.PostDataResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

@Service
public class LoadImageService {

    public static final int MAX_UPLOAD_SIZE = 1024*1024*10;

    public PostDataResponse uploadImage(MultipartFile image) {
        PostDataResponse response = new PostDataResponse();
        Map<String, String> errors = new HashMap<>();

        if(image == null || image.isEmpty() || image.getOriginalFilename() == null){
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE);
            response.setErrors(errors);
            return response;
        }

        if (image.getSize() > MAX_UPLOAD_SIZE) {
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_OVER_SIZE);
        }

        Pattern formatImagePattern = Pattern.compile("^(.*)(.)(png|jpe?g)$");
        if(!formatImagePattern.matcher(image.getOriginalFilename()).matches()){
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_WRONG_FORMAT);
        }

        if(errors.isEmpty()){
            String localFileName = "";
            try (InputStream inputStream = image.getInputStream()) {

                StringBuilder filePathBuilder = new StringBuilder("upload/");
                for (int i = 0; i < 3; i++) {
                    filePathBuilder.append(RandomStringUtils.randomAlphabetic(2)).append("/");
                }
                Path filePath = Paths.get(filePathBuilder.toString());
                Files.createDirectories(filePath);

                filePathBuilder.append(RandomStringUtils.randomNumeric(5)).append(".jpg");

                localFileName = filePathBuilder.toString();

                Files.copy(inputStream, Paths.get(localFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            response.setResult(true);
            response.setResultDataString(localFileName);
        }else{
            response.setErrors(errors);
        }
        return response;
    }

    public MultipartFile getMultipartImageFile(String fileName) {
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("File not found: " + fileName);
        }else {
            System.out.println("File exists " + fileName);
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                //MultipartFile multipartFile = new MockMultipartFile(fileName, is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
