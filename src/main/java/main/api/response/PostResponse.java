package main.api.response;
/*
    {
        "count": 390,
        "posts": [
          {
            "id": 345,
            "timestamp": 1592338706,
            "user":
              {
                "id": 88,
                "name": "Дмитрий Петров"
              },
            "title": "Заголовок поста",
            "announce": "Текст анонса поста без HTML-тэгов",
            "likeCount": 36,
            "dislikeCount": 3,
            "commentCount": 15,
            "viewCount": 55
           },
        {...}
        ]
    }
*/

import main.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostResponse {
    private long count;

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}


