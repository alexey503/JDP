package main.service;

import main.api.response.PostDataResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Service
public class LoadImageService {

    public static final int MAX_UPLOAD_SIZE = 1024*1024*10;

    public PostDataResponse uploadImage(MultiPartFile image) {
        PostDataResponse response = new PostDataResponse();
        Map<String, String> errors = new HashMap<>();

        if (image.getSize() > MAX_UPLOAD_SIZE) {
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_OVER_SIZE);
        }

        Pattern formatImagePattern = Pattern.compile("^(.*)(.)(png|jpe?g)$");
        if(!formatImagePattern.matcher(image.getOriginalFilename()).matches()){
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_WRONG_FORMAT);
        }

        if(errors.isEmpty()){
            //TODO Saving file to disc
            try (InputStream inputStream = image.getInputStream()) {
                StringBuffer localFileName = new StringBuffer("/upload");
                for (int i = 0; i < 3; i++) {
                    localFileName.append("/")
                            .append(RandomStringUtils.randomAlphabetic(2));
                }
                Files.createDirectories(localFileName);

                localFileName.append(RandomStringUtils.randomNumeric(5)).append(".jpg");

                Files.copy(inputStream, Paths.get(localFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            response.setResult(true);
            response.setResultDataString(localFileName);
        }else{
            response.setErrors(errors);
        }
        return response;

    }
}
