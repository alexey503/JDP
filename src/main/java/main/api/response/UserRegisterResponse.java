package main.api.response;

import lombok.Data;

import java.util.Map;

@Data
public class UserRegisterResponse {
    private boolean result;
    private Map<String, String> errors;

    public UserRegisterResponse(boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

}
