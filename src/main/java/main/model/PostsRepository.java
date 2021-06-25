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

	//TODO
	@Query("SELECT p FROM Posts p WHERE p.status = ?1 and p.name = ?2")
	Page<Post> findAllSpecialQuerySortByPopular(Pageable pageable);

	@Query("SELECT p FROM Posts p WHERE p.status = ?1 and p.name = ?2")
	Page<Post> findAllSpecialQuerySortByBest(Pageable pageable);
	
	
	//Page<Post> findAllByIsActiveAndModerationStatusAndTimeBeforeOrderByPostCommentsSize(byte isActive, ModerationStatus status, Date date, Pageable pageable);

	//List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date);

	//@Query("select u from posts u where u.id = 5")
	/*
	* select  byte isActive = 1
	* moderation status = ModerationStatus.ACCEPTED
	* date < currentDate
	*
	* sorting:
	*	+ recent
		+ early
	* 		popular (commentsCount MAX->MIN)
	*   		best    (like and dislike votes count MAX->MIN)
	* 	
	*
	* */
}
