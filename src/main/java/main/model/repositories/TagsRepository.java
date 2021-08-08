package main.model.repositories;

import main.model.entities.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository
        extends CrudRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);
}
