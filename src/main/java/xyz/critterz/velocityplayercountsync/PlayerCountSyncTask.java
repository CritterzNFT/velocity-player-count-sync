package xyz.critterz.velocityplayercountsync;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerCountSyncTask implements Runnable {

    private final VelocityPlayerCountSync plugin;

    public PlayerCountSyncTask(VelocityPlayerCountSync plugin) {
        this.plugin = plugin;

        plugin.getServer().getScheduler()
                .buildTask(plugin, this)
                .delay((long) (5000 + Math.random() * 1000), TimeUnit.MILLISECONDS)
                .repeat((long) (5000 + Math.random() * 1000), TimeUnit.MILLISECONDS)
                .schedule();
    }

    public void run() {
        int myPlayerCount = plugin.getServer().getPlayerCount();
        int totalPlayerCount = 0;

        Map<UUID, ServerPlayerCount> onlinePlayerCounts = plugin.getRedisManager().getRedissonClient().getMap("onlinePlayerCounts");
        onlinePlayerCounts.put(plugin.getUuid(), new ServerPlayerCount(myPlayerCount, System.currentTimeMillis()));

        for (Map.Entry<UUID, ServerPlayerCount> entry : onlinePlayerCounts.entrySet()) {
            if (entry.getValue().getLastOnline() < System.currentTimeMillis() - 60 * 1000) {
                // Old entry, remove
                onlinePlayerCounts.remove(entry.getKey());
                continue;
            }

            if (entry.getValue().getLastOnline() > System.currentTimeMillis() - 15 * 1000) {
                // Server is online
                totalPlayerCount += entry.getValue().getPlayerCount();
            }
        }

        plugin.setTotalPlayerCount(totalPlayerCount);
    }
}
