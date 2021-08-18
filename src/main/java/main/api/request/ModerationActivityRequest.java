package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModerationActivityRequest {
    @JsonProperty("post_id")
    private Integer postId;

    private String  decision;

}
