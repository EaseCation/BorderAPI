package com.boybook.BorderAPI.wall.slime;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import com.boybook.BorderAPI.Border;
import com.boybook.BorderAPI.MotionDirection;
import com.boybook.BorderAPI.wall.BorderWall;

import java.util.ArrayList;
import java.util.List;

/**
 * 人墙大法好
 */
public class BorderSlimeWall extends BorderWall {

    public List<SlimeWallSlime> pool = new ArrayList<>();

    public List<DataPacket> spawnDataPacketsCache = new ArrayList<>();
    public List<DataPacket> despawnDataPacketsCache = new ArrayList<>();

    public BorderSlimeWall(Border border, MotionDirection direction) {
        super(border, direction);
        loadAll();
        cacheSpawnPackets();
        cacheDespawnPackets();
    }

    public void loadAll() {
        Server.getInstance().getLogger().debug("开始获取所有史莱姆对象");
        double x;
        double z;
        double y;
        switch (direction) {
            case Xx:
                x = border.nowBorder.maxX + 5;
                z = border.nowBorder.minZ;
                Server.getInstance().getLogger().debug(border.nowBorder.toString());
                while (z < border.nowBorder.maxZ) {
                    Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                    for (y = 0; y < 10; y+=10) {
                        pool.add(new SlimeWallSlime(new Vector3(x, y, z),
                                direction.getYaw())
                        );
                        Server.getInstance().getLogger().debug("已添加一个史莱姆 " + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
                    }
                    z += 10;
                }
                break;
            case xX:
                x = border.nowBorder.minX - 5;
                z = border.nowBorder.minZ;
                Server.getInstance().getLogger().debug(border.nowBorder.toString());
                while (z < border.nowBorder.maxZ) {
                    Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                    for (y = 0; y < 10; y+=10) {
                        pool.add(new SlimeWallSlime(new Vector3(x, y, z),
                                direction.getYaw())
                        );
                        Server.getInstance().getLogger().debug("已添加一个史莱姆 " + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
                    }
                    z += 10;
                }
                break;
            case Zz:
                z = border.nowBorder.maxZ + 5;
                x = border.nowBorder.minX;
                Server.getInstance().getLogger().debug(border.nowBorder.toString());
                while (x < border.nowBorder.maxX) {
                    Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                    for (y = 0; y < 10; y+=10) {
                        pool.add(new SlimeWallSlime(new Vector3(x, y, z),
                                        direction.getYaw())
                        );
                        Server.getInstance().getLogger().debug("已添加一个史莱姆 " + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
                    }
                    x += 10;
                }
                break;
            case zZ:
                z = border.nowBorder.minZ - 5;
                x = border.nowBorder.minX;
                Server.getInstance().getLogger().debug(border.nowBorder.toString());
                while (x < border.nowBorder.maxX) {
                    Server.getInstance().getLogger().debug("X: " + String.valueOf(x) + " Z: " + String.valueOf(z));
                    for (y = 0; y < 10; y+=10) {
                        pool.add(new SlimeWallSlime(new Vector3(x, y, z),
                                        direction.getYaw())
                        );
                        Server.getInstance().getLogger().debug("已添加一个史莱姆 " + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
                    }
                    x += 10;
                }
                break;
        }
    }

    @Override
    public void onThreadTick(int tick) {
        if (tick % 20 == 0) {
            if (this.border.canSee()) {
                this.updateAll();
            }
        }
        if (tick % 200 == 0) {
            if (this.border.canSee()) {
                if (this.border.changeTick > 0) {
                    this.cacheSpawnPackets();
                    this.cacheDespawnPackets();
                }

            }

        }

    }

    public void updateAll() {
        //todo
        updateAllSlimes();
        for (Player player: this.border.getLevel().getPlayers().values()) {
            updatePosTo(player);
        }

    }

    public void updateAllSlimes() {
        double x;
        double z;
        switch (direction) {
            case Xx:
                x = border.nowBorder.maxX + 5;
                //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                for (SlimeWallSlime s: this.pool) {
                    if (s.z < border.nowBorder.minZ || s.z > border.nowBorder.maxZ) {
                        sendPacketToAllPlayers(s.despawnFrom());
                        this.pool.remove(s);
                    }
                    s.x = x;
                }
                break;
            case xX:
                x = border.nowBorder.minX - 5;
                //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                for (SlimeWallSlime s: this.pool) {
                    if (s.z < border.nowBorder.minZ || s.z > border.nowBorder.maxZ) {
                        sendPacketToAllPlayers(s.despawnFrom());
                        this.pool.remove(s);
                    }
                    s.x = x;
                }
                break;
            case Zz:
                z = border.nowBorder.maxZ + 5;
                //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                for (SlimeWallSlime s: this.pool) {
                    if (s.x < border.nowBorder.minX || s.x > border.nowBorder.maxX) {
                        sendPacketToAllPlayers(s.despawnFrom());
                        this.pool.remove(s);
                    }
                    s.z = z;
                }
                break;
            case zZ:
                z = border.nowBorder.minZ - 5;
                //Server.getInstance().getLogger().debug(border.nowBorder.toString());
                for (SlimeWallSlime s: this.pool) {
                    if (s.x < border.nowBorder.minX || s.x > border.nowBorder.maxX) {
                        sendPacketToAllPlayers(s.despawnFrom());
                        this.pool.remove(s);
                    } else {
                        s.z = z;
                    }

                }
                break;
        }
    }

    public void sendPacketToAllPlayers(DataPacket[] pks) {
        for (DataPacket pk: pks) {
            this.sendPacketToAllPlayers(pk);
        }
    }

    public void sendPacketToAllPlayers(DataPacket pk) {
        for (Player player: border.getLevel().getPlayers().values()) {
            player.dataPacket(pk);
        }
    }

    public void updatePosTo(Player player) {
        for (SlimeWallSlime s: this.pool) {
            MoveEntityPacket pk = new MoveEntityPacket();
            pk.eid = s.getId();
            pk.x = s.x;
            pk.y = s.y + player.getY() - 5;
            pk.z = s.z;
            pk.yaw = s.yaw;
            pk.headYaw = s.yaw;
            pk.pitch = 0;
            player.dataPacket(pk);
        }
    }

    public void sendMotionToAll() {
        SetEntityMotionPacket[] pk = new SetEntityMotionPacket[this.pool.size()];
        int i = 0;
        double x = 0;
        double z = 0;
        switch (direction) {
            case Xx:
                x = border.speedMaxX;
                break;
            case xX:
                x = border.speedMinX;
                break;
            case Zz:
                z = border.speedMaxZ;
                break;
            case zZ:
                z = border.speedMinZ;
        }
        for (SlimeWallSlime s: this.pool) {
            pk[i] = new SetEntityMotionPacket();
            pk[i].eid = s.getId();
            pk[i].motionX = (float) x;
            pk[i].motionY = 0;
            pk[i].motionZ = (float) z;
            i++;
        }
        sendPacketToAllPlayers(pk);
    }

    public void cacheSpawnPackets() {
        this.spawnDataPacketsCache.clear();
        for (SlimeWallSlime s: this.pool) {
            spawnDataPacketsCache.add(s.spawnTo());
        }
    }

    public void cacheDespawnPackets() {
        this.despawnDataPacketsCache.clear();
        for (SlimeWallSlime s: this.pool) {
            despawnDataPacketsCache.add(s.despawnFrom());
        }
    }

    @Override
    public void spawnTo(Player player) {
        for (DataPacket pk: this.spawnDataPacketsCache) {
            player.directDataPacket(pk);
            //Server.getInstance().getLogger().debug(pk.toString());
        }
    }

    @Override
    public void despawnFrom(Player player) {
        for (DataPacket pk: this.despawnDataPacketsCache) {
            player.directDataPacket(pk);
            //Server.getInstance().getLogger().debug(pk.toString());
        }
    }

    @Override
    public void onTick(int tick) {

    }
}
