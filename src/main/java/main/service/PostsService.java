package main.service;

import main.api.response.PostDto;
import main.api.response.PostResponse;
import main.controllers.ApiPostController;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repository;

    public PostResponse getPostResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse();

        Page<Post> postPage;
        Pageable pageable;

        if (mode.equals(ApiPostController.MODE_RECENT)) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
            postPage = repository.findAllByIsActiveAndModerationStatusAndTimeBefore((byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
        } else if (mode.equals(ApiPostController.MODE_EARLY)){
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
            postPage = repository.findAllByIsActiveAndModerationStatusAndTimeBefore((byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
        }else if (mode.equals(ApiPostController.MODE_BEST)) {
            pageable = PageRequest.of(offset / limit, limit);
            postPage = repository.findAllBest(pageable);
        } else if (mode.equals(ApiPostController.MODE_POPULAR)){
            pageable = PageRequest.of(offset / limit, limit);
            postPage = repository.findAllPopular(pageable);
        }else {
            return postResponse;
        }

        List<PostDto> posts = new ArrayList<>();
        for (Post post : postPage) {
                PostDto postDto = new PostDto(post.getId(),
                        post.getUser(),
                        post.getTime() / 1000,
                        post.getTitle(),
                        getAnnounce(post.getText()),
                        post.getViewCount(),
                        post.getPostComments().size(),
                        post.getPostVotes()
                );
                posts.add(postDto);
        }
        postResponse.setPosts(posts);
        postResponse.setCount(postPage.getTotalElements());
        return postResponse;
    }

    public static String getAnnounce(String text) {
        String textWithOutTags = Pattern.compile("(<[^>]*>)")
                .matcher(text)
                .replaceAll("");
        if (text.length() > 150) {
            return textWithOutTags.substring(0, 150) + "...";
        } else {
            return textWithOutTags;
        }
    }

    public PostResponse getPostSearch(int offset, int limit, String query) {
        PostResponse postResponse = new PostResponse();

        List <Post> postList = new ArrayList<>();//repository.findAllByIsActiveAndModerationStatusAndTimeBefore((byte) 1, ModerationStatus.ACCEPTED, new Date(), );
        postResponse.setCount(postList.size());

        if(limit == 0 || offset/limit >= postList.size()) {
            return postResponse;
        }

        List<PostDto> posts = new ArrayList<>();

        for (int i = offset/limit; (i < limit) && (i < postList.size()) ; i++) {
            Post post = postList.get(i);
            if(post.getText().contains(query)) {
                PostDto postDto = new PostDto(post.getId(),
                        post.getUser(),
                        post.getTime() / 1000,
                        post.getTitle(),
                        getAnnounce(post.getText()),
                        post.getViewCount(),
                        post.getPostComments().size(),
                        post.getPostVotes()
                );
                posts.add(postDto);
            }
        }

        postResponse.setPosts(posts);
        postResponse.setCount(posts.size());

        return postResponse;
    }
}