package main.model;

import liquibase.pro.packaged.T;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface PostsRepository
		extends CrudRepository<Post, Integer> {
}
/*
@Repository
public interface PostsRepository
        extends PagingAndSortingRepository<Post, Integer> {

	//List<Post> findAllByIsActive(byte isActive, Pageable pageable);

	//List<Post> findAllBy(Post post);

	//<S extends Post> Iterable<Post> findAll(Example<Post> example);
	//<S extends T> Iterable<S> findAll(Example<S> example);
	//Iterable<Post> findAll(Example<Post> example);

	@Query("select a from posts a where a.time <= :time")
	List<Post> findAllWithTimeBefore(@Param("time") long time);

// @Query("select * from posts where is_active = :isActive and moderation_status = :status and time < :timestamp")
    //List<Post> findAllByDate(Date date, Pageable pageable);

    //@Query("select * from posts where is_active = :isActive and moderation_status = :status and time < :timestamp")
	/*
	@Query("select * from posts where is_active = 1 and moderation_status = \"accepted\" and time < \'2021-05-08 02:00:00\'")
	List<Post> findAllPost(@Param("id") byte isActive,
					   @Param("status") ModerationStatus status,
					   @Param("timestamp") long timestamp,
					   Pageable pageable);
*/
/*
isActive == 1 
ModerationStatus == ModerationStatus.ACCEPTED
Time() < (new Date().getTime())) {

*/
/*
}

 */
