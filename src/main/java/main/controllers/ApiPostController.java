package main.controllers;

import main.api.request.PostPostCommentRequest;
import main.api.request.PostPostRequest;
import main.api.response.PostExtendedDto;
import main.api.response.PostResponse;
import main.api.response.PostDataResponse;
import main.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ApiPostController {

    public static final String MODE_RECENT =  "recent";
    public static final String MODE_EARLY =   "early";
    public static final String MODE_POPULAR = "popular";
    public static final String MODE_BEST =    "best";

    public static final String STATUS_INACTIVE =  "inactive";
    public static final String STATUS_PENDING =   "pending";
    public static final String STATUS_DECLINED =   "declined";
    public static final String STATUS_PUBLISHED = "published";

    private final PostsService postsService;

    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }


    @GetMapping("/api/post")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {

        return ResponseEntity.ok().body(postsService.getPostResponse(offset, limit, mode));
    }


    //TODO добавление поста Api page 14
    @PostMapping("/api/post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postPost(@RequestBody PostPostRequest postPostRequest) {
        return ResponseEntity.ok().body(new PostDataResponse());
    }

    //TODO добавление комментания к посту Api page 17
    @PostMapping("/api/comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postPostComment(@RequestBody PostPostCommentRequest postPostCommentRequest) {
        return ResponseEntity.ok().body(new PostDataResponse());
    }

    //TODO добавление лайка Api page 24
    @PostMapping("/api/post/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postLike(@RequestBody int post_id) {
        return ResponseEntity.ok().body(new PostDataResponse());
    }

    //TODO добавление дизлайка Api page 24
    @PostMapping("/api/post/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postDislike(@RequestBody int post_id) {
        return ResponseEntity.ok().body(new PostDataResponse());
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


    //TODO редактирование поста Api page 15
    @PutMapping("/api/post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostDataResponse> postEdit(@PathVariable int id) {

        return ResponseEntity.ok(new PostDataResponse());
    }
}
