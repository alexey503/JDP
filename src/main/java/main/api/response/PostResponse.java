package main.api.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PostResponse {
    private long count;
    private List<PostDto> posts;

    public PostResponse(Page<PostDto> posts) {
        this.posts = posts.getContent();
        this.count = posts.getTotalElements();
    }

    public PostResponse() {
    }

    public List<PostDto> getPosts() { return posts; }
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


