package main.api.response;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="users")
public class PostUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

}
