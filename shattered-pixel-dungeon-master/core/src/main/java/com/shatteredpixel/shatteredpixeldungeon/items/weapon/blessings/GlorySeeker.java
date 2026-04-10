package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TimeStop;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Bundle;

public class GlorySeeker extends Blessing {

    private static final String KILLS = "kills";
    public int kills = 0;

    @Override
    public String name() {
        return "GlorySeeker";
    }

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel) {

        // ==========================================
        // 1. THE TALKING MECHANIC
        // ==========================================
        if (attacker instanceof Hero && damage > 0 && Random.Int(100) < 20) {
            String[] quotes = {
                    "For Glory!",
                    "Strike them down!",
                    "Let the shadows burn!",
                    "A masterful strike!",
                    "None shall stand before us!",
                    "My blade thirsts for justice!"
            };
            String quote = Random.element(quotes);

            GLog.p("GlorySeeker: \"" + quote + "\"");
            attacker.sprite.showStatus(0xFFD700, quote);
        }

        // ==========================================
        // 2. THE SENTIENT UPGRADE MECHANIC
        // ==========================================
        if (defender.HP <= damage) {
            kills++;
            int requiredKills = Math.max(1, weaponLevel) * 10;

            if (kills >= requiredKills) {
                weapon.upgrade();
                kills = 0;

                GLog.p("[#FFD700]GlorySeeker[] has consumed enough souls to grow stronger!");
                attacker.sprite.showStatus(0xFFD700, "I GROW STRONGER!");
            }
        }

        // ==========================================
        // 3. THE LEVEL 30+ BEAM (SHOOTS ON EVERY SWING!)
        // ==========================================
        if (attacker instanceof Hero && weaponLevel >= 30 && damage > 0) {
            int w = Dungeon.level.width();
            int cx = attacker.pos % w;
            int cy = attacker.pos / w;
            int tx = defender.pos % w;
            int ty = defender.pos / w;

            int dx = Integer.compare(tx, cx);
            int dy = Integer.compare(ty, cy);

            int beamDamage = Math.max(1, damage / 2);

            // Project 8 tiles deep
            for (int i = 1; i <= 8; i++) {
                int nx = cx + dx * i;
                int ny = cy + dy * i;

                if (nx < 0 || nx >= w || ny < 0 || ny >= Dungeon.level.height()) {
                    break;
                }

                int beamPos = nx + ny * w;

                // Blast the floor with a thick trail of sparks!
                CellEmitter.get(beamPos).burst(SparkParticle.FACTORY, 8);

                Char enemy = Actor.findChar(beamPos);

                if (enemy != null && enemy.alignment == Char.Alignment.ENEMY && enemy != defender) {
                    enemy.damage(beamDamage, attacker);
                    // Massive spark explosion when the beam pierces an enemy!
                    enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 15);
                }
            }
        }

        // ==========================================
        // 4. THE 5% RANDOM BUFFS
        // ==========================================
        // Kept at a 1-in-20 chance because stopping time on every single hit would crash the game!
        if (attacker instanceof Hero && Random.Int(20) == 0) {
            for (Char mob : Dungeon.level.mobs) {
                if (mob instanceof Mob) ((Mob) mob).beckon(attacker.pos);
            }

            if (weaponLevel >= 60) {
                Buff.affect(attacker, TimeStop.class).set(10);
            } else {
                Buff.affect(attacker, GreaterHaste.class).set(15);
            }

            int shield = 0;
            for (Char mob : Dungeon.level.mobs) {
                if (mob.alignment == Char.Alignment.ENEMY) shield += mob.HT / 10;
            }
            if (shield > 0) Buff.affect(attacker, Barrier.class).setShield(shield);

            if (weaponLevel >= 10) {
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int pos = defender.pos + PathFinder.NEIGHBOURS8[i];
                    Char adj = Actor.findChar(pos);
                    if (adj != null && adj.alignment == Char.Alignment.ENEMY && adj != defender) {
                        adj.damage(damage, attacker);
                        adj.sprite.centerEmitter().burst(SparkParticle.FACTORY, 4);
                    }
                }
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFD700);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KILLS, kills);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        kills = bundle.getInt(KILLS);
    }
}