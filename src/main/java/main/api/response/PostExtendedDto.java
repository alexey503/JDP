package main.api.response;

import lombok.Data;
import main.model.entities.PostComment;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostExtendedDto {

        private int id;
        private long timestamp;
        private boolean active;
        private PostUserEntity user;

        private String title;
        private String text;
        private int likeCount;
        private int dislikeCount;
        private int viewCount;

        private List<PostComment> comments;

        private List<String> tags;

        public PostExtendedDto(int id, long timestamp, boolean active, PostUserEntity user, String title, String text, int likeCount, int dislikeCount, int viewCount, List<PostComment> comments, List<String> tags) {
                this.id = id;
                this.timestamp = timestamp;
                this.active = active;
                this.user = user;
                this.title = title;
                this.text = text;
                this.likeCount = likeCount;
                this.dislikeCount = dislikeCount;
                this.viewCount = viewCount;
                if (comments != null) {
                        this.comments = comments;
                } else {
                        this.comments = new ArrayList<>();
                }
                this.tags = tags;
        }
}
