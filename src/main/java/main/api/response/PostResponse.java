package main.api.response;

import lombok.Data;
import main.model.entities.Post;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostResponse {
    private long count;
    private List<PostDto> posts;

    public PostResponse(Page<Post> posts) {

        this.posts = new ArrayList<>();
        for (Post post : posts) {
            this.posts.add(
                    new PostDto(post,
                            post.getPostVotes().stream().filter(postVote -> postVote.getValue() > 0).count())
            );
        }
        this.count = posts.getTotalElements();
    }

    public PostResponse() {
        count = 0;
        posts = new ArrayList<>();
    }
}


