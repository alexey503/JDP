package main.controllers;

import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    public static final String MODE_RECENT = "recent";
    public static final String MODE_POPULAR = "popular";
    public static final String MODE_BEST = "best";
    public static final String MODE_EARLY = "early";

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
        //System.out.println("offset=" + offset + " limit=" + limit + " mode=" + mode);
        if (mode.equals(ApiPostController.MODE_EARLY) ||
                (mode.equals(ApiPostController.MODE_RECENT))) {
            return postsService.getPostResponseSortByDate(offset, limit, mode);
        } else if (mode.equals(ApiPostController.MODE_POPULAR) ||
                (mode.equals(ApiPostController.MODE_BEST))) {
            return postsService.getPostResponseSortByVote(offset, limit, mode);

        }
        return null;
    }

    //TODO @GetMapping(/api/post/search) page 5

    //TODO @GetMapping(/api/calendar) page 6

    //TODO @GetMapping(/api/post/byDate)

    //TODO @GetMapping(/api/post/byTag)

    //TODO @GetMapping(/api/post/{ID}) Page 9
}
