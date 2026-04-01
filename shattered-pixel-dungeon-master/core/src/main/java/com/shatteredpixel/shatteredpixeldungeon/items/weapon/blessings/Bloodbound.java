package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Bloodbound extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        if (attacker instanceof Hero && Random.Int(10) == 0) {
            
            if (weaponLevel >= 30) {
                // AoE Drain in FOV
                for (Char mob : Dungeon.level.mobs) {
                    if (mob.alignment == Char.Alignment.ENEMY && attacker.fieldOfView[mob.pos]) {
                        drain(attacker, mob, weaponLevel);
                    }
                }
            } else {
                // Single target drain
                drain(attacker, defender, weaponLevel);
            }
        }

        return damage;
    }

    private void drain(Char attacker, Char defender, int weaponLevel) {
        int drain = Math.max(1, defender.HT / 10);
        defender.damage(drain, this);
        Buff.affect(defender, Bleeding.class).set(drain);
        
        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            int heal = drain;
            if (hero.HP + heal > hero.HT) {
                int overheal = (hero.HP + heal) - hero.HT;
                if (weaponLevel >= 60) {
                    // Sanguine Immortality: permanent HP increase
                    hero.HTBoost += overheal;
                    hero.updateHT(true);
                } else {
                    Buff.affect(hero, Barrier.class).setShield(overheal);
                }
                hero.HP = hero.HT;
            } else {
                hero.HP += heal;
            }
            attacker.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
            defender.sprite.emitter().burst(BloodParticle.FACTORY, 5);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x8B0000); // Dark Red
    }
}
