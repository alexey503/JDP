package main.model.repositories;

import main.model.entities.Post;
import main.model.entities.PostVote;
import main.model.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotesRepository extends CrudRepository<PostVote, Integer> {

    @Query("SELECT v " +
            "FROM PostVote v " +
            "WHERE v.post = :post " +
            "   AND v.user = :user"
    )
    Optional<PostVote> findByPostAndByUser(Post post, User user);

    @Query("SELECT COUNT(v) " +
            "FROM PostVote v " +
            "WHERE v.user.id = :userId" +
            "   AND v.value = 1"
    )
    int getUserLikesCount(int userId);

    @Query("SELECT COUNT(v) " +
            "FROM PostVote v " +
            "WHERE v.user.id = :userId" +
            "   AND v.value = -1"
    )
    int getUserDislikesCount(int userId);

    @Query("SELECT COUNT(v) " +
            "FROM PostVote v " +
            "WHERE v.value = 1"
    )
    int countLikes();

    @Query("SELECT COUNT(v) " +
            "FROM PostVote v " +
            "WHERE v.value = -1"
    )
    int countDislikes();
}
