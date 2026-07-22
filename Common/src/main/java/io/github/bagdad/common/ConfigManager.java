package io.github.bagdad.common;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ConfigManager {

    private static ConfigManager instance = null;

    private final Map<Class<?>, Object> configs;

    private ConfigManager() {
        this.configs = new HashMap<>();
    }

    private static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public static <T> void loadConfig(String filepath, Class<T> configClass) {
        log.info("Loading config from \"{}\"", filepath);

        Optional<T> loadedConfig = JsonConverter.convertToObject(Paths.get(filepath), configClass);
        loadedConfig.ifPresent(t -> ConfigManager.getInstance().configs.put(configClass, t));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> configClass) {
        log.info("Getting config {}", configClass);

        return (T) getInstance().configs.get(configClass);
    }

}