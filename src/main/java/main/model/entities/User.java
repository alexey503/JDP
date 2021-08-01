package main.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import main.model.Role;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="users")
@Data
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @Column(name="is_moderator", nullable = false)
    private byte isModerator;

    @JsonIgnore
    @Column(nullable = false)
    private Date reg_time;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    private String code;

    @Column(length = 65535,columnDefinition="Text")
    private String photo;

    public Role getRole(){
        return isModerator == 1 ? Role.MODERATOR : Role.USER;
    }

}
