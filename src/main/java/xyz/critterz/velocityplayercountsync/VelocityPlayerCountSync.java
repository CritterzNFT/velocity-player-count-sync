package xyz.critterz.velocityplayercountsync;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.UUID;

@Plugin(
        id = "velocity-player-count-sync",
        name = "velocity-player-count-sync",
        version = "1.0.0-SNAPSHOT",
        description = "Sync the player count across your multiple velocity proxies",
        authors = {"PureGero"}
)
public class VelocityPlayerCountSync {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataFolder;
    private final UUID uuid;
    private RedisManager redisManager;
    private int totalPlayerCount = 0;

    @Inject
    public VelocityPlayerCountSync(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.uuid = UUID.randomUUID();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.redisManager = new RedisManager(this);

        new PlayerCountSyncTask(this);

        logger.info("VelocityPlayerCountSync has been enabled!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        redisManager.shutdown();
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        event.setPing(event.getPing().asBuilder().onlinePlayers(totalPlayerCount).build());
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataFolder() {
        return dataFolder;
    }

    public UUID getUuid() {
        return uuid;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setTotalPlayerCount(int totalPlayerCount) {
        this.totalPlayerCount = totalPlayerCount;
    }
}
