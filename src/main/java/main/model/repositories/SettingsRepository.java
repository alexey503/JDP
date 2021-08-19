package main.model.repositories;

import main.model.entities.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository
        extends CrudRepository<GlobalSetting, Integer> {
    Optional<GlobalSetting> findByCode(String key);
}
