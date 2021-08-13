package main.controllers;

import main.api.request.PostPostCommentRequest;
import main.api.request.PostPostRequest;
import main.api.response.PostDataResponse;
import main.api.response.PostExtendedDto;
import main.api.response.PostResponse;
import main.api.response.PostUserEntity;
import main.model.ModerationStatus;
import main.model.entities.User;
import main.model.repositories.UserRepository;
import main.service.LoadImageService;
import main.service.PostsService;
import main.service.TagsService;
import main.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ApiPostController {

    public static final String MODE_RECENT = "recent";
    public static final String MODE_EARLY = "early";
    public static final String MODE_POPULAR = "popular";
    public static final String MODE_BEST = "best";

    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_DECLINED = "declined";
    public static final String STATUS_PUBLISHED = "published";

    @Autowired
    private final PostsService postsService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TagsService tagsService;
    @Autowired
    private final LoadImageService loadImageService;
    @Autowired
    private final VoteService voteService;


    @GetMapping("/api/post")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return ResponseEntity.ok().body(postsService.getPostResponse(offset, limit, mode));
    }

    @PostMapping("/api/post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postNewPost(@RequestBody PostPostRequest postPostRequest) {
        return ResponseEntity.ok().body(postsService.postNewPost(postPostRequest));
    }

    @PutMapping("/api/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postEdit(@PathVariable int id, @RequestBody PostPostRequest postPostRequest) {
        return ResponseEntity.ok().body(postsService.editPost(postPostRequest, id));
    }


    //TODO добавление комментания к посту Api page 17
    @PostMapping("/api/comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postPostComment(@RequestBody PostPostCommentRequest postPostCommentRequest) {
        return ResponseEntity.ok().body(postsService.addComment(postPostCommentRequest));
    }

    //TODO добавление лайка Api page 24
    @PostMapping("/api/post/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postLike(@RequestBody(name = "post_id") int postId) {
        return ResponseEntity.ok().body(voteService.putVote(postId, userId, 1));
    }

    //TODO добавление дизлайка Api page 24
    @PostMapping("/api/post/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postDislike(@RequestBody int post_id) {
        return ResponseEntity.ok().body(voteService.putVote(postId, userId, -1));
    }


    @GetMapping("/api/post/search")
    public ResponseEntity<PostResponse> postSearchByStringQuery(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query) {

        if (query == null || query.trim().length() == 0) {
            return ResponseEntity.ok().body(postsService.getPostResponse(offset, limit, ApiPostController.MODE_RECENT));

        } else {
            return ResponseEntity.ok().body(postsService.getPostSearchByStringQuery(offset, limit, query));
        }
    }

    @GetMapping("/api/post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostResponse> getMyPosts(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "status") String status,
            Principal principal) {

        return ResponseEntity.ok(postsService.getMyPosts(offset, limit, status, principal));
    }

    @GetMapping("/api/post/byDate")
    public PostResponse postSearchByDate(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "date") String dateString) {

        return postsService.getPostSearchByDate(offset, limit, dateString);
    }

    @GetMapping("/api/post/byTag")
    public PostResponse postSearchByTag(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "tag") String tag) {

        return postsService.getPostSearchByTag(offset, limit, tag);
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostExtendedDto> postGetById(@PathVariable int id) {
        PostExtendedDto postExtendedDto = postsService.getPostById(id);
        if (postExtendedDto != null) {
            return ResponseEntity.ok(postExtendedDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/api/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image) {

        PostDataResponse response = this.loadImageService.uploadImage(image);
        if (!response.getResult()) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response.getResultDataString());
        }
    }
}
