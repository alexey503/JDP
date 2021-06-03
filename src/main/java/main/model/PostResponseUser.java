package main.model;

import javax.persistence.*;

@Entity
@Table(name="users")
public class PostResponseUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false)
        private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
