package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="post_comments")
public class PostComment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("timestamp")
    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, length = 65535,columnDefinition="Text")
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userEntity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id")
    private Post parent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public long getTime() {
        return time.getTime()/1000;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
