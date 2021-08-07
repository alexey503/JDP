package main.controllers;

import main.api.response.PostExtendedDto;
import main.api.response.PostResponse;
import main.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    public static final String MODE_RECENT =  "recent";
    public static final String MODE_EARLY =   "early";
    public static final String MODE_POPULAR = "popular";
    public static final String MODE_BEST =    "best";

    private final PostsService postsService;

    public ApiPostController(PostsService postsService) {
        this.postsService = postsService;
    }


    @GetMapping("/api/post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity getPost(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return postsService.getPostResponse(offset, limit, mode);
    }

    @GetMapping("/api/post/search")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity postSearchByStringQuery(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query) {

        if (query == null || query.trim().length() == 0) {
            return postsService.getPostResponse(offset, limit, ApiPostController.MODE_RECENT);

        } else {
            return postsService.getPostSearchByStringQuery(offset, limit, query);
        }

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
    public ResponseEntity postGetById(@PathVariable int id) {
        PostExtendedDto postExtendedDto = postsService.getPostById(id);
        if (postExtendedDto != null) {
            return new ResponseEntity(postExtendedDto, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
