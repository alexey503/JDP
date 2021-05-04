package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="post_votes")
public class PostVote
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //TODO Ссылка на UserEntity Id
    @Column(name="user_id", nullable = false)
    private int userId;

    //TODO Ссылка на Post Id
    @Column(name="post_id", nullable = false)
    private int postId;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private byte value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
