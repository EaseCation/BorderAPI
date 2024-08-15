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

    public final Map<Level, Border> borders = new HashMap<>();

    public final TickBorderThread tickBorderThread = new TickBorderThread();

    public static BorderAPI getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getServer().getScheduler().scheduleTask(this, tickBorderThread::start);
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
