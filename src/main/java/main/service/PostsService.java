package main.service;

import main.api.response.PostResponse;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repository;

    private List<Post>posts;

    public PostResponse getPostResponse(int offset, int limit, String mode){
        PostResponse postResponse = new PostResponse();
        List<Post>posts = new ArrayList<>();
        if(this.posts == null){
            this.posts = new ArrayList<>();
            Iterable<Post> postIterable = repository.findAll();
            for (Post post : postIterable) {
                this.posts.add(post);
            }
        }

        long counter = 0;
        for (Post post : this.posts) {

            if(post.isActive() == 1 &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED &&
                    post.getTime().before(new Date())) {

                counter++;
                if (counter >= offset && counter < limit) {
                    posts.add(post);
                }
            }
        }

        postResponse.setCount(counter);
        postResponse.setPosts(posts);
        return postResponse;
    }
}
