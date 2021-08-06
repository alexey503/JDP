package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitResponse {
    @Value("${blogNew.title}")
    private String title;
    @Value("${blogNew.subtitle}")
    private String subtitle;
    @Value("${blogNew.phone}")
    private String phone;
    @Value("${blogNew.email}")
    @JsonProperty("email")
    private String email;
    @Value("${blogNew.copyright}")
    private String copyright;
    @Value("${blogNew.copyrightFrom}")
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
