package main.api.response;

import lombok.Data;
import main.model.entities.Post;
import main.model.entities.PostVote;
import org.jsoup.Jsoup;

import java.util.List;

@Data
public class PostDto {

    private int id;
    private PostUserEntity user;
    private long timestamp;
    private String title;
    private String announce;
    private int viewCount;
    private long commentCount;
    private long likeCount;
    private long dislikeCount;

    public PostDto() {
    }

    public PostDto(int id, PostUserEntity user, long timestamp, String title, String announce, int viewCount, int commentCount, List<PostVote> postVotes) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
        this.title = title;
        this.announce = announce;
        this.viewCount = viewCount;
        this.commentCount = commentCount;

        this.likeCount = 0;
        this.dislikeCount = 0;

        for (PostVote postVote : postVotes) {
            if (postVote.getValue() > 0) {
                this.likeCount++;
            }
            if (postVote.getValue() < 0) {
                this.dislikeCount++;
            }
        }
    }

    public PostDto(Post post, long likes) {
        this.id = post.getId();
        this.user = post.getUser();
        this.timestamp = post.getTime();
        this.title = post.getTitle();
        this.announce = Jsoup.parse(post.getText()).text();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getPostComments().size();
        this.likeCount = likes;

        this.dislikeCount = 0;
        for (PostVote postVote : post.getPostVotes()) {
            if (postVote.getValue() < 0) {
                this.dislikeCount++;
            }
        }
    }
}
