package main.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthCheckResponse {

    private boolean result;

    @JsonIgnore
    private User user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
