package main.model.repositories;

import main.model.Captcha;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {

    Optional<Captcha> findBySecretCode(String secretCode);

    @Transactional
    @Modifying
    @Query("DELETE FROM Captcha c WHERE c.time < :time")
    void deleteByTimeBefore(Date time);
}

