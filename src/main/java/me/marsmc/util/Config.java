package me.marsmc.util;

import lombok.Getter;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    @Getter
    private static YamlConfiguration instance;

    public static void setInstance(YamlConfiguration config) {
        instance = config;
    }

    public static void load(InputStream inputStream) {
        if (instance == null) {
            instance = new YamlConfiguration();
        }
        instance.setDefaults(loadYaml(inputStream));
    }

    private static Configuration loadYaml(InputStream inputStream) {
        Yaml yaml = new Yaml();
        return yaml.load(new InputStreamReader(inputStream));
    }

    public static String getString(String path) {
        return instance.getString(path);
    }

    public static int getInt(String path) {
        return instance.getInt(path);
    }
}