package main.api.response;

import main.model.entities.Post;
import main.model.entities.PostVote;
import main.service.PostsService;
import org.jsoup.Jsoup;

import java.util.List;

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

        public PostDto(Post post, long likes, long commentsCount) {
                this.id = post.getId();
                this.user = post.getUser();
                this.timestamp = post.getTime();
                this.title = post.getTitle();
                this.announce = Jsoup.parse(post.getText()).text();
                //this.announce = PostsService.getAnnounce(post.getText());
                this.viewCount = post.getViewCount();
                this.commentCount = post.getPostComments().size();
                //this.commentCount = commentsCount;
                this.likeCount = likes;

                this.dislikeCount = 0;
                for (PostVote postVote : post.getPostVotes()) {
                        if(postVote.getValue() < 0) {
                                this.dislikeCount++;
                        }
                }
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public PostUserEntity getUser() {
                return user;
        }

        public void setUser(PostUserEntity user) {
                this.user = user;
        }

        public long getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getAnnounce() {
                return announce;
        }

        public void setAnnounce(String announce) {
                this.announce = announce;
        }

        public int getViewCount() {
                return viewCount;
        }

        public void setViewCount(int viewCount) {
                this.viewCount = viewCount;
        }

        public long getCommentCount() {
                return commentCount;
        }

        public void setCommentCount(long commentCount) {
                this.commentCount = commentCount;
        }

        public long getLikeCount() {
                return likeCount;
        }

        public void setLikeCount(long likeCount) {
                this.likeCount = likeCount;
        }

        public long getDislikeCount() {
                return dislikeCount;
        }

        public void setDislikeCount(long dislikeCount) {
                this.dislikeCount = dislikeCount;
        }

}
