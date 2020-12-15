package com.boybook.BorderAPI;

import cn.nukkit.Player;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

@FunctionalInterface
public interface OutDo {

    void outDo(Player player, int ticks);

    class Damage implements OutDo {
        private final float beDamagedOut;
        private final int beDamagedOutTicks;

        public Damage() {
            this(1, 10);
        }
        public Damage(float beDamagedOut, int beDamagedOutTicks) {
            this.beDamagedOut = beDamagedOut;
            this.beDamagedOutTicks = beDamagedOutTicks;
        }

        @Override
        public void outDo(Player player, int ticks) {
            if (this.beDamagedOut > 0 && player.noDamageTicks <= 0) {
                player.attack(new EntityDamageByBlockEvent(new BlockAir(), player, EntityDamageEvent.DamageCause.VOID, this.beDamagedOut));
                player.noDamageTicks = this.beDamagedOutTicks;
            }
        }
    }

}
