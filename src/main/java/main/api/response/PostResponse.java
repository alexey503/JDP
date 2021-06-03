package main.api.response;

import main.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResponse {
    private long count;

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}


