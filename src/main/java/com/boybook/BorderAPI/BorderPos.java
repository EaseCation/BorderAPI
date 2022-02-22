package com.boybook.BorderAPI;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;

/**
 * Created by funcraft on 2016/2/11.
 */
public class BorderPos {

    public double minX;
    public double maxX;

    public double minZ;
    public double maxZ;

    public BorderPos(double minX, double maxX, double minZ, double maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public BorderPos grow(double x, double z) {
        return new BorderPos(this.minX - x, this.maxX + x, this.minZ - z, this.maxZ + z);
    }

    public Vector2 getMiddle() {
        return new Vector2(this.minX + ((this.maxX - this.minX) / 2), this.minZ + ((this.maxZ - this.minZ) / 2));
        //return new Vector2((this.maxX - this.minX) / 2, (this.maxZ - this.minZ) / 2);
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public MotionDirection isOutside(Vector3 pos) {
        if (pos.x >= this.maxX) return MotionDirection.Xx;
        if (pos.x < this.minX) return MotionDirection.xX;
        if (pos.z >= this.maxZ) return MotionDirection.Zz;
        if (pos.z < this.minZ) return MotionDirection.zZ;
        return null;
    }

    public boolean isOutSideBool(Vector3 pos) {
        return isOutside(pos) != null;
    }

    public boolean isInside(Vector3 pos) {
        return !isOutSideBool(pos);
    }

    public MotionDirection isBlockOutside(Vector3 pos) {
        if (pos.x >= (int) this.maxX) return MotionDirection.Xx;
        if (pos.x < (int) this.minX) return MotionDirection.xX;
        if (pos.z >= (int) this.maxZ) return MotionDirection.Zz;
        if (pos.z < (int) this.minZ) return MotionDirection.zZ;
        return null;
    }

    public boolean isBlockOutSideBool(Vector3 pos) {
        return isBlockOutside(pos) != null;
    }

    public boolean isBlockInside(Vector3 pos) {
        return !isBlockOutSideBool(pos);
    }

    public String toString() {
        return "minX=" + minX + " " +
                "maxX=" + maxX + " " +
                "minZ=" + minZ + " " +
                "maxZ=" + maxZ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BorderPos) {
            BorderPos b = (BorderPos) obj;
            return this.minX == b.minX && this.minZ == b.minZ && this.maxX == b.maxX && this.maxZ == b.maxZ;
        }
        return false;
    }
}
