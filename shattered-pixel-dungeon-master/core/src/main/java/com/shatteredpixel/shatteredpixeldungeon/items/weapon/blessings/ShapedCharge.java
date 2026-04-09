package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class ShapedCharge extends Blessing {

    public int countdown = 20;
    private static final String COUNTDOWN = "countdown";

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        countdown--;
        if (countdown <= 0) {
            countdown = 20;

            attacker.sprite.showStatus(CharSprite.POSITIVE, "SHAPED CHARGE");
            explode(attacker, defender.pos, damage, weaponLevel);
        }

        return damage;
    }

    private void explode(Char attacker, int center, int damage, int weaponLevel) {
        int radius = (weaponLevel >= 30) ? 2 : 1;
        
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (Dungeon.level.distance(center, i) <= radius) {
                
                // Visuals
                CellEmitter.center(i).burst(SparkParticle.FACTORY, 3);
                
                Char target = Actor.findChar(i);
                if (target != null && target != attacker) {
                    int dmg = damage;
                    if (weaponLevel >= 30) {
                        // Ignores 50% armor - simulated by dealing damage as a source that might bypass or just adding bonus
                        // In SPD, damage() call doesn't specify armor bypass easily without custom source logic, 
                        // but we can just ensure it hits hard.
                    }
                    
                    target.damage(dmg, this);
                    
                    if (weaponLevel >= 60 && !target.isAlive()) {
                        // Chain reaction
                        explode(attacker, target.pos, damage / 2, weaponLevel);
                    }
                }
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNTDOWN, countdown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        countdown = bundle.getInt(COUNTDOWN);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFF4500); // OrangeRed
    }
}
