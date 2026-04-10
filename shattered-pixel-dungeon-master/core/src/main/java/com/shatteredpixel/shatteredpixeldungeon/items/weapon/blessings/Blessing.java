package com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class Blessing implements Bundlable {

	public static final Class<?>[] blessings = new Class<?>[]{
			GlorySeeker.class, Bloodbound.class, Guided.class, Phasing.class,
			Harmonized.class, Captivating.class, Radiant.class, ShapedCharge.class
	};

	public abstract int proc(Weapon weapon, Char attacker, Char defender, int damage, int weaponLevel);

	public String name() {
		String dictName = Messages.get(this, "name");
		if (dictName.contains("NO TEXT")) {
			return this.getClass().getSimpleName();
		}
		return dictName;
	}

	public String desc() {
		return Messages.get(this, "desc");
	}

	public boolean curse() {
		return false;
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
		ArrayList<Class<? extends Blessing>> candidates = new ArrayList<>();
		for (Class<?> c : blessings) {
			candidates.add((Class<? extends Blessing>) c);
		}

		if (toIgnore != null && toIgnore.length > 0) {
			candidates.removeAll(Arrays.asList(toIgnore));
		}

		if (candidates.isEmpty()) {
			return (Blessing) Reflection.newInstance(Random.element(blessings));
		} else {
			return (Blessing) Reflection.newInstance(Random.element(candidates));
		}
	}
}
