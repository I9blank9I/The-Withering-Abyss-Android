package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public abstract class Blessing implements Bundlable {

    public static final Class<?>[] blessings = new Class<?>[]{
            GlorySeeker.class, Bloodbound.class, Guided.class, Phasing.class,
            Harmonized.class, Captivating.class, Radiant.class, ShapedCharge.class
    };

    public abstract int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel);

    public String name() {
        return name(Messages.get(this, "blessing"));
    }

    public String name(String weaponName) {
        return Messages.get(this, "name", weaponName);
    }

    public String desc() {
        return Messages.get(this, "desc");
    }

    public boolean curse() {
        return false; // Blessings are never curses
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
    }

    @Override
    public void storeInBundle(Bundle bundle) {
    }

    public abstract ItemSprite.Glowing glowing();

    @SuppressWarnings("unchecked")
    public static Blessing random(Class<? extends Blessing>... toIgnore) {
        Class<?>[] candidates = blessings;
        if (toIgnore.length > 0) {
            candidates = new Class<?>[blessings.length - toIgnore.length];
            int idx = 0;
            for (Class<?> blessing : blessings) {
                boolean ignore = false;
                for (Class<?> ignored : toIgnore) {
                    if (blessing == ignored) {
                        ignore = true;
                        break;
                    }
                }
                if (!ignore) {
                    candidates[idx++] = blessing;
                }
            }
        }
        return Reflection.newInstance(Random.element(candidates));
    }
    }
}