package com.boybook.BorderAPI.wall;

import cn.nukkit.Player;
import com.boybook.BorderAPI.Border;
import com.boybook.BorderAPI.MotionDirection;

/**
 * com.boybook.BorderAPI.wall
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public abstract class BorderWall {

    protected Border border;
    protected MotionDirection direction;

    public BorderWall(Border border, MotionDirection direction) {
        this.border = border;
        this.direction = direction;
    }

    public abstract void onThreadTick(int tick);

    public abstract void onTick(int tick);

    public abstract void spawnTo(Player player);

    public abstract void despawnFrom(Player player);

}
