package com.boybook.BorderAPI;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Level;

/**
 * Created by funcraft on 2016/2/11.
 */
public class EventListener implements Listener {

    private final BorderAPI plugin;

    public EventListener(BorderAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLevelLoad(LevelLoadEvent event) {
        Level level = event.getLevel();
        plugin.borders.put(level, new Border(level, null));
    }

    @EventHandler
    public void onLevelUnload(LevelUnloadEvent event) {
        Level level = event.getLevel();
        plugin.borders.remove(level);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom()!= null && event.getTo() != null && event.getFrom().getLevel() != event.getTo().getLevel()) {
                /*
                for (FullChunk chunk: event.getFrom().getLevel().getChunks().values()) {
                    player.unloadChunk(chunk.getX(), chunk.getZ());
                }
                ChangeDimensionPacket pk = new ChangeDimensionPacket();
                pk.dimension = (byte)0;
                pk.x = (float)event.getTo().getX();
                pk.y = (float)event.getTo().getY();
                pk.z = (float)event.getTo().getZ();
                player.dataPacket(pk);
*/
            Level from = event.getFrom().getLevel();
            Level to = event.getTo().getLevel();
            if (plugin.isLevelHasBorder(from)) {
                Border border = plugin.getLevelBorder(from);
                if (border.canSee()) {
                    border.despawnAllWallsTo(player);
                }
            }
            if (plugin.isLevelHasBorder(to)) {
                Border border = plugin.getLevelBorder(to);
                if (border.canSee()) {
                    border.spawnAllWallsTo(player);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Level level = event.getPlayer().getLevel();
        if (plugin.isLevelHasBorder(level)) {
            Border border = plugin.getLevelBorder(level);
            if (border.canSee()) {
                border.spawnAllWallsTo(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.isLevelHasBorder(player.getLevel())) {
            Border border = this.plugin.getLevelBorder(player.getLevel());
            if (!border.canEditOut() && border.nowBorder != null && border.nowBorder.isOutside(event.getBlock()) != null) event.setCancelled();
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.isLevelHasBorder(player.getLevel())) {
            Border border = this.plugin.getLevelBorder(player.getLevel());
            if (!border.canEditOut() && border.nowBorder != null && border.nowBorder.isOutside(event.getBlock()) != null) event.setCancelled();
        }
    }

}
