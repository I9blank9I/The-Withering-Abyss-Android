package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Radiant extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(6) == 0) {
            // Blind enemy - handled in weapon proc
            int bonusDamage = (int)(damage * 1.2f);

            // Level 30+: Illuminate
            if (weaponLevel >= 30) {
                // Reveal map area - placeholder
                bonusDamage = (int)(bonusDamage * 1.3f);
            }

            // Level 60+: Divine Light - placeholder
            if (weaponLevel >= 60) {
                // Permanent blindness and damage over time - placeholder
            }

            return bonusDamage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFFF00); // Yellow color
    }
}