package com.boybook.BorderAPI;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;

/**
 * Created by funcraft on 2016/2/15.
 */
public class TickBorderThread extends AsyncTask {

    int tick = 0;
    public void onRun() {
        long startTime = System.currentTimeMillis();
        while(true) {
            try {
                for (Border border: new ArrayList<>(BorderAPI.getInstance().borders.values())) {
                    try {
                        border.onThreadTick(tick);
                    } catch (Exception e) {
                        Server.getInstance().getLogger().debug(TextFormat.YELLOW + "TickBorderThread Tick 遍历出错：" + e.getMessage() + "  已跳过");
                    }
                }
            } catch (Exception e) {
                Server.getInstance().getLogger().debug(TextFormat.YELLOW + "TickBorderThread Tick 遍历出错：" + e.getMessage());
            }

            long duration = System.currentTimeMillis() - startTime;
            if (duration < 50) {
                try{
                    Thread.sleep(50 - duration);
                } catch (InterruptedException e) {}
            }
            startTime = System.currentTimeMillis();
            tick++;
        }

    }
}
