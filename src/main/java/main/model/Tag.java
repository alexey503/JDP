package main.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;
    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2post",
            joinColumns = {@JoinColumn(name = "name", nullable = false) },
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Post> name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get(id).getText();
    }

     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}