package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface PostsRepository
		extends PagingAndSortingRepository<Post, Integer> {
	Iterable<Post> findAll(Sort sort);

	Page<Post> findAll(Pageable pageable);

	Page<Post> findAllByIsActive(byte isActive, Pageable pageable);

	Page<Post> findAllByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date, Pageable pageable);

	//Page<Post> findAllByIsActiveAndModerationStatusAndTimeBeforeOrderByPostCommentsSize(byte isActive, ModerationStatus status, Date date, Pageable pageable);

	//List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date);

	//@Query("select u from posts u where u.id = 5")
	/*
	* select
	* 			byte isActive = 1
	* 			moderation status = ModerationStatus.ACCEPTED
	* 			date < currentDate
	*
SELECT * FROM posts WHERE is_active = 1 AND moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), time) >= 0;


SELECT
	*
FROM
	posts
WHERE
	is_active = 1
	AND moderation_status = "ACCEPTED"
	AND DATEDIFF(NOW(), time) >= 0;



show comments count:
SELECT COUNT(posts.id = post_comments.post_id) AS CommentsCount, posts.id FROM posts JOIN post_comments ON posts.id = post_comments.post_id GROUP BY post_comments.post_id;

add column with comments count
SELECT posts.*, COUNT(posts.id = post_comments.post_id) AS CommentsCount FROM posts LEFT JOIN post_comments ON posts.id = post_comments.post_id GROUP BY id;

sort by commentsCount
SELECT
		posts.id,
		posts.time,
		COUNT(posts.id = post_comments.post_id) AS CommentsCount
FROM
	posts
		LEFT JOIN
			post_comments
		ON
			posts.id = post_comments.post_id
GROUP BY
	id
ORDER BY
	CommentsCount DESC;





column with like votes
SELECT posts.id, posts.time, COUNT(posts.id = post_votes.post_id) AS votes FROM posts LEFT JOIN post_votes ON posts.id = post_votes.post_id GROUP BY id ORDER BY votes DESC;

	*
	* sorting:
	*
	*	recent
	* 		ORDER BY time DESC;
	*
	* 	early
	* 		ORDER BY time ASC;
	*
	* 	popular //by commentsCount
SELECT  posts.id, posts.time,  COUNT(posts.id = post_comments.post_id) AS CommentsCount FROM  posts  LEFT JOIN  post_comments ON posts.id = post_comments.post_id  WHERE posts.is_active = 1 AND posts.moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id ORDER BY CommentsCount DESC;


	*   best //by most likes
SELECT  posts.id, posts.time, CASE WHEN SUM(post_votes.value) is NULL THEN 0 ELSE SUM(post_votes.value) END AS likes FROM  posts LEFT JOIN post_votes ON posts.id = post_votes.post_id WHERE posts.is_active = 1 AND posts.moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id ORDER BY likes DESC;
	*
	*
	* */



	//Page<Post> findAllByTimeBeforeOrderById(Date date, Pageable pageable);

}
/*
	SELECT
		posts.*,
		COUNT(posts.id = post_comments.post_id) AS CommentsCount,
		COUNT(posts.id = post_votes.post_id) AS votes
	FROM
		posts
			LEFT JOIN
				post_comments ON posts.id = post_comments.post_id
	 		LEFT JOIN post_votes ON posts.id = post_votes.post_id
	GROUP BY id;


Added post comments and post votes

SELECT
	posts.id,
	posts.time,
	COUNT(posts.id = post_comments.post_id) AS CommentsCount,
	COUNT(posts.id = post_votes.post_id) AS votes
FROM
	posts
		LEFT JOIN post_comments
			ON posts.id = post_comments.post_id
		LEFT JOIN post_votes
			ON posts.id = post_votes.post_id
GROUP BY id
ORDER BY ;


SELECT
	posts.id,
	posts.time,
	COUNT(posts.id = post_comments.post_id) AS commentsCount,
	COUNT(posts.id = post_votes.post_id) AS votes
FROM posts
	LEFT JOIN  post_comments
		ON posts.id = post_comments.post_id
	LEFT JOIN post_votes
		ON posts.id = post_votes.post_id
WHERE
	posts.is_active = 1 AND
	posts.moderation_status = "ACCEPTED" AND
	DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id;


добавлен столбец commentsCount и фильтрация по параметрам
SELECT  posts.id, posts.time,  COUNT(posts.id = post_comments.post_id) AS CommentsCount FROM  posts  LEFT JOIN  post_comments ON posts.id = post_comments.post_id  WHERE posts.is_active = 1 AND posts.moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id;

добалвены лайки, но выдает ошибку. так же сбилось количество комментов
SELECT  posts.id, posts.time,  COUNT(posts.id = post_comments.post_id) AS CommentsCount, SUM(post_votes.value) FROM  posts  LEFT JOIN  post_comments ON posts.id = post_comments.post_id LEFT JOIN post_votes ON posts.id = post_votes.post_id WHERE posts.is_active = 1 AND posts.moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id;


/*
likes sort best
SELECT  posts.id, posts.time, CASE WHEN SUM(post_votes.value) is NULL THEN 0 ELSE SUM(post_votes.value) END AS likes FROM  posts LEFT JOIN post_votes ON posts.id = post_votes.post_id WHERE posts.is_active = 1 AND posts.moderation_status = "ACCEPTED" AND DATEDIFF(NOW(), posts.time) >= 0 GROUP BY id ORDER BY likes DESC;

 */
