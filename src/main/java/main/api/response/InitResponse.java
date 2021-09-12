package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
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
}
