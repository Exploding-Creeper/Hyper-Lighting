package me.hypherionmc.hyperlighting.common.config;

import me.hypherionmc.nightconfig.core.Config;
import me.hypherionmc.nightconfig.core.conversion.ObjectConverter;
import me.hypherionmc.nightconfig.core.file.CommentedFileConfig;

import java.io.File;

public class ConfigHandler {

    public static HyperLightingConfig initConfig() {
        CommentedFileConfig config = CommentedFileConfig.builder("config/hyperlighting.toml").build();
        ObjectConverter converter = new ObjectConverter();
        if (!new File("config/hyperlighting.toml").exists()) {
            saveConfig(new HyperLightingConfig());
        }
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

}
