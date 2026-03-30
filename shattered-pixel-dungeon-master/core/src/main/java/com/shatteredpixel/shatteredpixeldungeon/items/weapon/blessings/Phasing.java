package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Phasing extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        // Base: Teleport behind enemy and strike
        if (Random.Int(6) == 0) {
            // Teleport logic would be handled in weapon proc
            int bonusDamage = (int)(damage * 1.2f);

            // Level 30+: Phase Strike - AoE damage
            if (weaponLevel >= 30) {
                // Damage adjacent enemies - placeholder
                bonusDamage = (int)(bonusDamage * 1.3f);
            }

            // Level 60+: Quantum Shift - placeholder
            if (weaponLevel >= 60) {
                // Teleport enemy to you - placeholder
            }

            return bonusDamage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x800080); // Purple color
    }
}