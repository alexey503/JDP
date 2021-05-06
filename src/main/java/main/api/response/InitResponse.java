package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitResponse {
    @Value("${BlogEngine.title}")
    private String title;
    @Value("${BlogEngine.subtitle}")
    private String subtitle;
    @Value("${BlogEngine.phone}")
    private String phone;
    @Value("${BlogEngine.email}")
    @JsonProperty("email")
    private String email;
    @Value("${BlogEngine.copyright}")
    private String copyright;
    @Value("${BlogEngine.copyrightFrom}")
    private String copyrightFrom;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }
}
