package main.model.repositories;

import main.model.entities.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository
        extends CrudRepository<GlobalSetting, Integer> {
}
