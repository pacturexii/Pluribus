package cc.mcns.pluribus;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Pluribus extends Plugin {
    private Configuration config = null;
    private HashMap<UUID, String> players = null;

    public synchronized String getPlayerName(UUID uuid) {
        if (players == null)
            return null;
        return players.get(uuid);
    }

    @Override
    public void onEnable() {
        String player_cfg_name = "players.yml";
        File player_cfg = new File(getDataFolder(), player_cfg_name);
        if (!player_cfg.exists()) {
            getLogger().info("Config file \"" + player_cfg_name + "\" does not exist; creating file...");
            try {
                if (!player_cfg.createNewFile()) {
                    getLogger().severe("Could not create config file \"" + player_cfg_name + "\"");
                }
                getLogger().info("Config file \"" + player_cfg_name + "\" created successfully");
                players = new HashMap<>();
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("Could not create config file \"" + player_cfg_name + "\"!");
            }
            return;
        }
        else if (!player_cfg.canWrite()) {
            getLogger().severe("Insufficient permissions to write to config file \" + player_cfg_name + \"");
            return;
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(player_cfg);
            Configuration player_list = config.getSection("players");
            players = new HashMap<>();
            for (String uuid : player_list.getKeys())
                players.put(UUID.fromString(uuid), player_list.getString(uuid));
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Could not read config file \"" + player_cfg_name + "\"");
        }
    }
}