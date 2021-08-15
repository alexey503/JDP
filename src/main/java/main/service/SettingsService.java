package main.service;

import main.model.entities.GlobalSetting;
import main.model.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsService {

    public static final String KEY_STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";

    private Map<String, Boolean> settings;

    @Autowired
    private SettingsRepository repository;

    public Map<String, Boolean> getGlobalSettings(){
        if(settings == null){
            settings = new HashMap<>();
            Iterable<GlobalSetting> settingsIterable = repository.findAll();
            for (GlobalSetting setting : settingsIterable) {
                settings.put(setting.getCode(), setting.getValue().equals("YES"));
            }
        }
        return settings;
    }
}
