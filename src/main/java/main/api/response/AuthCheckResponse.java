package main.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.model.UserEntity;

public class AuthCheckResponse {

    private boolean result;

    @JsonIgnore
    private UserEntity userEntity;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
