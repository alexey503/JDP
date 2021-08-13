package main.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
public class PostDataResponse {

    public static final String ERR_TYPE_EMAIL = "email";
    public static final String ERR_TYPE_NAME = "name";
    public static final String ERR_TYPE_PASSWORD = "password";
    public static final String ERR_TYPE_CAPTCHA = "captcha";
    public static final String ERR_TYPE_TITLE = "title";
    public static final String ERR_TYPE_TEXT = "text";
    public static final String ERR_TYPE_PHOTO = "photo";
    public static final String ERR_TYPE_IMAGE = "image";

    public static final String ERROR_EMAIL_ENGAGED = "Этот e-mail уже зарегистрирован";
    public static final String ERROR_WRONG_NAME = "Имя указано неверно";
    public static final String ERROR_SHORT_PASSWORD = "Пароль короче 6-ти символов";
    public static final String ERROR_WRONG_CAPTURE = "Код с картинки введён неверно";

    public static final String ERROR_SHORT_TITLE = "Заголовок должен содержать 3 и более символа";
    public static final String ERROR_SHORT_TEXT = "Текст публикации должен быть более 50 символов";

    public static final String ERROR_AVATAR_OVER_SIZE = "Фото слишком большое, нужно не более 5 Мб";
    public static final String ERROR_UPLOAD_FILE_OVER_SIZE = "Размер файла превышает допустимый размер";
    public static final String ERROR_UPLOAD_FILE_WRONG_FORMAT = "Формат изображения должен быть jpg или png";

    public static final String ERROR_UPLOAD_FILE = "Ошибка при загрузке картинки";


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    @JsonIgnore
    private String resultDataString;

    public PostDataResponse() {
        result = false;
    }

    public PostDataResponse(Boolean result) {
        this.result = result;
    }
}
