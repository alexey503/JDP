package main.service;

import main.api.response.PostDto;
import main.api.response.PostResponse;
import main.controllers.ApiPostController;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repository;



    public PostResponse getPostResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();

        Pageable pageable;
        if(mode.equals(ApiPostController.MODE_POPULAR)){
            //сортировать по убыванию количества комментариев (посты без комментариев выводить)
            pageable = PageRequest.of(offset/limit, limit, Sort.by("commentCount").ascending());
        }else if(mode.equals(ApiPostController.MODE_BEST)){
            // сортировать по убыванию количества лайков (посты без лайков и дизлайков выводить)
            pageable = PageRequest.of(offset/limit, limit, Sort.by("likeCount").ascending());
        }else if(mode.equals(ApiPostController.MODE_EARLY)){
            pageable = PageRequest.of(offset/limit, limit, Sort.by("time").ascending());
        }else { //ApiPostController.MODE_RECENT as default
            pageable = PageRequest.of(offset/limit, limit, Sort.by("time").descending());
        }

        Iterable<Post> postIterable;

        postIterable = repository.findAllByIsActiveAndModerationStatus((byte)1, ModerationStatus.ACCEPTED, pageable);

        List<PostDto> posts = new ArrayList<>();
        for (Post post : postIterable) {
/*
            System.out.println("Id = " + post.getId() +
                    " Is active = " + post.isActive() +
                    " Moderation status = " + post.getModerationStatus().name() +
                    " Date = " + new Date(post.getTime() * 1000).toString() +
                    " Comments count = " + post.getCommentCount() +
                    " like count = " + post.getLikeCount() +
                    " Dis like count = " + post.getDislikeCount()
                    );

 */
            PostDto postDto = new PostDto(post.getId(),
                    post.getUser(),
                    post.getTime()/1000,
                    post.getTitle(),
                    post.getText(),
                    post.getViewCount(),
                    post.getCommentCount(),
                    post.getLikeCount(),
                    post.getDislikeCount()
                    );

            posts.add(postDto);
        }

        postResponse.setCount(repository.count());
        postResponse.setPosts(posts);

        return postResponse;
    }


}