package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Harmonized extends Blessing {

    public float bonusMultiplier = 0f;
    private static final String BONUS = "bonusMultiplier";

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        // Harmonized triggers on every attack
        if (Random.Int(2) == 0) {
            float multiplier = 1.5f;

            if (weaponLevel >= 30) {
                multiplier = 6.0f; // +500% = 6x
            }

            if (weaponLevel >= 60) {
                multiplier += bonusMultiplier;
                bonusMultiplier += 0.01f;
            }

            attacker.sprite.showStatus(CharSprite.POSITIVE, "HARMONIZED");
            attacker.sprite.centerEmitter().burst(SparkParticle.FACTORY, 5);
            
            return (int)(damage * multiplier);
        }

        return damage;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BONUS, bonusMultiplier);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bonusMultiplier = bundle.getFloat(BONUS);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x7FFF00); // Chartreuse
    }
}
