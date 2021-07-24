package main.service;

import main.api.response.PostDto;
import main.api.response.PostExtendedDto;
import main.api.response.PostResponse;
import main.controllers.ApiPostController;
import main.model.entities.Post;
import main.model.entities.PostVote;
import main.model.entities.Tag;
import main.model.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity getPostResponse(int offset, int limit, String mode) {

        Sort sort;
        switch (mode){
            case ApiPostController.MODE_RECENT:
                sort = Sort.by("time").ascending();
                break;
            case ApiPostController.MODE_EARLY:
                sort = Sort.by("time").descending();
                break;
            case ApiPostController.MODE_BEST://сортировать по убыванию количества лайков
                sort = Sort.by("likesCount").descending();
                break;
            case ApiPostController.MODE_POPULAR://сортировать по убыванию количества комментариев
                sort = Sort.by("commentsCount").descending();
                break;

            default:
                return ResponseEntity.badRequest().body("Error mode not found");
        }

        return ResponseEntity.ok(new PostResponse(
                repository.findPostsDto(PageRequest.of(offset / limit, limit, sort))));
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

    public ResponseEntity getPostSearchByStringQuery(int offset, int limit, String query) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Page<Post> postPage = repository.postSearchByStringQuery(query, pageable);

        return ResponseEntity.ok(getPostResponse(postPage));
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

        int likeCount = 0;
        int dislikeCount = 0;
        for (PostVote postVote : post.getPostVotes()) {
            if (postVote.getValue() > 0) {
                likeCount++;
            }
            if (postVote.getValue() < 0) {
                dislikeCount++;
            }
        }

        return new PostExtendedDto(
                post.getId(),
                post.getTime() / 1000,
                post.isActive() == 1,
                post.getUser(),
                post.getTitle(),
                post.getText(),
                likeCount,
                dislikeCount,
                post.getViewCount(),
                post.getPostComments(),
                post.getTags().stream().map(Tag::getName).collect(Collectors.toList()));

    }
}