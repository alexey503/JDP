package main.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import main.model.entities.User;

@Data
public class AuthCheckResponse {

    private boolean result;

    @JsonIgnore
    private User user;

}
