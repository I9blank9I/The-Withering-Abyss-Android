package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class ShapedCharge extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(10) == 0) {
            // Explosion damage - handled in weapon proc
            int bonusDamage = (int)(damage * 2.0f); // Explosion multiplier

            // Level 30+: Chain Reaction
            if (weaponLevel >= 30) {
                // Chain explosions - placeholder
                bonusDamage = (int)(bonusDamage * 1.5f);
            }

            // Level 60+: Nuclear Option - placeholder
            if (weaponLevel >= 60) {
                // Massive AoE explosion - placeholder
            }

            return bonusDamage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF0000); // Red color
    }
}