package main.api.response;

import main.model.Post;

import java.util.List;

public class PostResponse {
    private long count;
    private List<PostDto> posts;



    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}


