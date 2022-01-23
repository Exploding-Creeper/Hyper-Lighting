package me.hypherionmc.hyperlighting.common.config;

import me.hypherionmc.nightconfig.core.CommentedConfig;
import me.hypherionmc.nightconfig.core.Config;
import me.hypherionmc.nightconfig.core.conversion.ObjectConverter;
import me.hypherionmc.nightconfig.core.file.CommentedFileConfig;

import java.io.File;

public class ConfigHandler {

    private static final int configVer = 1;

    public static HyperLightingConfig initConfig() {
        CommentedFileConfig config = CommentedFileConfig.builder("config/hyperlighting.toml").build();
        ObjectConverter converter = new ObjectConverter();
        if (!new File("config/hyperlighting.toml").exists()) {
            saveConfig(new HyperLightingConfig());
        }
        upgradeConfig();
        config.load();
        return converter.toObject(config, HyperLightingConfig::new);
    }

    public static void saveConfig(HyperLightingConfig config) {
        Config.setInsertionOrderPreserved(true);
        ObjectConverter converter = new ObjectConverter();
        CommentedFileConfig conf = CommentedFileConfig.builder("config/hyperlighting.toml").build();

        converter.toConfig(config, conf);
        conf.save();
    }

    private static void upgradeConfig() {
        File configPath = new File("config/hyperlighting.toml");
        Config.setInsertionOrderPreserved(true);

        CommentedFileConfig oldConfig = CommentedFileConfig.builder(configPath).build();
        CommentedFileConfig newConfig = CommentedFileConfig.builder(configPath).build();

        newConfig.load();
        newConfig.clear();
        oldConfig.load();

        if (!oldConfig.contains("configVersion") || oldConfig.getInt("configVersion") != configVer) {

            ObjectConverter objectConverter = new ObjectConverter();
            objectConverter.toConfig(new HyperLightingConfig(), newConfig);

            oldConfig.valueMap().forEach((key, value) -> {
                if (value instanceof CommentedConfig commentedConfig) {
                    commentedConfig.valueMap().forEach((subKey, subValue) -> {
                        newConfig.set(key + "." + subKey, subValue);
                    });
                } else {
                    newConfig.set(key, value);
                }
            });

            boolean ignored = configPath.renameTo(new File(configPath.getAbsolutePath().replace(".toml", ".bak")));
            newConfig.set("configVersion", configVer);
            newConfig.save();
            newConfig.close();
            oldConfig.close();
        }
    }

}
