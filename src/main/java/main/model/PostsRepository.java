package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface PostsRepository
		extends PagingAndSortingRepository<Post, Integer> {
	//Iterable<Post> findAll(Sort sort);

	//Page<Post> findAll(Pageable pageable);

	//Page<Post> findAllByIsActive(byte isActive, Pageable pageable);

	Page<Post> findAllByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date, Pageable pageable);

/*
	@Query(value = "SELECT " +
			"posts.*, " +
			"COUNT(posts.id = post_comments.post_id) AS commentsCount " +
			"FROM  posts  LEFT JOIN post_comments ON posts.id = post_comments.post_id " +
			"WHERE posts.is_active = 1 AND posts.moderation_status = \"ACCEPTED\" AND DATEDIFF(NOW(), posts.time) >= 0 " +
			"GROUP BY id " +
			"ORDER BY commentsCount DESC",
			nativeQuery = true)
*/

	@Query("SELECT p " +
			"FROM Post p " +
			"LEFT JOIN PostComment c ON p.id = c.post " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
			"GROUP BY p.id " +
			"ORDER BY COUNT(c) DESC")
	Page<Post> findAllPopular(Pageable pageable);


/*
@Query(value = "SELECT " +
		"posts.*, CASE WHEN SUM(post_votes.value) is NULL THEN 0 ELSE SUM(post_votes.value) END AS likes " +
		"FROM  posts LEFT JOIN post_votes ON posts.id = post_votes.post_id " +
		"WHERE posts.is_active = 1 AND posts.moderation_status = \"ACCEPTED\" AND DATEDIFF(NOW(), posts.time) >= 0 " +
		"GROUP BY id " +
		"ORDER BY likes DESC",
		nativeQuery = true)
*/

	@Query("SELECT p " +
			"FROM  Post p " +
			"LEFT JOIN PostVote v ON p.id = v.post " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
			"GROUP BY p.id " +
			"ORDER BY SUM(v.value) DESC")

	Page<Post> findAllBest(Pageable pageable);

	@Query( "SELECT EXTRACT(YEAR FROM p.time) AS YEAR " +
			"FROM  Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
			"GROUP BY YEAR " +
			"ORDER BY YEAR ASC")
	ArrayList<String> findAllYearPublications();

	@Query( "SELECT " +
				"EXTRACT(YEAR FROM p.time) " +
				"||'-'||" +
				"EXTRACT(MONTH FROM p.time) " +
				"||'-'||" +
				"EXTRACT(DAY FROM p.time) " +
					"AS DATE, " +

				"COUNT(p) " +

			"FROM  Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
				"AND EXTRACT(YEAR FROM p.time) = :year " +
			"GROUP BY DATE " +
			"ORDER BY DATE ASC")
	List<String> findCountPostsByDateForYear(Integer year);

}
