package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Captivating extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        // 10% chance to trigger
        if (attacker instanceof Hero && Random.Int(10) == 0) {

            if (weaponLevel >= 30) {
                // Domination: Permanent Corruption
                Buff.affect(defender, Corruption.class);
            } else {
                // Base: Charm the enemy for 5 turns
                // Using the engine's built-in prolong method instead of .set()
                Buff.prolong(defender, Charm.class, 5f);
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF69B4); // Hot Pink
    }
}