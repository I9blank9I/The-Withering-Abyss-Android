package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;

import java.util.Arrays;

public class TutorialLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    private static final int ROOM_SIZE = 5; 
    private static final int STAGE_COUNT = 14; 

    private int currentStage = 0;
    private boolean stageLocked = false;
    private boolean waitingForNextRoom = false;
    
    private boolean artifactEquipped = false;
    private boolean pickedUpRation = false;
    private boolean tutorialComplete = false;

    private static final int STAGE_MOVE = 0;
    private static final int STAGE_EQUIP = 1;
    private static final int STAGE_COMBAT = 2;
    private static final int STAGE_SURPRISE = 3;
    private static final int STAGE_LEVELING = 4;
    private static final int STAGE_STRENGTH = 5;
    private static final int STAGE_CURSED = 6;
    private static final int STAGE_WAND = 7;
    private static final int STAGE_THROWING = 8;
    private static final int STAGE_PLANTS = 9;
    private static final int STAGE_TRAPS = 10;
    private static final int STAGE_SEARCH = 11;
    private static final int STAGE_FOOD = 12;
    private static final int STAGE_UPGRADE = 13;

    @Override
    public String tilesTex() { return Assets.Environment.TILES_SEWERS; }

    @Override
    public String waterTex() { return Assets.Environment.WATER_SEWERS; }

    private Point getRoomCenter(int stage) {
        int row = stage / 5; 
        int col = stage % 5;
        if (row % 2 != 0) {
            col = 4 - col; 
        }
        int cx = 4 + col * (ROOM_SIZE + 1);
        int cy = 4 + row * (ROOM_SIZE + 1);
        return new Point(cx, cy);
    }

    @Override
    protected boolean build() {
        setSize(32, 32); 
        
        map = new int[length()];
        Arrays.fill(map, Terrain.WALL);

        for (int stage = 0; stage < STAGE_COUNT; stage++) {
            Point c = getRoomCenter(stage);
            Painter.fill(this, c.x - 2, c.y - 2, ROOM_SIZE, ROOM_SIZE, Terrain.EMPTY);
        }

        for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
            Point c1 = getRoomCenter(stage);
            Point c2 = getRoomCenter(stage + 1);
            int doorPos = pointToCell(new Point((c1.x + c2.x) / 2, (c1.y + c2.y) / 2));
            map[doorPos] = Terrain.DOOR;
        }

        Point cThrow = getRoomCenter(STAGE_THROWING);
        map[pointToCell(new Point(cThrow.x + 1, cThrow.y - 1))] = Terrain.CHASM;
        map[pointToCell(new Point(cThrow.x + 2, cThrow.y - 1))] = Terrain.CHASM;
        map[pointToCell(new Point(cThrow.x + 1, cThrow.y - 2))] = Terrain.CHASM;

        Point cPlants = getRoomCenter(STAGE_PLANTS);
        for(int i=-2; i<=2; i++) {
            for(int j=-2; j<=2; j++) {
                if ((i+j)%2 == 0) map[pointToCell(new Point(cPlants.x+i, cPlants.y+j))] = Terrain.HIGH_GRASS;
            }
        }

        Point cTraps = getRoomCenter(STAGE_TRAPS);
        int trapPos = pointToCell(cTraps);
        map[trapPos] = Terrain.TRAP;

        Point cSearch = getRoomCenter(STAGE_SEARCH);
        int hiddenPos = pointToCell(new Point(cSearch.x, cSearch.y + 1));
        map[hiddenPos] = Terrain.SECRET_TRAP;

        this.entrance = pointToCell(getRoomCenter(0));
        map[this.entrance] = Terrain.ENTRANCE;
        transitions.add(new LevelTransition(this, this.entrance, LevelTransition.Type.REGULAR_ENTRANCE));

        this.exit = pointToCell(getRoomCenter(STAGE_COUNT - 1));
        map[this.exit] = Terrain.EXIT;
        transitions.add(new LevelTransition(this, this.exit, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    public void closeAllDoors() {
        for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
            if (stage >= currentStage) {
                Point c1 = getRoomCenter(stage);
                Point c2 = getRoomCenter(stage + 1);
                int doorPos = pointToCell(new Point((c1.x + c2.x) / 2, (c1.y + c2.y) / 2));
                set(doorPos, Terrain.LOCKED_DOOR); 
                GameScene.updateMap(doorPos);
            }
        }
        Dungeon.observe();
    }

    @Override
    public int entrance() {
        return this.entrance;
    }

    @Override
    public int randomRespawnCell(Char ch) { return entrance(); }

    @Override
    protected void createMobs() {
        Mob combatMob = new TutorialDummy();
        combatMob.pos = pointToCell(getRoomCenter(STAGE_COMBAT));
        mobs.add(combatMob);

        Point cSurp = getRoomCenter(STAGE_SURPRISE);
        Mob surpriseMob = new TutorialDummy();
        surpriseMob.pos = pointToCell(new Point(cSurp.x + 2, cSurp.y));
        mobs.add(surpriseMob);

        Point cWand = getRoomCenter(STAGE_WAND);
        Mob wandMob = new TutorialDummy();
        wandMob.pos = pointToCell(new Point(cWand.x + 2, cWand.y + 2));
        mobs.add(wandMob);

        Point cThrow = getRoomCenter(STAGE_THROWING);
        Mob throwingMob = new TutorialDummy();
        throwingMob.pos = pointToCell(new Point(cThrow.x + 2, cThrow.y - 2)); 
        mobs.add(throwingMob);

        // First Trap (Room 10) - Visible
        Point cTraps = getRoomCenter(STAGE_TRAPS);
        int trapPos = pointToCell(cTraps);
        WornDartTrap trap = new WornDartTrap();
        trap.pos = trapPos; 
        trap.visible = true; 
        traps.put(trapPos, trap); 
        
        // Search Room Trap (Room 11) - Hidden
        Point cSearch = getRoomCenter(STAGE_SEARCH);
        int hiddenPos = pointToCell(new Point(cSearch.x, cSearch.y + 1));
        WornDartTrap hiddenTrap = new WornDartTrap();
        hiddenTrap.pos = hiddenPos; 
        hiddenTrap.visible = false; 
        traps.put(hiddenPos, hiddenTrap);

        TutorialManagerMob manager = new TutorialManagerMob();
        manager.pos = 0; 
        mobs.add(manager);
    }

    @Override
    protected void createItems() {
        drop(new Guidebook(), pointToCell(getRoomCenter(STAGE_MOVE)));
        drop(new Shortsword(), pointToCell(getRoomCenter(STAGE_EQUIP)));
        drop(new PotionOfExperience(), pointToCell(getRoomCenter(STAGE_LEVELING)));

        Point cStrength = getRoomCenter(STAGE_STRENGTH);
        drop(new PotionOfStrength(), pointToCell(new Point(cStrength.x - 1, cStrength.y)));
        drop(new PotionOfStrength(), pointToCell(new Point(cStrength.x - 1, cStrength.y + 1))); 
        drop(new LeatherArmor(), pointToCell(new Point(cStrength.x + 1, cStrength.y)));

        Point cCursed = getRoomCenter(STAGE_CURSED);
        DriedRose cursedItem = new DriedRose();
        cursedItem.cursed = true;
        drop(cursedItem, pointToCell(new Point(cCursed.x - 1, cCursed.y)));
        drop(new ScrollOfRemoveCurse(), pointToCell(new Point(cCursed.x + 1, cCursed.y)));

        drop(new WandOfMagicMissile(), pointToCell(getRoomCenter(STAGE_WAND)));

        Point cThrow = getRoomCenter(STAGE_THROWING);
        ThrowingStone stones = new ThrowingStone();
        stones.quantity(7); 
        drop(stones, pointToCell(new Point(cThrow.x - 1, cThrow.y)));

        drop(new Sungrass.Seed(), pointToCell(getRoomCenter(STAGE_PLANTS)));
        drop(new SmallRation(), pointToCell(getRoomCenter(STAGE_FOOD)));
        
        Point cUpgrade = getRoomCenter(STAGE_UPGRADE);
        drop(new ScrollOfUpgrade(), pointToCell(new Point(cUpgrade.x + 1, cUpgrade.y)));
    }

    @Override
    public Mob createMob() { return null; }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("currentStage", currentStage);
        bundle.put("stageLocked", stageLocked);
        bundle.put("waitingForNextRoom", waitingForNextRoom);
        bundle.put("artifactEquipped", artifactEquipped);
        bundle.put("pickedUpRation", pickedUpRation);
        bundle.put("tutorialComplete", tutorialComplete);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        currentStage = bundle.getInt("currentStage");
        stageLocked = bundle.getBoolean("stageLocked");
        waitingForNextRoom = bundle.getBoolean("waitingForNextRoom");
        artifactEquipped = bundle.getBoolean("artifactEquipped");
        pickedUpRation = bundle.getBoolean("pickedUpRation");
        tutorialComplete = bundle.getBoolean("tutorialComplete");
    }

    public void advanceStage() {
        if (currentStage >= STAGE_COUNT - 1) {
            if (!tutorialComplete) {
                showTutorialMessage("Tutorial Complete", "Now you're ready and finished the tutorial! Pick up the Scroll of Upgrade and take the stairs down to start your adventure.");
                tutorialComplete = true; 
                
                Preferences prefs = Gdx.app.getPreferences("ShatteredPixelDungeon_Data");
                prefs.putBoolean("tutorial_finished", true);
                prefs.flush();
            }
            return;
        }

        Point c1 = getRoomCenter(currentStage);
        Point c2 = getRoomCenter(currentStage + 1);
        int doorPos = pointToCell(new Point((c1.x + c2.x) / 2, (c1.y + c2.y) / 2));

        set(doorPos, Terrain.DOOR); 
        GameScene.updateMap(doorPos);
        Dungeon.observe();

        currentStage++;
        waitingForNextRoom = true;
    }

    public void showStageMessage() {
        stageLocked = true;
        String title = "Tutorial";
        String text = "";
        
        switch (currentStage) {
            case STAGE_MOVE: text = "Pick up the Guidebook. Use WASD, arrow keys, or click to move."; break;
            case STAGE_EQUIP: text = "Pick up the sword, by clicking on it, and equip it from your inventory."; break;
            case STAGE_COMBAT: text = "Click Q near the enemy or walk into it to attack it."; break;
            case STAGE_SURPRISE: text = "Enemies lose track of you when you break line of sight. Stand behind a door, wait for the enemy to step through, and attack for a guaranteed Surprise Attack!"; break;
            case STAGE_LEVELING: text = "Defeating enemies grants Experience Points (XP). Leveling up increases your maximum Health, Evasion, and Accuracy. Drink this Potion of Experience to level up!"; break;
            case STAGE_STRENGTH: text = "Drink the Potions of Strength to wear heavier armor."; break;
            case STAGE_CURSED: text = "Pick up the artifact from the floor and equip it in your Misc slot."; break;
            case STAGE_WAND: text = "Wands shoot magical projectiles that never miss. Select the Wand to zap the enemy from afar."; break;
            case STAGE_THROWING: text = "You can't reach the enemy. Pick up the stones and throw them over the gap."; break;
            case STAGE_PLANTS: text = "If you stand on tall grass, there is a chance that it drops water drops to heal yourself with, and there's also a chance it drops seeds."; break;
            case STAGE_TRAPS: text = "Watch out for traps on the floor! They activate when you step on them or throw things on them."; break;
            case STAGE_SEARCH: text = "Rooms and hallways can contain hidden doors and traps. Click the magnifying glass Search button (or double-tap it) to reveal the hidden trap in this room."; break;
            case STAGE_FOOD: text = "Moving and making turns makes you hungry. Eat the ration to restore your energy."; break;
            case STAGE_UPGRADE: text = "Now you're ready and finished the tutorial! Pick up the Scroll of Upgrade and take the stairs down to start your adventure."; break;
        }
        
        showTutorialMessage(title, text);
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
            int heroPos = Dungeon.hero.pos;
            Point c = getRoomCenter(currentStage);
            
            int hX = heroPos % width();
            int hY = heroPos / width();
            if (Math.abs(hX - c.x) <= 2 && Math.abs(hY - c.y) <= 2) {
                waitingForNextRoom = false;
                showStageMessage();
            }
            return;
        }

        if (stageLocked) return;

        Hero hero = Dungeon.hero;

        switch (currentStage) {
            case STAGE_MOVE:
                Point c0 = getRoomCenter(STAGE_MOVE);
                if ((hero.pos % width()) >= (c0.x + 1)) advanceStage(); 
                break;
            case STAGE_EQUIP:
                if (hero.belongings.weapon != null && hero.belongings.weapon instanceof Shortsword) advanceStage();
                break;
            case STAGE_COMBAT:
            case STAGE_SURPRISE:
            case STAGE_WAND:
            case STAGE_THROWING:
                boolean dummyAlive = false;
                Point center = getRoomCenter(currentStage);
                for (Mob mob : mobs) {
                    if (mob instanceof TutorialDummy && mob.isAlive()) {
                        int mX = mob.pos % width();
                        int mY = mob.pos / width();
                        if (Math.abs(mX - center.x) <= 3 && Math.abs(mY - center.y) <= 3) {
                            dummyAlive = true; 
                            break;
                        }
                    }
                }
                if (!dummyAlive) advanceStage();
                break;
            case STAGE_LEVELING:
                if (hero.lvl > 1) {
                    advanceStage();
                } else if (hero.belongings.getItem(PotionOfExperience.class) == null) {
                    // FAILSAFE: Direct inject into backpack
                    PotionOfExperience xp = new PotionOfExperience();
                    xp.collect(hero.belongings.backpack);
                    GLog.w("You lost your Potion of Experience! Added another to your bag.");
                }
                break;
            case STAGE_STRENGTH:
                if (hero.STR >= 12 && hero.belongings.armor != null) {
                    advanceStage();
                } else if (hero.STR < 12 && hero.belongings.getItem(PotionOfStrength.class) == null) {
                    // FAILSAFE: Direct inject into backpack
                    PotionOfStrength strPotion = new PotionOfStrength();
                    strPotion.collect(hero.belongings.backpack);
                    GLog.w("You lost a Strength Potion! Added another to your bag.");
                }
                break;
            case STAGE_CURSED:
                DriedRose rose = hero.belongings.getItem(DriedRose.class);
                boolean roseEquipped = (rose != null && rose.isEquipped(hero));
                boolean roseCursed = (rose != null && rose.cursed);
                
                if (!artifactEquipped && roseEquipped && roseCursed) {
                    artifactEquipped = true;
                    showTutorialMessage("Cursed Artifact", "As you can see on this artifact its cursed, for that it gives curse removing scrolls, Use the curse removing scroll to get rid of the curse.");
                } 
                else if (artifactEquipped) {
                    if (!roseCursed) {
                        advanceStage(); 
                    } 
                    else if (hero.belongings.getItem(ScrollOfRemoveCurse.class) == null) {
                        // FAILSAFE: Direct inject into backpack
                        ScrollOfRemoveCurse rc = new ScrollOfRemoveCurse();
                        rc.collect(hero.belongings.backpack);
                        GLog.w("You wasted the scroll! Added another Remove Curse scroll to your bag.");
                    }
                }
                break;
            case STAGE_PLANTS:
                if (hero.belongings.getItem(Sungrass.Seed.class) != null) advanceStage();
                break;
            case STAGE_TRAPS:
                Trap trap = traps.get(pointToCell(getRoomCenter(STAGE_TRAPS)));
                if (trap == null || !trap.active) advanceStage();
                break;
            case STAGE_SEARCH:
                Point cSearch = getRoomCenter(STAGE_SEARCH);
                int hiddenPos = pointToCell(new Point(cSearch.x, cSearch.y + 1));
                if (map[hiddenPos] == Terrain.TRAP) advanceStage();
                break;
            case STAGE_FOOD: 
                if (!pickedUpRation && hero.belongings.getItem(SmallRation.class) != null) {
                    pickedUpRation = true;
                }
                else if (pickedUpRation && hero.belongings.getItem(SmallRation.class) == null) {
                    advanceStage();
                }
                break;
            case STAGE_UPGRADE: 
                if (hero.belongings.getItem(ScrollOfUpgrade.class) != null) advanceStage(); 
                break;
        }
    }

    // -------------------------------------------------------------
    // CUSTOM ACTORS & SPRITES
    // -------------------------------------------------------------

    public static class TutorialDummy extends Mob {
        {
            spriteClass = RatSprite.class;
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
            GLog.i("Enemy defeated!");
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
                    if (tickCounter == 0) {
                        level.closeAllDoors();
                    }
                    tickCounter++;
                    spend(TICK);
                    return true;
                }
                if (tickCounter == 3) {
                    tickCounter++;
                    if (level.currentStage == STAGE_MOVE && !level.waitingForNextRoom) {
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