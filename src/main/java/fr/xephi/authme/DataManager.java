package fr.xephi.authme;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;

public class DataManager extends Thread {

    public AuthMe plugin;
    public DataSource database;

    public DataManager(AuthMe plugin, DataSource database) {
        this.plugin = plugin;
        this.database = database;
    }

    public void run() {
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer result = null;
        try {
            if (org.bukkit.Bukkit.class.getMethod("getOfflinePlayer", new Class[] { String.class }).isAnnotationPresent(Deprecated.class)) {
                for (OfflinePlayer op : Bukkit.getOfflinePlayers())
                    if (op.getName().equalsIgnoreCase(name)) {
                        result = op;
                        break;
                    }
            } else {
                result = Bukkit.getOfflinePlayer(name);
            }
        } catch (Exception e) {
            result = Bukkit.getOfflinePlayer(name);
        }
        return result;
    }

    public void purgeAntiXray(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            org.bukkit.OfflinePlayer player = getOfflinePlayer(name);
            if (player == null)
                continue;
            String playerName = player.getName();
            File playerFile = new File("." + File.separator + "plugins" + File.separator + "AntiXRayData" + File.separator + "PlayerData" + File.separator + playerName);
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " AntiXRayData Files");
    }

    public void purgeLimitedCreative(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            org.bukkit.OfflinePlayer player = getOfflinePlayer(name);
            if (player == null)
                continue;
            String playerName = player.getName();
            File playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + ".yml");
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
            playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + "_creative.yml");
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
            playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + "_adventure.yml");
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " LimitedCreative Survival, Creative and Adventure files");
    }

    public void purgeDat(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            org.bukkit.OfflinePlayer player = getOfflinePlayer(name);
            if (player == null)
                continue;
            String playerName = player.getName();
            File playerFile = new File(plugin.getServer().getWorldContainer() + File.separator + Settings.defaultWorld + File.separator + "players" + File.separator + playerName + ".dat");
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " .dat Files");
    }

    public void purgeEssentials(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            File playerFile = new File(plugin.ess.getDataFolder() + File.separator + "userdata" + File.separator + name + ".yml");
            if (playerFile.exists()) {
                playerFile.delete();
                i++;
            }
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " EssentialsFiles");
    }

    public UUID getUUID(String name) {
        OfflinePlayer op = getOfflinePlayer(name);
        return op == null ? new UUID(0,0) : op.getUniqueId();
    }
}
