package main.api.response;

import lombok.Data;

@Data
public class MyStatisticsResponse {
    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private long firstPublication;
}
