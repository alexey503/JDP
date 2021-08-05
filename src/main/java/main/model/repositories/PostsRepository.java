package main.model.repositories;

import main.api.response.PostDto;
import main.model.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostsRepository
		extends PagingAndSortingRepository<Post, Integer> {


	@Query("SELECT p " +
			"FROM Post p " +
			//"LEFT JOIN PostComment c ON p.id = :id " +
			"WHERE " +
			//"	p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() AND " +
			"	p.id = :id " +
			"GROUP BY p.id"
	)
	Optional<Post> findById(Integer id);

	@Query("SELECT p.time " +
			"FROM Post p " +
			//"LEFT JOIN PostComment c ON p.id = :id " +
			"WHERE " +
			//"	p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() AND " +
			"	p.id = :id "
			//"GROUP BY p.id"
	)
	long findTimeById(Integer id);
/*
	@Query("SELECT new main.api.response.PostDto(" +
			"        p, " +
//			"		 SUM(CASE WHEN v.value = 1 THEN 1 ELSE 0 END) AS likesCount, " +
			"		 COUNT(DISTINCT v.id) AS likesCount, " +
			"		 COUNT(DISTINCT c.id) AS commentsCount " +
			"    ) " +
			"FROM Post p " +
			"LEFT JOIN PostVote v ON p.id = v.post AND v.value = 1 " +
			"LEFT JOIN PostComment c ON p.id = c.postId " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
			"GROUP BY p.id")
	Page<PostDto> findPostsDto(Pageable pageable);
	*/
	@Query("SELECT p " +
			"FROM Post p " +
			"LEFT JOIN PostVote v ON p.id = v.post AND v.value = 1 " +
			"LEFT JOIN PostComment c ON p.id = c.postId " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"GROUP BY p.id")
	Page<Post> findPostsPage(Pageable pageable);

	@Query("SELECT p " +
			"FROM Post p " +
			"LEFT JOIN PostVote v ON p.id = v.post AND v.value = 1 " +
			"LEFT JOIN PostComment c ON p.id = c.postId " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"GROUP BY p.id " +
			"ORDER BY COUNT(DISTINCT c.id) DESC")
	Page<Post> findPostsPageSortedByCommentsCount(Pageable pageable);


	@Query("SELECT p " +
			"FROM Post p " +
			"LEFT JOIN PostVote v ON p.id = v.post AND v.value = 1 " +
			"LEFT JOIN PostComment c ON p.id = c.postId " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"GROUP BY p.id " +
			"ORDER BY COUNT(DISTINCT v.id) DESC")
	Page<Post> findPostsPageSortedByLikesCount(Pageable pageable);



	//@Query( "SELECT EXTRACT(YEAR FROM p.time) AS YEAR " +
	@Query( "SELECT YEAR(FROM_UNIXTIME(p.time)) AS y " +
			"FROM  Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"GROUP BY y " +
			"ORDER BY y ASC")
	ArrayList<String> findAllYearPublications();

	@Query( "SELECT " +
			" 	FROM_UNIXTIME(p.time, '%Y-%m-%d') AS d, " +
			"	COUNT(p) " +
			"FROM  Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
				"AND YEAR(FROM_UNIXTIME(p.time)) = :year " +
			"GROUP BY d " +
			"ORDER BY d ASC")
	List<String> findCountPostsByDateForYear(Integer year);

	@Query("SELECT p " +
			"FROM Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"AND p.text LIKE %:query% " +
			"ORDER BY p.time DESC")
	Page<Post> postSearchByStringQuery(String query, Pageable pageable);

	@Query("SELECT p " +
			"FROM Post p " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"AND YEAR(FROM_UNIXTIME(p.time)) = :year " +
			"AND MONTH(FROM_UNIXTIME(p.time)) = :month " +
			"AND DAY(FROM_UNIXTIME(p.time)) = :day " +
			"ORDER BY p.time DESC")
	Page<Post> postSearchByDate(Integer year, Integer month, Integer day, Pageable pageable);

	@Query( "SELECT p " +
			"FROM  Post p " +
			"RIGHT JOIN Tag2Post t ON p = t.post " +
			"WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= UNIX_TIMESTAMP() " +
			"AND t.tag.name = :tag " +
			"GROUP BY p.id " +
			"ORDER BY p.time DESC")
	Page<Post> postSearchByTag(String tag, Pageable pageable);

}