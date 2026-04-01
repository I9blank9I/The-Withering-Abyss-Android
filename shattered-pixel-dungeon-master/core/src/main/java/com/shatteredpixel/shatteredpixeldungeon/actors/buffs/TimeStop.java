package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TimeStop extends Buff {

	{
		type = buffType.POSITIVE;
	}

	public float left;
	private ArrayList<Integer> presses = new ArrayList<>();

	@Override
	public int icon() {
		return BuffIndicator.TIME;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0, 1f); // Purple tint
	}

	@Override
	public float iconFadePercent() {
		return 0;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)(left + 0.001f));
	}

	public void set(float turns) {
		left = turns;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(Math.max(0, left)));
	}

	public void processTime(float time) {
		left -= time;

		if (left < -0.001f) {
			detach();
		}
	}

	public void setDelayedPress(int cell) {
		if (!presses.contains(cell)) {
			presses.add(cell);
		}
	}

	public void triggerPresses() {
		final ArrayList<Integer> toTrigger = new ArrayList<>(presses);
		presses.clear();
		Actor.add(new Actor() {
			{
				actPriority = VFX_PRIO;
			}

			@Override
			protected boolean act() {
				for (int cell : toTrigger) {
					Plant p = Dungeon.level.plants.get(cell);
					if (p != null) {
						p.trigger();
					}
					Trap t = Dungeon.level.traps.get(cell);
					if (t != null) {
						t.trigger();
					}
				}
				Actor.remove(this);
				return true;
			}
		});
	}

	public void disarmPresses() {
		for (int cell : presses) {
			Plant p = Dungeon.level.plants.get(cell);
			if (p != null && !(p instanceof Rotberry)) {
				Dungeon.level.uproot(cell);
			}
			Trap t = Dungeon.level.traps.get(cell);
			if (t != null && t.disarmedByActivation) {
				t.disarm();
			}
		}
		presses.clear();
	}

	@Override
	public void detach() {
		super.detach();
		triggerPresses();
		if (target != null) target.next();
	}

	@Override
	public void fx(boolean on) {
		if (!(target instanceof Hero)) return;
		Emitter.freezeEmitters = on;
		if (on) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null) mob.sprite.add(CharSprite.State.PARALYSED);
			}
		} else {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.paralysed <= 0 && mob.sprite != null) mob.sprite.remove(CharSprite.State.PARALYSED);
			}
		}
	}

	private static final String PRESSES = "presses";
	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		int[] values = new int[presses.size()];
		for (int i = 0; i < values.length; i++) values[i] = presses.get(i);
		bundle.put(PRESSES, values);
		bundle.put(LEFT, left);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		int[] values = bundle.getIntArray(PRESSES);
		for (int value : values) presses.add(value);
		left = bundle.getFloat(LEFT);
	}
}
