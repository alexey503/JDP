package main.controllers;

import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    private final PostsService postsService;

    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }

    //TODO GET /api/post
    @GetMapping("/api/post")
    public PostResponse getPost(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = true) String mode
    )
    {
        System.out.println("offset=" + offset + " limit=" + limit + " mode=" + mode);
        return postsService.getPostResponse(offset, limit, mode);
    }
}
