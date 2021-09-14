package main.service;

import main.api.request.ModerationActivityRequest;
import main.api.request.PostPostCommentRequest;
import main.api.request.PostPostRequest;
import main.api.response.*;
import main.controllers.ApiPostController;
import main.model.ModerationStatus;
import main.model.entities.*;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostsRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostsService {
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostCommentsRepository commentsRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private SettingsService settingsService;


    public PostResponse getPostResponse(int offset, int limit, String mode) {

        switch (mode) {
            case ApiPostController.MODE_RECENT:
                return new PostResponse(postsRepository.findPostsPage(PageRequest.of(offset / limit, limit,
                        Sort.by("time").descending())));
            case ApiPostController.MODE_EARLY:
                return new PostResponse(postsRepository.findPostsPage(PageRequest.of(offset / limit, limit,
                        Sort.by("time").ascending())));
            case ApiPostController.MODE_BEST://сортировать по убыванию количества лайков
                return new PostResponse(
                        postsRepository.findPostsPageSortedByLikesCount(PageRequest.of(offset / limit, limit)));
            case ApiPostController.MODE_POPULAR://сортировать по убыванию количества комментариев
                return new PostResponse(
                        postsRepository.findPostsPageSortedByCommentsCount(PageRequest.of(offset / limit, limit)));

            default:
                return new PostResponse();
        }
    }

    public static String getAnnounce(String text) {
        String textWithOutTags = Pattern.compile("(<[^>]*>)")
                .matcher(text)
                .replaceAll("");
        if (textWithOutTags.length() > 150) {
            return textWithOutTags.substring(0, 150) + "...";
        } else {
            return textWithOutTags;
        }
    }

    public PostResponse getPostSearchByStringQuery(int offset, int limit, String query) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Page<Post> postPage = postsRepository.postSearchByStringQuery(query, pageable);

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

        Page<Post> postPage = postsRepository.postSearchByDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                pageable);

        return getPostResponse(postPage);
    }


    public PostResponse getPostSearchByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());

        Page<Post> postPage = postsRepository.postSearchByTag(tag, pageable);

        return getPostResponse(postPage);
    }

    private PostResponse getPostResponse(Page<Post> postPage) {

        PostResponse postResponse = new PostResponse();
        List<PostDto> posts = new ArrayList<>();

        for (Post post : postPage) {
            PostDto postDto = new PostDto(post.getId(),
                    post.getUser(),
                    post.getTime(),
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

    public PostExtendedDto getPostById(int id, User user) {
        Optional<Post> optionalPost = postsRepository.findById(id);
        if (optionalPost.isEmpty()) {
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

        if (user != null && (user.getIsModerator() == 0) &&
                post.getUser().getId() != user.getId()) {
            post.setViewCount(post.getViewCount() + 1);
            postsRepository.save(post);
        }

        List<PostComment> postComments = commentsRepository.findByPostId(id);

        return new PostExtendedDto(
                post.getId(),
                post.getTime(),
                post.getIsActive() == 1,
                post.getUser(),
                post.getTitle(),
                post.getText(),
                likeCount,
                dislikeCount,
                post.getViewCount(),
                postComments,
                post.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
    }

    public PostResponse getMyPosts(int offset, int limit, String status, Principal principal) {
        switch (status) {
            case ApiPostController.STATUS_INACTIVE:
                return new PostResponse(
                        postsRepository.findMyPostsInactive(PageRequest.of(offset / limit, limit), principal.getName()));
            case ApiPostController.STATUS_PENDING:
                return new PostResponse(
                        postsRepository.findMyPostsPending(PageRequest.of(offset / limit, limit), principal.getName()));
            case ApiPostController.STATUS_DECLINED:
                return new PostResponse(
                        postsRepository.findMyPostsDeclined(PageRequest.of(offset / limit, limit), principal.getName()));
            case ApiPostController.STATUS_PUBLISHED:
                return new PostResponse(
                        postsRepository.findMyPostsPublished(PageRequest.of(offset / limit, limit), principal.getName()));
            default:
                return new PostResponse();
        }
    }

    public PostDataResponse addComment(PostPostCommentRequest postPostCommentRequest, String userName) {
        PostComment newComment = new PostComment();

        Optional<User> user = userRepository.findByEmail(userName);
        if (user.isPresent()) {
            newComment.setUser(user.get());
        } else {
            return new PostDataResponse();
        }

        newComment.setText(postPostCommentRequest.getText());
        newComment.setPostId(postPostCommentRequest.getPostId());
        newComment.setParent(postsRepository.findById(postPostCommentRequest.getParentId()).orElse(null));
        newComment.setTime(new Date().getTime() / 1000);

        commentsRepository.save(newComment);
        return new PostDataResponse(true);
    }

    public PostDataResponse postNewPost(PostPostRequest postPostRequest, String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            return new PostDataResponse();
        }
        return getPostDataResponseForPostEdit(postPostRequest, optionalUser.get(), false, -1);
    }

    public PostDataResponse editPost(PostPostRequest postPostRequest, int id, String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            return new PostDataResponse();
        }

        if (optionalUser.get().getIsModerator() == 1) {
            return getPostDataResponseForPostEdit(postPostRequest, optionalUser.get(), true, id);
        }
        return new PostDataResponse();
    }


    private PostDataResponse getPostDataResponseForPostEdit(PostPostRequest postPostRequest, User user, boolean isModerator, int id) {

        Map<String, String> errors = new HashMap<>();
        if (postPostRequest.getTitle().length() < 3) {
            errors.put(PostDataResponse.ERR_TYPE_TITLE, PostDataResponse.ERROR_SHORT_TITLE);
        }

        if (postPostRequest.getText().length() < 50) {
            errors.put(PostDataResponse.ERR_TYPE_TEXT, PostDataResponse.ERROR_SHORT_TEXT);
        }

        long nowTime = new Date().getTime() / 1000;
        if (nowTime > postPostRequest.getTimestamp()) {
            postPostRequest.setTimestamp(nowTime);
        }

        PostDataResponse postDataResponse = new PostDataResponse();
        if (errors.isEmpty()) {
            main.model.entities.Post newPost = new main.model.entities.Post();
            if (id >= 0) {
                newPost.setId(id);
            }
            newPost.setTime(postPostRequest.getTimestamp());
            newPost.setIsActive(postPostRequest.getActive());
            newPost.setTitle(postPostRequest.getTitle());

            newPost.setTags(tagsService.getTagsForPost(postPostRequest.getTags()));

            newPost.setText(postPostRequest.getText());


            if (!isModerator && settingsService.getSettingValue(SettingsService.KEY_POST_PREMODERATION)) {
                newPost.setModerationStatus(ModerationStatus.NEW);
            } else {
                newPost.setModerationStatus(ModerationStatus.ACCEPTED);
            }
            newPost.setUser(new PostUserEntity(user.getId(), user.getName()));

            postsRepository.save(newPost);

            postDataResponse.setResult(true);
        } else {
            postDataResponse.setErrors(errors);
        }
        return postDataResponse;
    }

    public PostResponse getPostResponseModeration(int offset, int limit, String status, String userName) {
        if (status.equals("NEW")) {
            return new PostResponse(postsRepository.findPostsPageModerationNew(PageRequest.of(offset / limit, limit)));
        }
        if (status.equals("ACCEPTED")) {
            return new PostResponse(postsRepository.findPostsPageModeration(PageRequest.of(offset / limit, limit), userRepository.findByEmail(userName).get(),
                    ModerationStatus.ACCEPTED));
        }
        if (status.equals("DECLINED")) {
            return new PostResponse(postsRepository.findPostsPageModeration(PageRequest.of(offset / limit, limit), userRepository.findByEmail(userName).get(),
                    ModerationStatus.DECLINED));
        }
        return new PostResponse();
    }

    public PostDataResponse moderationActivity(ModerationActivityRequest request, String authUserEmail) {
        User moderator = userRepository.findByEmail(authUserEmail).get();
        Optional<Post> postOptional = postsRepository.findById(request.getPostId());
        if (postOptional.isEmpty()) {
            return new PostDataResponse();
        }
        Post post = postOptional.get();

        if (request.getDecision().equalsIgnoreCase("accept")) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        if (request.getDecision().equalsIgnoreCase("decline")) {
            post.setModerationStatus(ModerationStatus.DECLINED);
        }
        post.setModerator(moderator);

        postsRepository.save(post);

        return new PostDataResponse(true);
    }

    public ResponseEntity<PostExtendedDto> getPostByIdRequest(int id, User authUser) {
        PostExtendedDto postExtendedDto = getPostById(id, authUser);
        if (postExtendedDto != null) {
            return ResponseEntity.ok(postExtendedDto);
        }
        return ResponseEntity.notFound().build();
    }
}