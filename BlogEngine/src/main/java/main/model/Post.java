package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="posts")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="is_active", nullable = false)
    private byte isActive;

    //@Column(name="moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Column(name="moderation_status", length = 8, nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name="moderator_id")
    private int moderatorId;

    @Column(name="user_id", nullable = false)
    private int userId;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(length = 65535,columnDefinition="Text", nullable = false)
    private String text;
    
    @Column(name="view_count", nullable = false)
    private int viewCount;

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

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
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
