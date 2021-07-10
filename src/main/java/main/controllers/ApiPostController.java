package main.controllers;

import main.api.response.PostDto;
import main.api.response.PostExtendedDto;
import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public PostResponse postSearchByStringQuery(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query) {

        if (query == null || query.trim().length() == 0) {
            return postsService.getPostResponse(offset, limit, ApiPostController.MODE_RECENT);

        } else {
            return postsService.getPostSearchByStringQuery(offset, limit, query);
        }
    }

    //TODO Done @GetMapping(/api/post/byDate)
    @GetMapping("/api/post/byDate")
    public PostResponse postSearchByDate(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "date") String dateString) {

        return postsService.getPostSearchByDate(offset, limit, dateString);
    }

    //TODO Done @GetMapping(/api/post/byTag)
    @GetMapping("/api/post/byTag")
    public PostResponse postSearchByTag(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "tag") String tag) {

        return postsService.getPostSearchByTag(offset, limit, tag);
    }

    //TODO @GetMapping(/api/post/{id}) Page 9
    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostExtendedDto> postGetById(@PathVariable int id) {
        PostExtendedDto postExtendedDto = postsService.getPostById(id);
        if(postExtendedDto != null){
            return new ResponseEntity(postExtendedDto, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
