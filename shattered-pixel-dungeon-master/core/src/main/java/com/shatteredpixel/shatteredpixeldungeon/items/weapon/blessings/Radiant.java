package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class Radiant extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        if (attacker instanceof Hero && Random.Int(10) == 0) {

            GameScene.flash(0xFFFFFF80);
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC);

            if (weaponLevel >= 60) {
                // Omniscience: reveal map and blind EVERY enemy on floor
                for (int i = 0; i < Dungeon.level.length(); i++) {
                    Dungeon.level.discover(i);
                }
                for (Char mob : Dungeon.level.mobs) {
                    if (mob.alignment == Char.Alignment.ENEMY) {
                        blindAndBurn(attacker, mob, weaponLevel);
                    }
                }
            } else {
                // Base: Blindness in FOV
                for (Char mob : Dungeon.level.mobs) {
                    if (mob.alignment == Char.Alignment.ENEMY && attacker.fieldOfView[mob.pos]) {
                        blindAndBurn(attacker, mob, weaponLevel);
                    }
                }
            }
            attacker.sprite.showStatus(CharSprite.POSITIVE, "RADIANT FLASH");
        }

        return damage;
    }

    private void blindAndBurn(Char attacker, Char target, int weaponLevel) {
        Buff.prolong(target, Blindness.class, 10);
        
        if (weaponLevel >= 30) {
            // Scorching Light
            if (target.properties().contains(Char.Property.UNDEAD) || target.properties().contains(Char.Property.DEMONIC)) {
                target.die(this);
            } else {
                target.damage(target.HT / 5, this);
            }
            CellEmitter.center(target.pos).burst(Speck.factory(Speck.LIGHT), 4);
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFFFFF); // White light
    }
}
