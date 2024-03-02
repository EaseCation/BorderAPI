package com.boybook.BorderAPI;

import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;

import java.util.HashMap;
import java.util.Map;

/**
 * EaseCation Team - boybook
 */
public class BorderAPI extends PluginBase {

    public static BorderAPI instance;

    public Map<Level, Border> borders = new HashMap<>();

    public TickBorderThread tickBorderThread = new TickBorderThread();

    public static BorderAPI getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getScheduler().scheduleAsyncTask(this, tickBorderThread);
        getServer().getScheduler().scheduleRepeatingTask(this, new TickBirderRunnable(), 1);
        getLogger().info("BorderAPI by boybook Enabled!");
    }

    public Border getLevelBorder(Level level) {
        if (borders.containsKey(level)) {
            return borders.get(level);
        } else {
            Border border = new Border(level, null);
            borders.put(level, border);
            return border;
        }
    }

    public boolean isLevelHasBorder(Level level) {
        return borders.containsKey(level) && borders.get(level).nowBorder != null;
    }

}
