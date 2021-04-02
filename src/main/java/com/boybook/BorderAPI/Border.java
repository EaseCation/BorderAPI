package com.boybook.BorderAPI;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import com.boybook.BorderAPI.wall.slime.BorderSlimeWall;
import com.boybook.BorderAPI.wall.BorderWall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by funcraft on 2016/2/11.
 */
public class Border {

    private final Level level;

    private final Map<Player, Integer> playerOutTick = new HashMap<>();

    //变量可见性
    public volatile BorderPos nowBorder;
    //改变速度 格/tick
    public double speedMaxX = 0;
    public double speedMinX = 0;
    public double speedMaxZ = 0;
    public double speedMinZ = 0;
    public int changeTick = 0;

    private boolean canEditOut = false;
    private OutDo outDo;
    private boolean reboundPlayer = true;

    private final Map<MotionDirection, BorderWall> wall = new HashMap<>();

    public Border(Level level, BorderPos border) {
        this.level = level;
        this.nowBorder = border;
    }

    public Level getLevel() {
       return this.level;
    }

    public boolean canEditOut() {
        return canEditOut;
    }

    public Border setCanEditOut(boolean canEditOut) {
        this.canEditOut = canEditOut;
        return this;
    }

    public Border setBeDamagedOut(float beDamagedOut) {
        return this.setBeDamagedOut(beDamagedOut, 10);
    }

    public Border setBeDamagedOut(float beDamagedOut, int beDamagedOutTicks) {
        this.outDo = new OutDo.Damage(beDamagedOut, beDamagedOutTicks);
        return this;
    }

    public Border setOutDo(OutDo outDo) {
        this.outDo = outDo;
        return this;
    }

    public boolean canReboundPlayer() {
        return reboundPlayer;
    }

    public Border setReboundPlayer(boolean reboundPlayer) {
        this.reboundPlayer = reboundPlayer;
        return this;
    }

    public Border setCanSee(boolean canSee, Class<? extends BorderWall> clazz) {
        if (!canSee) {
            for (Player player : this.getLevel().getPlayers().values()) {
                this.despawnAllWallsTo(player);
            }
            this.wall.clear();
        } else {
            if (this.wall.size() == 0) {
                Server.getInstance().getLogger().debug("开始可见屏障 " + clazz.getSimpleName());
                //BorderHumanWall Xx = new BorderHumanWall(this, MotionDirection.Xx);
                try {
                    BorderWall Xx = clazz.getConstructor(Border.class, MotionDirection.class).newInstance(this, MotionDirection.Xx);
                    BorderWall xX = clazz.getConstructor(Border.class, MotionDirection.class).newInstance(this, MotionDirection.xX);
                    BorderWall Zz = clazz.getConstructor(Border.class, MotionDirection.class).newInstance(this, MotionDirection.Zz);
                    BorderWall zZ = clazz.getConstructor(Border.class, MotionDirection.class).newInstance(this, MotionDirection.zZ);
                    this.wall.put(MotionDirection.Xx, Xx);
                    this.wall.put(MotionDirection.xX, xX);
                    this.wall.put(MotionDirection.Zz, Zz);
                    this.wall.put(MotionDirection.zZ, zZ);
                    for (Player player : this.getLevel().getPlayers().values()) {
                        this.spawnAllWallsTo(player);
                    }
                } catch (Exception e) {
                    BorderAPI.getInstance().getLogger().alert("创建 " + this.getLevel().getFolderName() + " 地图边界可视化时发生错误！");
                    Server.getInstance().getLogger().logException(e);
                }
            }
        }
        return this;
    }

    public boolean canSee() {
        return this.wall.size() > 0;
    }

    void spawnAllWallsTo(Player player) {
        for (BorderWall wall: this.wall.values()) {
            wall.spawnTo(player);
        }
    }

    void despawnAllWallsTo(Player player) {
        for (BorderWall wall: this.wall.values()) {
            wall.despawnFrom(player);
        }
    }

    //反弹玩家
    double reboundPlayer(Player player, MotionDirection motionDirection) {
        Vector3 motion = new Vector3(0, -0.098, 0);
        Double speed = this.getReboundSpeed(player, motionDirection);
        switch (motionDirection) {
            case Xx:
                motion.x = -speed;
                break;
            case xX:
                motion.x = speed;
                break;
            case Zz:
                motion.z = -speed;
                break;
            case zZ:
                motion.z = speed;
                break;
        }
        //player.setMotion(motion);
        SetEntityMotionPacket pk = new SetEntityMotionPacket();
        pk.eid = player.getId();
        pk.motionX = (float) motion.x;
        pk.motionY = (float) motion.y;
        pk.motionZ = (float) motion.z;
        player.dataPacket(pk);
        player.onGround = true;
        return speed;
    }

