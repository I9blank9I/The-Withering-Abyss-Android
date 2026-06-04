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
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
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

    private int currentStage = 0;
    private boolean stageLocked = false;
    private boolean waitingForNextRoom = false;
    private static final int STAGE_COUNT = 8;

    private static final int STAGE_WALKING = 0;
    private static final int STAGE_COMBAT = 1;
    private static final int STAGE_FOOD = 2;
    private static final int STAGE_POTIONS = 3;
    private static final int STAGE_SCROLLS = 4;
    private static final int STAGE_STRENGTH = 5;
    private static final int STAGE_THROWING = 6;
    private static final int STAGE_TRAPS = 7;

    private int initialSTR = 10;

    @Override
    public String tilesTex() { return Assets.Environment.TILES_SEWERS; }

    @Override
    public String waterTex() { return Assets.Environment.WATER_SEWERS; }

    @Override
    protected boolean build() {
        int width = ROOM_WIDTH * STAGE_COUNT + SEPARATOR * (STAGE_COUNT - 1) + 1;
        setSize(width, ROOM_HEIGHT + 4);

        for (int i = 0; i < length(); i++) { map[i] = Terrain.WALL; }

        // Draw rooms, shifted right by 1
        for (int stage = 0; stage < STAGE_COUNT; stage++) {
            int xOffset = stage * (ROOM_WIDTH + SEPARATOR) + 1;   // shifted by 1
            Painter.fill(this, xOffset + 1, 2, ROOM_WIDTH - 2, ROOM_HEIGHT - 2, Terrain.EMPTY);
            Painter.fill(this, xOffset, 1, ROOM_WIDTH, 1, Terrain.WALL);
            Painter.fill(this, xOffset, ROOM_HEIGHT, ROOM_WIDTH, 1, Terrain.WALL);
            Painter.fill(this, xOffset, 2, 1, ROOM_HEIGHT - 2, Terrain.WALL);
            Painter.fill(this, xOffset + ROOM_WIDTH - 1, 2, 1, ROOM_HEIGHT - 2, Terrain.WALL);
        }

        // Draw corridors, shifted right by 1
        for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
            int corridorX = (stage + 1) * (ROOM_WIDTH + SEPARATOR) - 2 + 1;   // shifted by 1
            Painter.fill(this, corridorX, 2, 2, ROOM_HEIGHT - 2, Terrain.WALL);
        }

        // Entrance: at x=0, y=4
        int entrancePos = 0 + width() * 4;
        map[entrancePos] = Terrain.EMPTY;

        // Also, break the wall at (1,4) to connect to the first room
        int connectionPos = 1 + width() * 4;
        map[connectionPos] = Terrain.EMPTY;

        // Exit: we want at the shifted position of the old exit
        int exitPosRoom = (STAGE_COUNT - 1) * (ROOM_WIDTH + SEPARATOR) + ROOM_WIDTH - 2;
        int exitPos = exitPosRoom + 1 + width() * 4;   // shifted by 1 in x, then y offset
        map[exitPos] = Terrain.EXIT;
        this.exit = exitPos;

        transitions.add(new LevelTransition(this, exitPos, LevelTransition.Type.REGULAR_EXIT));
        // Set the tile to the left of the exit to wall
        map[exitPos - 1] = Terrain.WALL;

        return true;
    }


    @Override
    public int entrance() {
        return 0 + width() * 4;
    }

    @Override
    public int randomRespawnCell(Char ch) { return entrance(); }

    @Override
    protected void createMobs() {
        int combatRoomX = STAGE_COMBAT * (ROOM_WIDTH + SEPARATOR) + 1;
        Mob combatMob = new TutorialDummy();
        combatMob.pos = pointToCell(new Point(combatRoomX + 5, 4));
        mobs.add(combatMob);

        int trapsRoomX = STAGE_TRAPS * (ROOM_WIDTH + SEPARATOR) + 1;
        WornDartTrap trap = new WornDartTrap();
        trap.pos = pointToCell(new Point(trapsRoomX + 5, 4));
        traps.put(trap.pos, trap);

        TutorialManagerMob manager = new TutorialManagerMob();
        int exitPos = (STAGE_COUNT - 1) * (ROOM_WIDTH + SEPARATOR) + ROOM_WIDTH - 2 + 1 + width() * 4;
        manager.pos = exitPos;
        mobs.add(manager);
    }

    @Override
    protected void createItems() {
        drop(new Shortsword(), 4 + width() * 4);
        int foodRoomX = STAGE_FOOD * (ROOM_WIDTH + SEPARATOR) + 1;
        drop(new MysteryMeat(), pointToCell(new Point(foodRoomX + 5, 4)));
        int potionsRoomX = STAGE_POTIONS * (ROOM_WIDTH + SEPARATOR) + 1;
        drop(new PotionOfHealing(), pointToCell(new Point(potionsRoomX + 4, 4)));
        drop(new PotionOfStrength(), pointToCell(new Point(potionsRoomX + 6, 4)));
        int scrollsRoomX = STAGE_SCROLLS * (ROOM_WIDTH + SEPARATOR) + 1;
        drop(new ScrollOfIdentify(), pointToCell(new Point(scrollsRoomX + 3, 4)));
        drop(new ScrollOfUpgrade(), pointToCell(new Point(scrollsRoomX + 5, 4)));
        drop(new ScrollOfTeleportation(), pointToCell(new Point(scrollsRoomX + 7, 4)));
        int strengthRoomX = STAGE_STRENGTH * (ROOM_WIDTH + SEPARATOR) + 1;
        drop(new PotionOfStrength(), pointToCell(new Point(strengthRoomX + 5, 4)));
        int throwingRoomX = STAGE_THROWING * (ROOM_WIDTH + SEPARATOR) + 1;
        Heap throwingHeap = drop(new Dart(), pointToCell(new Point(throwingRoomX + 5, 4)));
        throwingHeap.type = Heap.Type.HEAP;
    }

    @Override
    public Mob createMob() { return null; }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("currentStage", currentStage);
        bundle.put("stageLocked", stageLocked);
        bundle.put("waitingForNextRoom", waitingForNextRoom);
        bundle.put("initialSTR", initialSTR);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        currentStage = bundle.getInt("currentStage");
        stageLocked = bundle.getBoolean("stageLocked");
        waitingForNextRoom = bundle.getBoolean("waitingForNextRoom");
        initialSTR = bundle.getInt("initialSTR");
    }

    public void advanceStage() {
        if (currentStage >= STAGE_COUNT - 1) {
            int exitPos = (STAGE_COUNT - 1) * (ROOM_WIDTH + SEPARATOR) + ROOM_WIDTH - 2 + 1 + width() * 4;
            set(exitPos - 1, Terrain.EMPTY);
            GameScene.updateMap(exitPos - 1);
            Dungeon.observe();

            showTutorialMessage("Tutorial Complete!", "You have finished the tutorial! Step onto the stairs to begin your descent.");
            stageLocked = true;
            return;
        }

        int roomRightWall = currentStage * (ROOM_WIDTH + SEPARATOR) + 1 + ROOM_WIDTH - 1;
        int nextRoomLeftWall = (currentStage + 1) * (ROOM_WIDTH + SEPARATOR) + 1;

        for (int x = roomRightWall; x <= nextRoomLeftWall; x++) {
            int cell = x + width() * 4;
            set(cell, Terrain.EMPTY);
            GameScene.updateMap(cell);
        }
        Dungeon.observe();

        currentStage++;
        waitingForNextRoom = true;
    }

    public void showStageMessage() {
        stageLocked = true;
        switch (currentStage) {
            case STAGE_WALKING: showTutorialMessage(Messages.get(TutorialLevel.class, "walking_title"), Messages.get(TutorialLevel.class, "walking_text")); break;
            case STAGE_COMBAT: showTutorialMessage(Messages.get(TutorialLevel.class, "combat_title"), Messages.get(TutorialLevel.class, "combat_text")); break;
            case STAGE_FOOD: showTutorialMessage(Messages.get(TutorialLevel.class, "food_title"), Messages.get(TutorialLevel.class, "food_text")); break;
            case STAGE_POTIONS: showTutorialMessage(Messages.get(TutorialLevel.class, "potions_title"), Messages.get(TutorialLevel.class, "potions_text")); break;
            case STAGE_SCROLLS: showTutorialMessage(Messages.get(TutorialLevel.class, "scrolls_title"), Messages.get(TutorialLevel.class, "scrolls_text")); break;
            case STAGE_STRENGTH: showTutorialMessage(Messages.get(TutorialLevel.class, "strength_title"), Messages.get(TutorialLevel.class, "strength_text")); break;
            case STAGE_THROWING: showTutorialMessage(Messages.get(TutorialLevel.class, "throwing_title"), Messages.get(TutorialLevel.class, "throwing_text")); break;
            case STAGE_TRAPS: showTutorialMessage(Messages.get(TutorialLevel.class, "traps_title"), Messages.get(TutorialLevel.class, "traps_text")); break;
        }
    }

    private void showTutorialMessage(String title, String text) {
        Game.runOnRenderThread(() -> {
            String fullText = title + "\n\n" + text;
            GameScene.show(new WndMessage(fullText) {
                @Override
                public void hide() {
                    super.hide();
                    stageLocked = false; 
                }
            });
        });
    }

    public void checkStageCompletion() {
        if (Dungeon.hero == null || !Dungeon.hero.isAlive()) return;

        if (waitingForNextRoom) {
            int heroX = Dungeon.hero.pos % width();
            int nextRoomX = currentStage * (ROOM_WIDTH + SEPARATOR) + 1;

            if (heroX >= nextRoomX + 1) {
                waitingForNextRoom = false;
                showStageMessage();
            }
            return;
        }

        if (stageLocked) return;

        Hero hero = Dungeon.hero;
        Hunger hunger = hero.buff(Hunger.class);

        switch (currentStage) {
            case STAGE_WALKING:
                int heroX = hero.pos % width();
                if (heroX >= 8) { advanceStage(); }
                break;
            case STAGE_COMBAT:
                boolean dummyAlive = false;
                for (Mob mob : mobs) {
                    if (mob instanceof TutorialDummy && mob.isAlive()) { dummyAlive = true; break; }
                }
                if (!dummyAlive) { advanceStage(); }
                break;
            case STAGE_FOOD: if (hunger != null && !hunger.isStarving()) { advanceStage(); } break;
            case STAGE_POTIONS:
                if (hero.belongings.getItem(PotionOfHealing.class) != null && hero.belongings.getItem(PotionOfStrength.class) != null) { advanceStage(); }
                break;
            case STAGE_SCROLLS:
                if (hero.belongings.getItem(ScrollOfIdentify.class) != null && hero.belongings.getItem(ScrollOfUpgrade.class) != null && hero.belongings.getItem(ScrollOfTeleportation.class) != null) { advanceStage(); }
                break;
            case STAGE_STRENGTH: if (hero.STR > initialSTR) { advanceStage(); } break;
            case STAGE_THROWING: if (com.shatteredpixel.shatteredpixeldungeon.Statistics.thrownAttacks > 0) { advanceStage(); } break;
            case STAGE_TRAPS:
                int trapPos = pointToCell(new Point(STAGE_TRAPS * (ROOM_WIDTH + SEPARATOR) + 5 + 1, 4));
                Trap trap = traps.get(trapPos);
                if (trap == null || !trap.active) { advanceStage(); }
                break;
        }
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WATER: return Messages.get(SewerLevel.class, "water_name");
            default: return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO: return Messages.get(SewerLevel.class, "empty_deco_desc");
            default: return super.tileDesc(tile);
        }
    }

    // -------------------------------------------------------------
    // CUSTOM ACTORS & SPRITES
    // -------------------------------------------------------------

    public static class TutorialDummy extends Mob {
        {
            spriteClass = TutorialDummySprite.class;
            HP = HT = 20;
            EXP = 2;
            state = PASSIVE;
        }
        
        @Override public int damageRoll() { return 0; }
        @Override public int attackSkill(Char target) { return 0; }
        @Override public int defenseSkill(Char attacker) { return 0; }
        @Override protected boolean act() { spend(TICK); return true; }
        
        @Override public void die(Object cause) {
            super.die(cause);
            GLog.i(Messages.get(this, "defeated"));
        }
        
        public static class TutorialDummySprite extends MobSprite {
            public TutorialDummySprite() {
                super();
                texture(Assets.Sprites.RAT); 
            }
            @Override public void update() {
                super.update();
                alpha(0.5f);
            }
        }
    }

    public static class InvisibleSprite extends MobSprite {
        public InvisibleSprite() {
            super();
            texture(Assets.Sprites.RAT); 
        }
        @Override
        public void update() {
            super.update();
            alpha(0.0f);
        }
    }

    public static class TutorialManagerMob extends Mob {
        private int tickCounter = 0;

        public TutorialManagerMob() {
            spriteClass = InvisibleSprite.class;
            HP = HT = 999;
            state = PASSIVE;
            flying = true;
        }

        @Override public int damageRoll() { return 0; }
        @Override public int attackSkill(Char target) { return 0; }
        @Override public int defenseSkill(Char attacker) { return 999; }


        @Override
        protected boolean act() {
            if (Dungeon.level instanceof TutorialLevel) {
                TutorialLevel level = (TutorialLevel) Dungeon.level;
                
                if (tickCounter < 3) {
                    tickCounter++;
                    spend(TICK);
                    return true;
                }
                if (tickCounter == 3) {
                    tickCounter++;
                    if (level.currentStage == STAGE_WALKING && !level.waitingForNextRoom) {
                        level.showStageMessage();
                    }
                } else {
                    level.checkStageCompletion();
                }
            }
            spend(TICK);
            return true;
        }
    }
}