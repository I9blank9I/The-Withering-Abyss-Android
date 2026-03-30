package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Bloodbound extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(10) == 0 && attacker instanceof Hero) {
            int drain = defender.maxHP / 20; // 5% of max HP
            defender.damage(drain, attacker);
            Buff.affect(attacker, Barrier.class).setShield(drain);

            // Level 30+: Blood Boil - AoE drain
            if (weaponLevel >= 30) {
                for (Char mob : Dungeon.level.mobs) {
                    if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(attacker.pos, mob.pos) <= 8) {
                        int aoeDrain = mob.maxHP / 20;
                        mob.damage(aoeDrain, attacker);
                        Buff.affect(attacker, Barrier.class).incShield(aoeDrain);
                    }
                }
            }

            // Level 60+: Sanguine Immortality - placeholder
            if (weaponLevel >= 60) {
                // Permanent HP increase logic would go here
            }

            return damage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x8B0000); // Dark red color
    }
}