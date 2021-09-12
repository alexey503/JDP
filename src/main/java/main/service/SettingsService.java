package main.service;

import main.api.response.PostDataResponse;
import main.model.entities.GlobalSetting;
import main.model.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SettingsService {

    public static final String KEY_STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";
    public static final String KEY_MULTIUSER_MODE = "MULTIUSER_MODE";
    public static final String KEY_POST_PREMODERATION = "POST_PREMODERATION";

    private Map<String, Boolean> settings;

    @Autowired
    private SettingsRepository settingsRepository;

    public Map<String, Boolean> getGlobalSettings() {
        if (settings == null) {
            settings = new HashMap<>();
            Iterable<GlobalSetting> settingsIterable = settingsRepository.findAll();
            for (GlobalSetting setting : settingsIterable) {
                settings.put(setting.getCode(), setting.getValue().equals("YES"));
            }
        }
        return settings;
    }

    public PostDataResponse saveGlobalSettings(Map<String, Boolean> paramsMap) {
        this.settings = paramsMap;
        for (String key : settings.keySet()) {
            Optional<GlobalSetting> optionalSetting = settingsRepository.findByCode(key);
            GlobalSetting newSetting;
            if (optionalSetting.isEmpty()) {
                newSetting = new GlobalSetting();
                newSetting.setCode(key);
            } else {
                newSetting = optionalSetting.get();
            }
            newSetting.setValue(settings.get(key) ? "YES" : "NO");
            settingsRepository.save(newSetting);
        }
        return new PostDataResponse();
    }

    public boolean getSettingValue(String key) {
        if (this.settings != null) {
            return this.settings.get(key);
        } else {
            return false;
        }
    }
}
