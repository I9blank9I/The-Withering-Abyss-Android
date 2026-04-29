/*
 * The Withering Abyss
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;

public class TutorialLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private static final int ROOM_WIDTH = 10;
	private static final int ROOM_HEIGHT = 8;
	private static final int SEPARATOR = 3;

	// Tutorial stage tracking
	private int currentStage = 0;
	private boolean stageLocked = true;
	private static final int STAGE_COUNT = 8;

	// Stage constants
	private static final int STAGE_WALKING = 0;
	private static final int STAGE_COMBAT = 1;
	private static final int STAGE_FOOD = 2;
	private static final int STAGE_POTIONS = 3;
	private static final int STAGE_SCROLLS = 4;
	private static final int STAGE_STRENGTH = 5;
	private static final int STAGE_THROWING = 6;
	private static final int STAGE_TRAPS = 7;

	// Track initial STR to detect strength potion use
	private int initialSTR = 10;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_SEWERS;
	}

	@Override
	protected boolean build() {
		setSize(ROOM_WIDTH * STAGE_COUNT + SEPARATOR * (STAGE_COUNT - 1), ROOM_HEIGHT + 4);

		// Clear map
		for (int i = 0; i < length(); i++) {
			map[i] = Terrain.WALL;
		}

		// Build each tutorial room
		for (int stage = 0; stage < STAGE_COUNT; stage++) {
			int xOffset = stage * (ROOM_WIDTH + SEPARATOR);

			// Floor
			Painter.fill(this, xOffset + 1, 2, ROOM_WIDTH - 2, ROOM_HEIGHT - 2, Terrain.EMPTY);

			// Walls
			Painter.fill(this, xOffset, 1, ROOM_WIDTH, 1, Terrain.WALL);
			Painter.fill(this, xOffset, ROOM_HEIGHT, ROOM_WIDTH, 1, Terrain.WALL);
			Painter.fill(this, xOffset, 1, 1, ROOM_HEIGHT, Terrain.WALL);
			Painter.fill(this, xOffset + ROOM_WIDTH - 1, 1, 1, ROOM_HEIGHT, Terrain.WALL);
		}

		// Connect rooms with corridors (initially blocked)
		for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
			int corridorX = (stage + 1) * (ROOM_WIDTH + SEPARATOR) - 2;
			Painter.fill(this, corridorX, 3, 2, 1, Terrain.WALL); // Start blocked
		}

		// Entrance
		int entrancePos = 1 + width() * 4;
		map[entrancePos] = Terrain.ENTRANCE;
		transitions.add(new LevelTransition(this, entrancePos, LevelTransition.Type.REGULAR_ENTRANCE));

		// Exit (only accessible after completing all stages)
		int exitPos = (STAGE_COUNT - 1) * (ROOM_WIDTH + SEPARATOR) + ROOM_WIDTH - 2 + width() * 4;
		map[exitPos] = Terrain.EXIT;
		transitions.add(new LevelTransition(this, exitPos, LevelTransition.Type.REGULAR_EXIT));

		// Block exit initially with a wall
		map[exitPos - 1] = Terrain.WALL;

		return true;
	}

	@Override
	protected void createMobs() {
		// Combat room - spawn a weak dummy enemy
		int combatRoomX = STAGE_COMBAT * (ROOM_WIDTH + SEPARATOR);
		Mob combatMob = new TutorialDummy();
		combatMob.pos = pointToCell(new Point(combatRoomX + 5, 4));
		mobs.add(combatMob);

		// Traps room - spawn a visible trap
		int trapsRoomX = STAGE_TRAPS * (ROOM_WIDTH + SEPARATOR);
		WornDartTrap trap = new WornDartTrap();
		trap.pos = pointToCell(new Point(trapsRoomX + 5, 4));
		traps.put(trap.pos, trap);
	}

	@Override
	protected void createItems() {
		// Entrance room - give player a weapon
		drop(new Shortsword(), 3 + width() * 4);

		// Food room items
		int foodRoomX = STAGE_FOOD * (ROOM_WIDTH + SEPARATOR);
		drop(new MysteryMeat(), pointToCell(new Point(foodRoomX + 5, 4)));

		// Potions room items
		int potionsRoomX = STAGE_POTIONS * (ROOM_WIDTH + SEPARATOR);
		drop(new PotionOfHealing(), pointToCell(new Point(potionsRoomX + 4, 4)));
		drop(new PotionOfStrength(), pointToCell(new Point(potionsRoomX + 6, 4)));

		// Scrolls room items
		int scrollsRoomX = STAGE_SCROLLS * (ROOM_WIDTH + SEPARATOR);
		drop(new ScrollOfIdentify(), pointToCell(new Point(scrollsRoomX + 3, 4)));
		drop(new ScrollOfUpgrade(), pointToCell(new Point(scrollsRoomX + 5, 4)));
		drop(new ScrollOfTeleportation(), pointToCell(new Point(scrollsRoomX + 7, 4)));

		// Strength room - another strength potion for demonstration
		int strengthRoomX = STAGE_STRENGTH * (ROOM_WIDTH + SEPARATOR);
		drop(new PotionOfStrength(), pointToCell(new Point(strengthRoomX + 5, 4)));

		// Throwing room items
		int throwingRoomX = STAGE_THROWING * (ROOM_WIDTH + SEPARATOR);
		Heap throwingHeap = drop(new Dart(), pointToCell(new Point(throwingRoomX + 5, 4)));
		throwingHeap.type = Heap.Type.HEAP;
	}

	@Override
	public int randomRespawnCell(Char ch) {
		return entrance();
	}

	@Override
	public Mob createMob() {
		return null;
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put("currentStage", currentStage);
		bundle.put("stageLocked", stageLocked);
		bundle.put("initialSTR", initialSTR);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		currentStage = bundle.getInt("currentStage");
		stageLocked = bundle.getBoolean("stageLocked");
		initialSTR = bundle.getInt("initialSTR");
	}

	@Override
	public boolean activateTransition(Hero hero, LevelTransition transition) {
		// Show walking tutorial on level enter
		boolean result = super.activateTransition(hero, transition);
		if (result && currentStage == STAGE_WALKING) {
			showTutorialMessage(Messages.get(this, "walking_title"),
					Messages.get(this, "walking_text"));
		}
		return result;
	}

	// Called to advance to the next tutorial stage
	public void advanceStage() {
		if (currentStage >= STAGE_COUNT - 1) {
			// All stages complete - open the exit
			map[exit() - 1] = Terrain.DOOR;
			GLog.h(Messages.get(this, "complete"));
			return;
		}

		currentStage++;
		stageLocked = true;

		// Open the corridor to the next room
		int corridorX = currentStage * (ROOM_WIDTH + SEPARATOR) - 2;
		map[corridorX + width() * 4] = Terrain.EMPTY;
		map[corridorX + 1 + width() * 4] = Terrain.EMPTY;

		// Show tutorial message for new stage
		switch (currentStage) {
			case STAGE_COMBAT:
				showTutorialMessage(Messages.get(this, "combat_title"),
						Messages.get(this, "combat_text"));
				break;
			case STAGE_FOOD:
				showTutorialMessage(Messages.get(this, "food_title"),
						Messages.get(this, "food_text"));
				break;
			case STAGE_POTIONS:
				showTutorialMessage(Messages.get(this, "potions_title"),
						Messages.get(this, "potions_text"));
				break;
			case STAGE_SCROLLS:
				showTutorialMessage(Messages.get(this, "scrolls_title"),
						Messages.get(this, "scrolls_text"));
				break;
			case STAGE_STRENGTH:
				showTutorialMessage(Messages.get(this, "strength_title"),
						Messages.get(this, "strength_text"));
				break;
			case STAGE_THROWING:
				showTutorialMessage(Messages.get(this, "throwing_title"),
						Messages.get(this, "throwing_text"));
				break;
			case STAGE_TRAPS:
				showTutorialMessage(Messages.get(this, "traps_title"),
						Messages.get(this, "traps_text"));
				break;
		}
	}

	private void showTutorialMessage(String title, String text) {
		Game.runOnRenderThread(() -> {
			GLog.h(title);
			GLog.i(text);
		});
	}

	// Check stage completion - called each turn
	public void checkStageCompletion() {
		if (stageLocked || Dungeon.hero == null || !Dungeon.hero.isAlive()) return;

		Hero hero = Dungeon.hero;
		Hunger hunger = hero.buff(Hunger.class);

		switch (currentStage) {
			case STAGE_WALKING:
				// Complete when player reaches the corridor
				if (hero.pos >= width() * 4 + ROOM_WIDTH - 2) {
					advanceStage();
				}
				break;

			case STAGE_COMBAT:
				// Complete when dummy is defeated
				boolean dummyAlive = false;
				for (Mob mob : mobs) {
					if (mob instanceof TutorialDummy && mob.isAlive()) {
						dummyAlive = true;
						break;
					}
				}
				if (!dummyAlive) {
					advanceStage();
				}
				break;

			case STAGE_FOOD:
				// Complete when player eats (hunger satisfied)
				if (hunger != null && !hunger.isStarving())  {
					advanceStage();
				}
				break;

			case STAGE_POTIONS:
				// Complete when potions are picked up and used
				if (hero.belongings.getItem(PotionOfHealing.class) == null ||
					hero.belongings.getItem(PotionOfStrength.class) == null) {
					advanceStage();
				}
				break;

			case STAGE_SCROLLS:
				// Complete when scrolls are used
				if (hero.belongings.getItem(ScrollOfIdentify.class) == null ||
					hero.belongings.getItem(ScrollOfUpgrade.class) == null ||
					hero.belongings.getItem(ScrollOfTeleportation.class) == null) {
					advanceStage();
				}
				break;

			case STAGE_STRENGTH:
				// Complete when STR increases
				if (hero.STR > initialSTR) {
					advanceStage();
				}
				break;

			case STAGE_THROWING:
				// Complete when player throws something
				if (com.shatteredpixel.shatteredpixeldungeon.Statistics.thrownAttacks > 0) {
					advanceStage();
				}
				break;

			case STAGE_TRAPS:
				// Complete when trap is triggered or avoided
				int trapPos = pointToCell(new Point(STAGE_TRAPS * (ROOM_WIDTH + SEPARATOR) + 5, 4));
				Trap trap = traps.get(trapPos);
				if (trap == null || !trap.active) {
					advanceStage();
				}
				break;
		}
	}


	@Override
	public String tileName(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(SewerLevel.class, "water_name");
			default:
				return super.tileName(tile);
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			default:
				return super.tileDesc(tile);
		}
	}

	// Dummy enemy for combat tutorial - doesn't fight back
	public static class TutorialDummy extends Mob {

		{
			spriteClass = TutorialDummySprite.class;

			HP = HT = 20;
			defenseSkill = 3;

			EXP = 2;

			state = PASSIVE;
		}

		@Override
		public int damageRoll() {
			return 0; // Doesn't fight back
		}

		@Override
		public int attackSkill(Char target) {
			return 0; // Never attacks
		}

		@Override
		public int defenseSkill(Char attacker) {
			return 3;
		}

		@Override
		protected boolean act() {
			// Stand still and take damage
			spend(TICK);
			return true;
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
			GLog.i(Messages.get(this, "defeated"));
		}

		public static class TutorialDummySprite extends MobSprite {

			public TutorialDummySprite() {
				super();
				texture(Assets.Sprites.GOO);
			}

			@Override
			public void update() {
				super.update();
				alpha(0.5f); // Semi-transparent to show it's a training dummy
			}
		}
	}
}
