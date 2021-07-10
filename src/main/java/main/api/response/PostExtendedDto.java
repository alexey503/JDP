package main.api.response;

import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;

import javax.xml.stream.events.Comment;
import java.util.List;

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
                this.comments = comments;
                this.tags = tags;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public long getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
        }

        public boolean isActive() {
                return active;
        }

        public void setActive(boolean active) {
                this.active = active;
        }

        public PostUserEntity getUser() {
                return user;
        }

        public void setUser(PostUserEntity user) {
                this.user = user;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public int getLikeCount() {
                return likeCount;
        }

        public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
        }

        public int getDislikeCount() {
                return dislikeCount;
        }

        public void setDislikeCount(int dislikeCount) {
                this.dislikeCount = dislikeCount;
        }

        public int getViewCount() {
                return viewCount;
        }

        public void setViewCount(int viewCount) {
                this.viewCount = viewCount;
        }

        public List<PostComment> getComments() {
                return comments;
        }

        public void setComments(List<PostComment> comments) {
                this.comments = comments;
        }

        public List<String> getTags() {
                return tags;
        }

        public void setTags(List<String> tags) {
                this.tags = tags;
        }
}
