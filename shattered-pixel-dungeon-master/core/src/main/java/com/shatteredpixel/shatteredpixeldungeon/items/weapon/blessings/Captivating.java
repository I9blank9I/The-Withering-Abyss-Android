package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Captivating extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(8) == 0) {
            // Charm enemy - handled in weapon proc
            int bonusDamage = (int)(damage * 1.1f); // Base 10% damage bonus
            
            // Level 30+: Minion Control
            if (weaponLevel >= 30) {
                // Control charmed enemy - placeholder
                bonusDamage = (int)(bonusDamage * 1.4f);
            }
            
            // Level 60+: Army of One - placeholder
            if (weaponLevel >= 60) {
                // Multiple charms - placeholder
            }
            
            return bonusDamage;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF69B4); // Pink color
    }
}