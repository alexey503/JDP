package main.service;

import main.api.response.PostResponse;
import main.controllers.ApiPostController;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostsService {

    @Autowired
    private PostsRepository repository;

    public PostResponse getPostResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();

        Iterable<Post> postIterable;
        Pageable pageable;


        Pageable pageable1 = PageRequest.of(offset, limit, Sort.by("id").descending());
        //Pageable pageable1 = PageRequest.of(2, 5, Sort.by("id").descending());
        //Pageable pageable = PageRequest.of(offset, limit);

        if(mode.equals(ApiPostController.MODE_POPULAR)){
            pageable = PageRequest.of(offset, limit);
        }else if(mode.equals(ApiPostController.MODE_BEST)){
            pageable = PageRequest.of(offset, limit, Sort.by("commentsCount").ascending());
        }else if(mode.equals(ApiPostController.MODE_EARLY)){
            pageable = PageRequest.of(offset, limit, Sort.by("timestamp").descending());
        }else { //ApiPostController.MODE_RECENT as default
            pageable = PageRequest.of(offset, limit, Sort.by("timestamp").ascending());
        }

        postIterable = repository.findAllPost((byte)1,
                ModerationStatus.ACCEPTED, new Date().getTime(),
                pageable);
        for (Post post : postIterable) {
            System.out.println(post.getText());
        }


       //List<Post> postList = repository.findAllByIsActive((byte)1, pageable1);
        Calendar calendar = new GregorianCalendar();;
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 9);

       //List<Post> postList = repository.findAllWithTimeBefore(calendar.getTime().getTime());
 /*
        for (Post post : postList) {
            System.out.println("IsActive=" + post.isActive() + " ModerationStatus=" + post.getModerationStatus().name()
                            +  post.getText() + " Date=" + (new Date(post.getTime())).toString());
        }
        */

        Post templatePost = new Post();
        templatePost.setActive((byte)1);
        templatePost.setModerationStatus(ModerationStatus.ACCEPTED);

        //ExampleMatcher matcher = ExampleMatcher.matching()

        Example<Post> example = Example.of(templatePost);
/*
        Iterable<Post> postIterable1 = repository.findAll(example);

        for (Post post : postIterable1) {
            System.out.println("IsActive=" + post.isActive() + " ModerationStatus=" + post.getModerationStatus().name() +  post.getText());
        }
*/
        //postResponse.setCount(postList.size());
        //postResponse.setPosts(postList);
        return postResponse;
    }
}
