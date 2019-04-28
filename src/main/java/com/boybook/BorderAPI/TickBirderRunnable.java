package com.boybook.BorderAPI;

/**
 * Created by funcraft on 2016/2/15.
 */
public class TickBirderRunnable implements Runnable{

    private int tick = 0;

    public void run() {
        for (Border border: BorderAPI.getInstance().borders.values()) {
            border.onTick(tick++);
        }
    }

}
