package com.boybook.BorderAPI;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;

@FunctionalInterface
public interface OutDo {

    void outDo(Player player, int ticks);

    class Damage implements OutDo {
        private final float beDamagedOut;
        private final int beDamagedOutTicks;
        private final Long2LongMap playerLastDamageTime = new Long2LongOpenHashMap();

        public Damage() {
            this(1, 10);
        }
        public Damage(float beDamagedOut, int beDamagedOutTicks) {
            this.beDamagedOut = beDamagedOut;
            this.beDamagedOutTicks = beDamagedOutTicks;
        }

        @Override
        public void outDo(Player player, int ticks) {
            if (this.beDamagedOut > 0 && System.currentTimeMillis() > playerLastDamageTime.getOrDefault(player.getLoaderId(), 0) + beDamagedOutTicks * 50L) {
                long nextAllowAttack = player.getNextAllowAttack();  // 让毒圈不造成攻击冷却（保持原来的攻击冷却时间）
                player.attack(new EntityDamageByBlockEvent(Block.get(Block.AIR), player, EntityDamageEvent.DamageCause.VOID, this.beDamagedOut));
                playerLastDamageTime.put(player.getLoaderId(), System.currentTimeMillis());
                player.setNextAllowAttack(nextAllowAttack);
            }
        }
    }

}
