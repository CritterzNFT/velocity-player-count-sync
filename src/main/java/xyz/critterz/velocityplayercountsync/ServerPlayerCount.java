package xyz.critterz.velocityplayercountsync;

public class ServerPlayerCount {

    private int playerCount;
    private long lastOnline;

    public ServerPlayerCount() {
    }

    public ServerPlayerCount(int playerCount, long lastOnline) {
        this.playerCount = playerCount;
        this.lastOnline = lastOnline;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }
}
