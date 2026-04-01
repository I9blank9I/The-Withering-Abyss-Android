package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TimeStop;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GlorySeeker extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        // 1 in 20 chance to trigger the Glory!
        if (attacker instanceof Hero && Random.Int(20) == 0) {

            // 1. THE SHOUT: Alert all enemies on the floor
            for (Char mob : Dungeon.level.mobs) {
                if (mob instanceof Mob) {
                    ((Mob) mob).beckon(attacker.pos);
                }
            }

            // 2. THE BUFFS: Haste or Time Stop (Level 60+)
            if (weaponLevel >= 60) {
                 Buff.affect(attacker, TimeStop.class).set(10);
            } else {
                 Buff.affect(attacker, GreaterHaste.class).set(15);
            }

            // 3. THE SHIELD: Gain shield based on enemy count
            int shield = 0;
            for (Char mob : Dungeon.level.mobs) {
                if (mob.alignment == Char.Alignment.ENEMY) {
                    shield += mob.HT / 10;
                }
            }
            Buff.affect(attacker, Barrier.class).setShield(shield);

            // 4. LEVEL 10+: Cleave (Hit adjacent enemies)
            if (weaponLevel >= 10) {
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int pos = defender.pos + PathFinder.NEIGHBOURS8[i];
                    Char adj = Actor.findChar(pos);
                    if (adj != null && adj.alignment == Char.Alignment.ENEMY) {
                        adj.damage(damage, attacker);
                    }
                }
            }

            // 5. LEVEL 30+: Blade Beams
            if (weaponLevel >= 30) {
                damage *= 2;
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFD700); // Gold color
    }
}