    //反弹玩家
    double getReboundSpeed(Vector3 pos, MotionDirection motionDirection) {
        double speed = 0;
        switch (motionDirection) {
            case Xx:
                speed = nowBorder.maxX - pos.x;
                break;
            case xX:
                speed = nowBorder.minX - pos.x;
                break;
            case Zz:
                speed = nowBorder.maxZ - pos.z;
                break;
            case zZ:
                speed = nowBorder.minZ - pos.z;
                break;
        }
        speed = Math.abs(speed);
        speed /= 4;
        if (speed < 0.15) speed = 0.15;
        return speed;
    }

    public Border setNewBorder(BorderPos border) {
        this.nowBorder = border;
        speedMaxX = 0;
        speedMinX = 0;
        speedMaxZ = 0;
        speedMinZ = 0;
        changeTick = 0;
        this.playerOutTick.clear();
        Server.getInstance().getLogger().debug("已设置新边界 " + nowBorder.toString());
        return this;
    }

    public Border setNewBorder(BorderPos border, int changeTick) {
        if (nowBorder == null) {
            nowBorder = new BorderPos(0, 0, 0, 0);
        }
        speedMaxX = (border.maxX - nowBorder.maxX) / changeTick;
        speedMinX = (border.minX - nowBorder.minX) / changeTick;
        speedMaxZ = (border.maxZ - nowBorder.maxZ) / changeTick;
        speedMinZ = (border.minZ - nowBorder.minZ) / changeTick;
        this.changeTick = changeTick;
        this.playerOutTick.clear();
        Server.getInstance().getLogger().debug("已设置新边界 " + nowBorder.toString());
        return this;
    }

    //中心型的正方形边界(PC那种)
    public Border setCentralTypeBorder(Vector2 central, int radius) {
        BorderPos border = new BorderPos(central.x - radius, central.x + radius, central.y - radius, central.y + radius);
        return this.setNewBorder(border);
    }

    public Border setCentralTypeBorder(Vector2 central, int radius, int changeTick) {
        BorderPos border = new BorderPos(central.x - radius, central.x + radius, central.y - radius, central.y + radius);
        return this.setNewBorder(border, changeTick);
    }

    public static BorderPos getCentralTypeBorderPos(Vector2 central, int radius) {
        return new BorderPos(central.x - radius, central.x + radius, central.y - radius, central.y + radius);
    }

    void onThreadTick(int tick) {
        if (nowBorder != null) {
            for (BorderWall wall: this.wall.values()) {
                wall.onThreadTick(tick);
            }
        }
    }

    int getPlayerOutTick(Player player) {
        Integer tick = this.playerOutTick.get(player);
        return tick != null ? tick : 0;
    }

    void onTick(int tick) {
        if (nowBorder != null) {
            for (Player player: this.getLevel().getPlayers().values()) {
                if (this.playerOutTick.containsKey(player)) {
                    if (!player.isOnline() || player.getLevel() != this.level) {
                        this.playerOutTick.remove(player);
                    }
                }
                MotionDirection m = this.nowBorder != null ? this.nowBorder.isOutside(player) : null;
                //Server.getInstance().getLogger().debug("test " + player.getName());
                if (m != null) {
                    if (this.playerOutTick.containsKey(player)) {
                        this.playerOutTick.replace(player, this.playerOutTick.get(player) + 1);
                    } else {
                        this.playerOutTick.put(player, 0);
                    }
                    if ((this.reboundPlayer && this.reboundPlayer(player, m) > 0.3) || (!this.reboundPlayer || this.getPlayerOutTick(player) > 20 * 5)) {
                        if (this.outDo != null) {
                            this.outDo.outDo(player, this.getPlayerOutTick(player));
                        }
                    }
                }
            }
            if (changeTick > 0) {
                nowBorder.maxX += speedMaxX;
                nowBorder.minX += speedMinX;
                nowBorder.maxZ += speedMaxZ;
                nowBorder.minZ += speedMinZ;
                changeTick--;
                if (nowBorder.maxX <= nowBorder.minX || nowBorder.maxZ <= nowBorder.minZ) {
                    changeTick = 0;
                }
            }
            for (BorderWall wall: this.wall.values()) {
                wall.onTick(tick);
            }
        }

    }

}
