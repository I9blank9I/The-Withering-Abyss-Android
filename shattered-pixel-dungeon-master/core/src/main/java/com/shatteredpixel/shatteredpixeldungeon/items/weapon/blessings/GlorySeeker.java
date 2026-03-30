package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class GlorySeeker extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(20) == 0 && attacker instanceof Hero) {
            // Shout and alert all enemies on floor
            for (Char mob : Dungeon.level.mobs) {
                if (mob.alignment == Char.Alignment.ENEMY && mob != defender) {
                    mob.aggro(attacker);
                }
            }
            Buff.affect(attacker, GreaterHaste.class).set(15);
            int shield = 0;
            for (Char mob : Dungeon.level.mobs) {
                if (mob.alignment == Char.Alignment.ENEMY) {
                    shield += mob.maxHP / 10;
                }
            }
            Buff.affect(attacker, Barrier.class).setShield(shield);

            // Level 10+: Cleave
            if (weaponLevel >= 10) {
                // Hit adjacent enemies
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) continue;
                        int pos = defender.pos + i + j * Dungeon.level.width();
                        Char adj = Actor.findChar(pos);
                        if (adj != null && adj.alignment == Char.Alignment.ENEMY) {
                            adj.damage(damage, attacker);
                        }
                    }
                }
            }

            // Level 30+: Blade Beams - placeholder
            if (weaponLevel >= 30) {
                // Piercing projectile logic would go here
            }

            // Level 60+: Time Stop - placeholder
            if (weaponLevel >= 60) {
                // Time stop logic would go here
            }

            return damage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFD700); // Gold color
    }
}