package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", length = 8, nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    @Where(clause = "is_moderator > 0")
    private User moderator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    //private User user;
    private PostResponseUser user;

    @JsonProperty("timestamp")
    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    //@JsonProperty("announce")
    @JsonIgnore
    @Column(length = 65535, columnDefinition = "Text", nullable = false)
    private String text;

    @Transient
    private String announce;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "post_comments",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PostComment> postComments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "post_votes",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PostVote> postVotes;

    @Transient
    private int commentCount;
    @Transient
    private int likeCount;
    @Transient
    private int dislikeCount;

    public PostResponseUser getUser() {
        return user;
    }

    public String getAnnounce() {
        String textWithOutTags = Pattern.compile("(<[^>]*>)")
                .matcher(text)
                .replaceAll("");
        if (text.length() > 150) {
            return textWithOutTags.substring(0, 150) + "...";
        } else {
            return textWithOutTags;
        }
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }


    public int getCommentCount() {
        return postComments.size();
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(List<PostVote> postVotes) {
        this.postVotes = postVotes;
    }

    public int getLikeCount() {
        int count = 0;
        for (PostVote postVote : postVotes) {
            if (postVote.getValue() > 0) {
                count++;
            }
        }
        likeCount = count;
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        int count = 0;
        for (PostVote postVote : postVotes) {
            if (postVote.getValue() < 0) {
                count++;
            }
        }
        dislikeCount = count;

        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte isActive() {
        return isActive;
    }

    public void setActive(byte active) {
        isActive = active;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public long getTime() {
        return time.getTime() / 1000;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
