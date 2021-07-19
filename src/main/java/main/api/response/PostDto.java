package main.api.response;

import main.model.entities.PostVote;

import java.util.List;

public class PostDto {

        private int id;
        private PostUserEntity user;
        private long timestamp;
        private String title;
        private String announce;
        private int viewCount;
        private int commentCount;
        private int likeCount;
        private int dislikeCount;

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

        public int getCommentCount() {
                return commentCount;
        }

        public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
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

}
