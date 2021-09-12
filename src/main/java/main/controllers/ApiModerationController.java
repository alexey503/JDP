package main.controllers;

import main.api.request.ModerationActivityRequest;
import main.api.response.PostDataResponse;
import main.api.response.PostResponse;
import main.service.AuthService;
import main.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiModerationController {

    @Autowired
    private PostsService postsService;

    @Autowired
    private AuthService authService;

    @GetMapping("/api/post/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostResponse> getPostsModeration(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "status", required = false, defaultValue = "new") String status) {
        return ResponseEntity.ok().body(postsService.getPostResponseModeration(offset, limit, status.toUpperCase(), authService.getAuthUserEmail()));
    }

    @PostMapping("/api/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostDataResponse> postModerationActivity(@RequestBody ModerationActivityRequest request) {
        return ResponseEntity.ok().body(postsService.moderationActivity(request, authService.getAuthUserEmail()));
    }
}
