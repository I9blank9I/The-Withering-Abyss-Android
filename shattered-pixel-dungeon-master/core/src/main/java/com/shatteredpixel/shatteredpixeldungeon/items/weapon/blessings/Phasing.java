package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Phasing extends Blessing {

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {
        if (Random.Int(12) == 0) {
            if (Random.Int(2) == 0) {
                // Teleport enemy
                int newPos = -1;
                ArrayList<Integer> wallTiles = new ArrayList<>();
                for (int i = 0; i < com.watabou.utils.PathFinder.NEIGHBOURS8.length; i++) {
                    int p = defender.pos + com.watabou.utils.PathFinder.NEIGHBOURS8[i];
                    if (Dungeon.level.solid[p]) {
                        wallTiles.add(p);
                    }
                }

                if (!wallTiles.isEmpty()) {
                    newPos = Random.element(wallTiles);
                    // Massive crush damage
                    int crushDamage = defender.HT / 4;
                    defender.damage(crushDamage, this);
                    
                    if (weaponLevel >= 30) {
                        // AoE shrapnel explosion
                        for (int i = 0; i < com.watabou.utils.PathFinder.NEIGHBOURS8.length; i++) {
                            Char adj = Actor.findChar(newPos + com.watabou.utils.PathFinder.NEIGHBOURS8[i]);
                            if (adj != null && adj != attacker) {
                                adj.damage(crushDamage, this);
                            }
                        }
                    }
                }

                if (weaponLevel >= 60 && Random.Int(10) == 0) {
                    // Void Banishment
                    defender.die(this);
                    GLog.w("The enemy was banished to the void!");
                }

            } else {
                // Teleport Hero behind enemy
                int newPos = -1;
                for (int i = 0; i < com.watabou.utils.PathFinder.NEIGHBOURS8.length; i++) {
                    int p = defender.pos + com.watabou.utils.PathFinder.NEIGHBOURS8[i];
                    if (Dungeon.level.passable[p] && Actor.findChar(p) == null) {
                        newPos = p;
                        break;
                    }
                }
                if (newPos != -1 && attacker instanceof Hero) {
                    attacker.pos = newPos;
                    attacker.sprite.move(attacker.pos, defender.pos);
                }
            }
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x9400D3); // Dark Violet
    }
}
