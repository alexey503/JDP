package main.api.response;

import main.model.entities.Post;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {
    private long count;
    private List<PostDto> posts;

    public PostResponse(Page<Post> posts) {

        this.posts = new ArrayList<>();
        for (Post post : posts) {
            this.posts.add(
                    new PostDto(post,
                            post.getPostVotes().stream().filter(postVote -> postVote.getValue() > 0).count(),
                            post.getPostComments().size())
            );
        }
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


