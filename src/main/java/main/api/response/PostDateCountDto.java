package main.api.response;

import lombok.Data;

@Data
public class PostDateCountDto {
    private String date;
    private String count;

    public PostDateCountDto(String date, String count) {
        this.date = date;
        this.count = count;
    }
}
