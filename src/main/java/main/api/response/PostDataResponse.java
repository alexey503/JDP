package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
public class PostDataResponse {

    public static final String ERR_TYPE_EMAIL = "email";
    public static final String ERR_TYPE_NAME = "name";
    public static final String ERR_TYPE_PASSWORD = "password";
    public static final String ERR_TYPE_CAPTCHA = "captcha";

    public static final String ERROR_EMAIL_ENGAGED = "Этот e-mail уже зарегистрирован";
    public static final String ERROR_WRONG_NAME = "Имя указано неверно";
    public static final String ERROR_SHORT_PASSWORD = "Пароль короче 6-ти символов";
    public static final String ERROR_WRONG_CAPTURE = "Код с картинки введён неверно";


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public PostDataResponse() {
        result = false;
    }
}
