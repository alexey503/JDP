package main.service;

import main.api.response.AuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthCheckResponse getAuthCheckResponse() {
        return new AuthCheckResponse();
    }
}
