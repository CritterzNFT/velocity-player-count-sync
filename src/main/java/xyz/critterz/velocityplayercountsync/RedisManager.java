package xyz.critterz.velocityplayercountsync;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RedisManager {

    private final VelocityPlayerCountSync plugin;
    private RedissonClient redissonClient;

    public RedisManager(VelocityPlayerCountSync plugin) {
        this.plugin = plugin;

        if (!Files.exists(getConfigFile())) {
            loadDefaultConfig();
        }

        try {
            Config redisConfig = Config.fromYAML(getConfigFile().toFile());
            redisConfig.setCodec(JsonJacksonCodec.INSTANCE);

            this.redissonClient = Redisson.create(redisConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getConfigFile() {
        return plugin.getDataFolder().resolve("redis.yml");
    }

    private void loadDefaultConfig() {
        try (InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream("redis.yml")) {
            assert inputStream != null;
            Files.createDirectories(getConfigFile().getParent());
            Files.copy(inputStream, getConfigFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        this.redissonClient.shutdown();
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
