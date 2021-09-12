package main.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import main.api.response.PostDataResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class LoadImageService {

    private Cloudinary cloudinary;

    public static final int MAX_UPLOAD_SIZE = 1024 * 1024 * 10;

    public LoadImageService() {

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcfikdwuh",
                "api_key", "912693425934635",
                "api_secret", "m5nPPy3BGbPUSgVNHhbuzGuRNDA"));
    }

    public PostDataResponse uploadImage(MultipartFile image) {
        PostDataResponse response = new PostDataResponse();
        Map<String, String> errors = new HashMap<>();

        if (image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE);
            response.setErrors(errors);
            return response;
        }

        if (image.getSize() > MAX_UPLOAD_SIZE) {
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_OVER_SIZE);
        }

        Pattern formatImagePattern = Pattern.compile("^(.*)(.)(png|jpe?g)$");
        if (!formatImagePattern.matcher(image.getOriginalFilename()).matches()) {
            errors.put(PostDataResponse.ERR_TYPE_IMAGE, PostDataResponse.ERROR_UPLOAD_FILE_WRONG_FORMAT);
        }

        if (errors.isEmpty()) {
            String localFileName = "";
            try (InputStream inputStream = image.getInputStream()) {

                StringBuilder filePathBuilder = new StringBuilder("upload/");
                for (int i = 0; i < 3; i++) {
                    filePathBuilder.append(RandomStringUtils.randomAlphabetic(2)).append("/");
                }
                Path filePath = Paths.get(filePathBuilder.toString());
                Files.createDirectories(filePath);

                filePathBuilder.append(RandomStringUtils.randomNumeric(5));

                localFileName = filePathBuilder.toString();

                Files.copy(inputStream, Paths.get(localFileName),
                        StandardCopyOption.REPLACE_EXISTING);

                Map uploadResultMap = cloudinary.uploader().upload(new File(localFileName),
                        ObjectUtils.asMap("public_id", localFileName));

                response.setResult(true);
                response.setResultDataString(uploadResultMap.get("url").toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setErrors(errors);
        }
        return response;
    }

    public String saveAndResizeImage(InputStream inputStream) {

        String localFileName = "";
        try {
            StringBuilder filePathBuilder = new StringBuilder("upload/");
            for (int i = 0; i < 3; i++) {
                filePathBuilder.append(RandomStringUtils.randomAlphabetic(2)).append("/");
            }
            Path filePath = Paths.get(filePathBuilder.toString());
            Files.createDirectories(filePath);

            filePathBuilder.append(RandomStringUtils.randomNumeric(5));

            localFileName = filePathBuilder.toString();

            Files.copy(inputStream, Paths.get(localFileName),
                    StandardCopyOption.REPLACE_EXISTING);

            Map uploadResultMap = cloudinary.uploader().upload(new File(localFileName),
                    ObjectUtils.asMap("public_id", localFileName,
                            "transformation",
                            new Transformation()
                                    .gravity("face").crop("crop").chain()
                                    .radius("max").chain()
                                    .width(36).height(36).crop("scale")));

            return uploadResultMap.get("url").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Save awatar error";
    }
}
