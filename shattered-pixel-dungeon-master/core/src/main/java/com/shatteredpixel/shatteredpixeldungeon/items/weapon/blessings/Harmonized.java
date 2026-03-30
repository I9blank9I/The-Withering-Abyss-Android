package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Harmonized extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        // Damage multiplier based on weapon level (minimum 1.2x for non-Masterwork)
        float multiplier = Math.max(1.2f, 1.0f + (weaponLevel / 10.0f)); // +10% per 10 levels, minimum 20%
        int bonusDamage = (int)(damage * multiplier);

        // Level 30+: Resonance - damage increases with consecutive hits
        if (weaponLevel >= 30) {
            // Consecutive hit tracking would be handled in weapon
            bonusDamage = (int)(bonusDamage * 1.5f); // Placeholder for resonance
        }

        // Level 60+: Infinite Harmony - placeholder
        if (weaponLevel >= 60) {
            // Damage scales infinitely - placeholder
        }

        return bonusDamage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFD700); // Gold color
    }
}