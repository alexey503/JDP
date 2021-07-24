package main.api.response;

import javax.persistence.*;

@Entity
@Table(name="users")
public class PostUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
            return this.id;
    }

    public String getName() {
            return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
