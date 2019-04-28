package com.boybook.BorderAPI.wall.particle;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.particle.*;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import com.boybook.BorderAPI.Border;
import com.boybook.BorderAPI.MotionDirection;
import com.boybook.BorderAPI.wall.BorderWall;
import com.boybook.BorderAPI.wall.slime.SlimeWallSlime;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * com.boybook.BorderAPI.wall.particle
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public class BorderParticleWall extends BorderWall {

    private boolean offset = false;
    private Queue<Map<Player, List<DataPacket>>> sendQueue = new LinkedBlockingQueue<>();

    public BorderParticleWall(Border border, MotionDirection direction) {
        super(border, direction);
    }

    @Override
    public void onTick(int tick) {
        Map<Player, List<DataPacket>> packets;
        while ((packets = this.sendQueue.poll()) != null) {
            packets.forEach((player, pks) -> pks.forEach(pk -> player.batchDataPacket(pk)));
        }
    }

    @Override
    public void onThreadTick(int tick) {
        if (tick % 10 == 0) {
            double x;
            double z;
            double y;
            Vector3 v3 = new Vector3();
            Map<Player, List<DataPacket>> packets = new HashMap<>();
            switch (this.direction) {
                case Xx:
                    x = border.nowBorder.maxX;
                    z = border.nowBorder.minZ;
                    while (z < border.nowBorder.maxZ) {
                        //Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                        for (Player player: this.border.getLevel().getPlayers().values()) {
                            if (!packets.containsKey(player)) packets.put(player, new ArrayList<>());
                            if (new Vector2(player.getX(), player.getZ()).distanceSquared(x, z) > 1200) continue;
                            List<DataPacket> ps = packets.get(player);
                            for (y = -10; y < 20; y++) {
                                v3.setComponents(x, player.getFloorY() + (offset ? y + 0.5 : y), this.offset ? z + 0.5 : z);
                                ps.addAll(Arrays.asList(new FlameParticle(v3).encode()));
                            }
                        }
                        z += 1;
                    }
                    break;
                case xX:
                    x = border.nowBorder.minX;
                    z = border.nowBorder.minZ;
                    //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                    while (z < border.nowBorder.maxZ) {
                        //Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                        for (Player player: this.border.getLevel().getPlayers().values()) {
                            if (!packets.containsKey(player)) packets.put(player, new ArrayList<>());
                            if (new Vector2(player.getX(), player.getZ()).distanceSquared(x, z) > 1200) continue;
                            List<DataPacket> ps = packets.get(player);
                            for (y = -10; y < 20; y++) {
                                v3.setComponents(x, player.getFloorY() + (offset ? y + 0.5 : y), this.offset ? z + 0.5 : z);
                                ps.addAll(Arrays.asList(new FlameParticle(v3).encode()));
                            }
                        }
                        z += 1;
                    }
                    break;
                case Zz:
                    z = border.nowBorder.maxZ;
                    x = border.nowBorder.minX;
                    //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                    while (x < border.nowBorder.maxX) {
                        //Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                        for (Player player: this.border.getLevel().getPlayers().values()) {
                            if (!packets.containsKey(player)) packets.put(player, new ArrayList<>());
                            if (new Vector2(player.getX(), player.getZ()).distanceSquared(x, z) > 1200) continue;
                            List<DataPacket> ps = packets.get(player);
                            for (y = -10; y < 20; y++) {
                                v3.setComponents(this.offset ? x + 0.5 : x, player.getFloorY() + (offset ? y + 0.5 : y), z);
                                ps.addAll(Arrays.asList(new FlameParticle(v3).encode()));
                            }
                        }
                        x += 1;
                    }
                    break;
                case zZ:
                    z = border.nowBorder.minZ;
                    x = border.nowBorder.minX;
                    //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                    while (x < border.nowBorder.maxX) {
                        //Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                        for (Player player: this.border.getLevel().getPlayers().values()) {
                            if (!packets.containsKey(player)) packets.put(player, new ArrayList<>());
                            if (new Vector2(player.getX(), player.getZ()).distanceSquared(x, z) > 1200) continue;
                            List<DataPacket> ps = packets.get(player);
                            for (y = -10; y < 20; y++) {
                                v3.setComponents(this.offset ? x + 0.5 : x, player.getFloorY() + (offset ? y + 0.5 : y), z);
                                ps.addAll(Arrays.asList(new FlameParticle(v3).encode()));
                            }
                        }
                        x += 1;
                    }
                    break;
            }

            this.sendQueue.offer(packets);
            this.offset = !this.offset;
        }
    }

    @Override
    public void spawnTo(Player player) {

    }

    @Override
    public void despawnFrom(Player player) {

    }
}
