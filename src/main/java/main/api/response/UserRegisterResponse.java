package main.api.response;

import java.util.Map;

public class UserRegisterResponse {
    private boolean result;
    private Map<String, String> errors;

    public UserRegisterResponse(boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
