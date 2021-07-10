package main.controllers;

import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    public static final String MODE_RECENT = "recent";
    public static final String MODE_EARLY = "early";

    public static final String MODE_POPULAR = "popular";
    public static final String MODE_BEST = "best";


    private final PostsService postsService;

    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/api/post")
    public PostResponse getPost(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return postsService.getPostResponse(offset, limit, mode);
    }

    //TODO Done @GetMapping(/api/post/search) page 5
    @GetMapping("/api/post/search")
    public PostResponse postSearch(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query) {

        if (query == null || query.trim().length() == 0) {
            return postsService.getPostResponse(offset, limit, ApiPostController.MODE_RECENT);

        } else {
            return postsService.getPostSearch(offset, limit, query);
        }
    }




    //TODO @GetMapping(/api/post/byDate)

    //TODO @GetMapping(/api/post/byTag)

    //TODO @GetMapping(/api/post/{ID}) Page 9
}
