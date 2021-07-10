package main.service;

import main.api.response.PostDto;
import main.api.response.PostExtendedDto;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repository;

    public PostResponse getPostResponse(int offset, int limit, String mode) {

        Page<Post> postPage;
        Pageable pageable;

        if (mode.equals(ApiPostController.MODE_RECENT)) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
            postPage = repository.findAllByIsActiveAndModerationStatusAndTimeBefore((byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
        } else if (mode.equals(ApiPostController.MODE_EARLY)) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
            postPage = repository.findAllByIsActiveAndModerationStatusAndTimeBefore((byte) 1, ModerationStatus.ACCEPTED, new Date(), pageable);
        } else if (mode.equals(ApiPostController.MODE_BEST)) {
            pageable = PageRequest.of(offset / limit, limit);
            postPage = repository.findAllBest(pageable);
        } else if (mode.equals(ApiPostController.MODE_POPULAR)) {
            pageable = PageRequest.of(offset / limit, limit);
            postPage = repository.findAllPopular(pageable);
        } else {
            return new PostResponse();
        }

        return getPostResponse(postPage);
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

    public PostResponse getPostSearchByStringQuery(int offset, int limit, String query) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Page<Post> postPage = repository.postSearchByStringQuery(query, pageable);

        return getPostResponse(postPage);
    }

    public PostResponse getPostSearchByDate(int offset, int limit, String dateString) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));
        } catch (ParseException e) {
            return new PostResponse();
        }

        Page<Post> postPage = repository.postSearchByDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                pageable);

        return getPostResponse(postPage);
    }


    public PostResponse getPostSearchByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Page<Post> postPage = repository.postSearchByTag(tag, pageable);

        return getPostResponse(postPage);

    }


    private PostResponse getPostResponse(Page<Post> postPage) {

        PostResponse postResponse = new PostResponse();
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

    public PostExtendedDto getPostById(int id) {
        Optional<Post> optionalPost = repository.findById(id);
        if( optionalPost.isEmpty()){
            return null;
        }
        Post post = optionalPost.get();
        return new PostExtendedDto(
                post.getId(),
                post.getTime() / 1000,
                post.isActive() == 1,
                post.getUser(),
                post.getTitle(),
                post.getText(),
                +1,
                -1,
                post.getViewCount(),
                post.getPostComments(),
                post.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList()));

    }
}