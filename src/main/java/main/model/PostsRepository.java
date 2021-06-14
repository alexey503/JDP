package main.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface PostsRepository
		extends PagingAndSortingRepository<Post, Integer> {
	Iterable<Post> findAll(Sort sort);

	Page<Post> findAll(Pageable pageable);

	Page<Post> findAllByIsActive(byte isActive, Pageable pageable);

	Page<Post> findAllByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date, Pageable pageable);

	List<Post> findByIsActiveAndModerationStatusAndTimeBefore(byte isActive, ModerationStatus status, Date date);

	Page<Post> findAllByTimeBefore(Date date, Pageable pageable);

}