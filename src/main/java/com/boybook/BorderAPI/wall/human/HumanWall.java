package com.boybook.BorderAPI.wall.human;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.*;
import com.boybook.BorderAPI.Border;
import com.boybook.BorderAPI.BorderAPI;
import com.boybook.BorderAPI.MotionDirection;
import com.boybook.BorderAPI.Utils;
import com.boybook.BorderAPI.wall.BorderWall;

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
public class HumanWall extends BorderWall {

    private final static Skin SKIN = new Skin(BorderAPI.getInstance().getResource("forcefield.png"))
            .setGeometryName("geometry.forcefield")
            .setGeometryData(Utils.readStringFromFile(BorderAPI.getInstance().getResource("forcefield.json")));

    private final UUID uuid = UUID.randomUUID();
    private final long entityId = Entity.entityCount++;

    private double xz, yaw;

    public HumanWall(Border border, MotionDirection direction) {
        super(border, direction);
        this.yaw = direction.getYaw();
        this.updatePosition();
    }

    @Override
    public void onTick(int tick) {

    }

    @Override
    public void onThreadTick(int tick) {
        this.updatePosition();
        for (Player player : this.border.getLevel().getPlayers().values()) {
            boolean needAllTick = false;
            if ((this.direction == MotionDirection.Xx || this.direction == MotionDirection.xX) && Math.max(player.x, this.xz) - Math.max(player.x, this.xz) < 50) needAllTick = true;
            else if ((this.direction == MotionDirection.Zz || this.direction == MotionDirection.zZ) && Math.max(player.z, this.xz) - Math.max(player.z, this.xz) < 50) needAllTick = true;
            if (needAllTick || tick % 20 == 0) this.sendPosition(player);
        }
    }

    private boolean updatePosition() {
        double xz = this.xz;

        switch (this.direction) {
            case Xx:
                xz = border.nowBorder.maxX;
                break;
            case xX:
                xz = border.nowBorder.minX;
                break;
            case Zz:
                xz = border.nowBorder.maxZ;
                break;
            case zZ:
                xz = border.nowBorder.minZ;
                break;
        }

        if (xz != this.xz) {
            this.xz = xz;
            return true;
        } else
            return false;
    }

    @Override
    public void spawnTo(Player player) {
        this.sendSkin(player.getPlayer());

        AddPlayerPacket pk = new AddPlayerPacket();
        pk.uuid = this.uuid;
        pk.username = "";
        pk.entityUniqueId = this.entityId;
        pk.entityRuntimeId = this.entityId;
        pk.x = (float) ((this.direction == MotionDirection.Xx || this.direction == MotionDirection.xX) ? this.xz : player.x);
        pk.y = (float) player.y + 2;
        pk.z = (float) ((this.direction == MotionDirection.Zz || this.direction == MotionDirection.zZ) ? this.xz : player.z);
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = (float) this.yaw;
        pk.headYaw = (float) this.yaw;
        pk.pitch = (float) 0;
        pk.item = Item.get(0);
        pk.metadata = new EntityMetadata()
                .putBoolean(Entity.DATA_ALWAYS_SHOW_NAMETAG, false)
                .putFloat(Entity.DATA_SCALE, 10f)
                .putFloat(Entity.DATA_BOUNDING_BOX_HEIGHT, 0)
                .putFloat(Entity.DATA_BOUNDING_BOX_WIDTH, 0);
        player.dataPacket(pk);
    }

    public void sendSkin(Player player) {
        PlayerListPacket pk1 = new PlayerListPacket();
        pk1.type = PlayerListPacket.TYPE_ADD;
        PlayerListPacket.Entry entry = new PlayerListPacket.Entry(this.uuid, this.entityId, "", SKIN);
        pk1.entries = new PlayerListPacket.Entry[]{entry};
        player.dataPacket(pk1);
        PlayerListPacket pk2 = new PlayerListPacket();
        pk2.type = PlayerListPacket.TYPE_REMOVE;
        pk2.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(this.uuid)};
        Server.getInstance().getScheduler().scheduleDelayedTask(BorderAPI.getInstance(), () -> {
            player.dataPacket(pk2);
        }, 10);
    }

    private void sendPosition(Player player) {
        MovePlayerPacket pk = new MovePlayerPacket();
        pk.eid = this.entityId;
        pk.x = (float) ((this.direction == MotionDirection.Xx || this.direction == MotionDirection.xX) ? this.xz : player.x);
        pk.y = (float) player.y + 2 + 1.62f;
        pk.z = (float) ((this.direction == MotionDirection.Zz || this.direction == MotionDirection.zZ) ? this.xz : player.z);
        pk.yaw = (float) this.yaw;
        pk.pitch = 0;
        pk.headYaw = (float) this.yaw;
        pk.mode = 0;
        player.dataPacket(pk);
    }

    @Override
    public void despawnFrom(Player player) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.entityId;
        player.dataPacket(pk);
    }
}
