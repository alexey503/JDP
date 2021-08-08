package main.model.entities;

import lombok.Data;
import main.api.response.PostUserEntity;
import main.model.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
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

    @ManyToOne(cascade = CascadeType.MERGE)
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
}
