package main.model.repositories;

import main.model.entities.PostVote;
import org.springframework.data.repository.CrudRepository;

public class VotesRepository extends CrudRepository<PostVote, Integer> {
    Optional<PostVote> findByPostAndByUser(Post post, User user);
}
