package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Guided extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(4) == 0) {
            // Guaranteed hit, bypass evasion and armor - handled in accuracyFactor
            int bonusDamage = (int)(damage * 1.5f); // Surprise attack bonus

            // Level 30+: Echo Strike
            if (weaponLevel >= 30) {
                // Additional strikes - extra damage
                bonusDamage = (int)(bonusDamage * 1.5f);
            }

            // Level 60+: Global Reach - placeholder
            if (weaponLevel >= 60) {
                // Strike highest threat enemy - placeholder
            }

            return bonusDamage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00FFFF); // Cyan color
    }
}