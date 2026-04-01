package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Guided extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        
        // 1/4 chance for the "Guided" effect
        if (attacker instanceof Hero && Random.Int(4) == 0) {
            
            // Treat as surprise attack - simulated by damage boost and FX
            int finalDamage = (int)(damage * 1.5f);
            
            attacker.sprite.showStatus(CharSprite.POSITIVE, "GUIDED");
            CellEmitter.center(defender.pos).burst(Speck.factory(Speck.STAR), 5);
            Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);

            // Level 30+: Echo Strike (3 hits)
            if (weaponLevel >= 30) {
                finalDamage *= 3;
                attacker.sprite.showStatus(CharSprite.POSITIVE, "ECHO STRIKE");
            }

            // Level 60+: Global Reach is handled in MeleeWeapon's logic or a specific hero action
            // For the proc, we can just ensure it hits hard.
            if (weaponLevel >= 60) {
                finalDamage *= 2; 
            }

            return finalDamage;
        }
        
        return damage;
    }

    // Static helper to find the "Highest Threat" enemy for Global Reach
    public static Mob findHighestThreat(Hero hero) {
        ArrayList<Mob> enemies = new ArrayList<>(Dungeon.level.mobs);
        if (enemies.isEmpty()) return null;

        Collections.sort(enemies, new Comparator<Mob>() {
            @Override
            public int compare(Mob a, Mob b) {
                // Threat defined by HT * Level
                return (b.HT * b.maxLvl) - (a.HT * a.maxLvl);
            }
        });

        return enemies.get(0);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00FFFF); // Cyan color
    }
}
