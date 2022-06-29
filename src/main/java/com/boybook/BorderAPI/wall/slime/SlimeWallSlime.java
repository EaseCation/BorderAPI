package com.boybook.BorderAPI.wall.slime;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.*;
import com.boybook.BorderAPI.MotionDirection;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by funcraft on 2016/2/13.
 */
public class SlimeWallSlime {

    public long id;

    public double x;
    public double y;
    public double z;
    public double yaw;

    public long getId() {
        return id;
    }

    public SlimeWallSlime(Vector3 pos, double yaw) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        this.yaw = yaw;
//        this.id = 1095216660480L + ThreadLocalRandom.current().nextLong(0L, 2147483647L);
        this.id = Entity.entityCount++;
    }

    public DataPacket spawnTo() {
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId = this.getId();
        pk.entityUniqueId = this.getId();
        pk.type = 37;
        pk.x = (float)this.x;
        pk.y = (float)this.y;
        pk.z = (float)this.z;
        pk.speedX = 0f;
        pk.speedY = 0f;
        pk.speedZ = 0f;
        pk.yaw = (float)this.yaw;
        pk.headYaw = (float)this.yaw;
        pk.pitch = 0f;
        pk.metadata = new EntityMetadata();
        /*
        pk.metadata.putByte(0, 32);
        pk.metadata.putBoolean(3, true);
        */
        pk.metadata.putBoolean(15, true);
        pk.metadata.putByte(16, 20);
        return pk;
    }

    public DataPacket despawnFrom() {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.getId();
        return pk;
    }

}
