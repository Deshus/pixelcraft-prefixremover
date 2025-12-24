package ru.pixelcraft.prefixremover;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms не найден! Плагин не будет работать.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("PixelCraft-PrefixRemover включен.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PixelCraft-PrefixRemover отключен.");
    }

    public static Main getInstance() {
        return instance;
    }
}