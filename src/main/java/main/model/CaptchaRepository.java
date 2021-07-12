package main.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {

    Optional<Captcha> findByCode(String code);
}

