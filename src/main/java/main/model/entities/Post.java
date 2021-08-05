package main.model.entities;

import main.api.response.PostUserEntity;
import main.model.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", length = 8, nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    @Where(clause = "is_moderator > 0")
    private User moderator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private PostUserEntity user;

    @Column(name = "time", nullable = false)
    private long time;

    @Column(nullable = false)
    private String title;

    @Column(length = 65535, columnDefinition = "Text", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany
    @JoinTable(name = "post_comments",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PostComment> postComments;


    @OneToMany
    @JoinTable(name = "post_votes",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})

    private List<PostVote> postVotes;

    @ManyToMany
    @JoinTable(name = "tag2post",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;


    public Post() {
    }

    public Post(int id, byte isActive, ModerationStatus moderationStatus, User moderator, PostUserEntity user, long time, String title, String text, int viewCount, List<PostComment> postComments, List<PostVote> postVotes, List<Tag> tags) {
        this.id = id;
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
        this.moderator = moderator;
        this.user = user;
        this.time = time;
        this.title = title;
        this.text = text;
        this.viewCount = viewCount;
        this.postComments = postComments;
        this.postVotes = postVotes;
        this.tags = tags;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public void setUser(PostUserEntity user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public PostUserEntity getUser() {
        return user;
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(List<PostVote> postVotes) {
        this.postVotes = postVotes;
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
        return time;
    }

    public void setTime(long time) {
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
