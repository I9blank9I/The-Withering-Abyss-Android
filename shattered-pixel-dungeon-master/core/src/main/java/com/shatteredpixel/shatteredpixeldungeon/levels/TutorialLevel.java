package com.shatteredpixel.shatteredpixeldungeon.levels;

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
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
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

    private static final int ROOM_SIZE = 5; // Internal size 5x5
    private static final int STAGE_COUNT = 10;

    private int currentStage = 0;
    private boolean stageLocked = false;
    private boolean waitingForNextRoom = false;
    
    // Trackers for our specific room events
    private boolean artifactEquipped = false;
    private boolean pickedUpRation = false;
    private boolean tutorialComplete = false;

    // The 10 specific rooms
    private static final int STAGE_MOVE = 0;
    private static final int STAGE_EQUIP = 1;
    private static final int STAGE_COMBAT = 2;
    private static final int STAGE_STRENGTH = 3;
    private static final int STAGE_CURSED = 4;
    private static final int STAGE_THROWING = 5;
    private static final int STAGE_PLANTS = 6;
    private static final int STAGE_TRAPS = 7;
    private static final int STAGE_FOOD = 8;
    private static final int STAGE_UPGRADE = 9;

    private int initialSTR = 10;

    @Override
    public String tilesTex() { return Assets.Environment.TILES_SEWERS; }

    @Override
    public String waterTex() { return Assets.Environment.WATER_SEWERS; }

    // Helper to get the center coordinate of any room in the 5x2 snake grid
    private Point getRoomCenter(int stage) {
        int col = stage < 5 ? stage : 9 - stage; // 0->4 goes right, 5->9 goes left
        int row = stage < 5 ? 0 : 1;             // 0->4 is top row, 5->9 is bottom row
        int cx = 4 + col * (ROOM_SIZE + 1);
        int cy = 4 + row * (ROOM_SIZE + 1);
        return new Point(cx, cy);
    }

    @Override
    protected boolean build() {
        setSize(32, 32); 
        
        map = new int[length()];
        Arrays.fill(map, Terrain.WALL);

        // Pre-carve all 10 rooms
        for (int stage = 0; stage < STAGE_COUNT; stage++) {
            Point c = getRoomCenter(stage);
            Painter.fill(this, c.x - 2, c.y - 2, ROOM_SIZE, ROOM_SIZE, Terrain.EMPTY);
        }

        // Place standard doors initially so the engine's check succeeds
        for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
            Point c = getRoomCenter(stage);
            int doorPos;
            if (stage < 4) {
                doorPos = pointToCell(new Point(c.x + 3, c.y));
            } else if (stage == 4) {
                doorPos = pointToCell(new Point(c.x, c.y + 3));
            } else {
                doorPos = pointToCell(new Point(c.x - 3, c.y));
            }
            map[doorPos] = Terrain.DOOR;
        }

        // Room 6 (Throwing) - Place a chasm wall on the right side of the room
        Point c5 = getRoomCenter(STAGE_THROWING);
        for (int y = c5.y - 2; y <= c5.y + 2; y++) {
            map[pointToCell(new Point(c5.x + 1, y))] = Terrain.CHASM;
        }

        // Room 7 (Plants) - Add High Grass
        Point c6 = getRoomCenter(STAGE_PLANTS);
        for(int i=-2; i<=2; i++) {
            for(int j=-2; j<=2; j++) {
                if ((i+j)%2 == 0) {
                    map[pointToCell(new Point(c6.x+i, c6.y+j))] = Terrain.HIGH_GRASS;
                }
            }
        }

        // Room 8 (Traps) - Set the physical floor tile to be a trap
        Point c7 = getRoomCenter(STAGE_TRAPS);
        int trapPos = pointToCell(c7);
        map[trapPos] = Terrain.TRAP;

        // EXPLICIT ENTRANCE FIX: Assign the Entrance Transition so the player spawns here
        this.entrance = pointToCell(getRoomCenter(0));
        map[this.entrance] = Terrain.ENTRANCE;
        transitions.add(new LevelTransition(this, this.entrance, LevelTransition.Type.REGULAR_ENTRANCE));

        // Exit in Room 10
        this.exit = pointToCell(getRoomCenter(9));
        map[this.exit] = Terrain.EXIT;
        transitions.add(new LevelTransition(this, this.exit, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    // Locks the doors immediately once the scene loads
    public void closeAllDoors() {
        for (int stage = 0; stage < STAGE_COUNT - 1; stage++) {
            if (stage >= currentStage) {
                Point c = getRoomCenter(stage);
                int doorPos;
                if (stage < 4) {
                    doorPos = pointToCell(new Point(c.x + 3, c.y));
                } else if (stage == 4) {
                    doorPos = pointToCell(new Point(c.x, c.y + 3));
                } else {
                    doorPos = pointToCell(new Point(c.x - 3, c.y));
                }
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

        Point c5 = getRoomCenter(STAGE_THROWING);
        Mob throwingMob = new TutorialDummy();
        throwingMob.pos = pointToCell(new Point(c5.x + 2, c5.y));
        mobs.add(throwingMob);

        // Room 8 (Traps)
        Point c7 = getRoomCenter(STAGE_TRAPS);
        int trapPos = pointToCell(c7);
        WornDartTrap trap = new WornDartTrap();
        trap.pos = trapPos; 
        trap.visible = true; 
        traps.put(trapPos, trap); 

        TutorialManagerMob manager = new TutorialManagerMob();
        // Fixed: Placed at position 0 (off-grid boundary) so the exit stairs are completely clear
        manager.pos = 0; 
        mobs.add(manager);
    }

    @Override
    protected void createItems() {
        // Room 2: Equip Sword
        drop(new Shortsword(), pointToCell(getRoomCenter(STAGE_EQUIP)));
        
        // Room 4: Strength & Armor
        Point c3 = getRoomCenter(STAGE_STRENGTH);
        drop(new PotionOfStrength(), pointToCell(new Point(c3.x - 1, c3.y)));
        drop(new LeatherArmor(), pointToCell(new Point(c3.x + 1, c3.y)));

        // Room 5: Cursed Artifact & Scroll
        Point c4 = getRoomCenter(STAGE_CURSED);
        DriedRose cursedItem = new DriedRose();
        cursedItem.cursed = true;
        drop(cursedItem, pointToCell(new Point(c4.x - 1, c4.y)));
        drop(new ScrollOfRemoveCurse(), pointToCell(new Point(c4.x + 1, c4.y)));

        // Room 6: Throwing Stones (Increased to 7 stack capacity)
        Point c5 = getRoomCenter(STAGE_THROWING);
        ThrowingStone stones = new ThrowingStone();
        stones.quantity(7);
        drop(stones, pointToCell(new Point(c5.x - 1, c5.y)));

        // Room 7: Plants 
        drop(new Sungrass.Seed(), pointToCell(getRoomCenter(STAGE_PLANTS)));

        // Room 9: Food
        drop(new SmallRation(), pointToCell(getRoomCenter(STAGE_FOOD)));

        // Room 10: Upgrade Scroll
        Point c9 = getRoomCenter(STAGE_UPGRADE);
        drop(new ScrollOfUpgrade(), pointToCell(new Point(c9.x + 1, c9.y)));
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
        initialSTR = bundle.getInt("initialSTR");
        artifactEquipped = bundle.getBoolean("artifactEquipped");
        pickedUpRation = bundle.getBoolean("pickedUpRation");
        tutorialComplete = bundle.getBoolean("tutorialComplete");
    }

    public void advanceStage() {
        if (currentStage >= STAGE_COUNT - 1) {
            if (!tutorialComplete) {
                showTutorialMessage("Tutorial Complete", "Now you're ready and finished the tutorial! Pick up the Scroll of Upgrade and take the stairs down to start your adventure.");
                tutorialComplete = true; 
            }
            return;
        }

        Point c = getRoomCenter(currentStage);
        int doorPos;
        if (currentStage < 4) {
            doorPos = pointToCell(new Point(c.x + 3, c.y)); 
        } else if (currentStage == 4) {
            doorPos = pointToCell(new Point(c.x, c.y + 3)); 
        } else {
            doorPos = pointToCell(new Point(c.x - 3, c.y)); 
        }

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
            case STAGE_MOVE: text = "Use WASD, arrow keys, or click to move."; break;
            case STAGE_EQUIP: text = "Pick up the sword, by clicking on it, and equip it from your inventory."; break;
            case STAGE_COMBAT: text = "Click Q near the enemy or walk into it to attack it."; break;
            case STAGE_STRENGTH: text = "Drink the Potion of Strength to wear heavier armor."; break;
            case STAGE_CURSED: text = "Pick up the artifact from the floor and equip it in your Misc slot."; break;
            case STAGE_THROWING: text = "You can't reach the enemy. Pick up the stones and throw them over the gap."; break;
            case STAGE_PLANTS: text = "If you stand on tall grass, there is a chance that it drops water drops to heal yourself with, and there's also a chance it drops seeds."; break;
            case STAGE_TRAPS: text = "Watch out for traps on the floor! They activate when you step on them or throw things on them."; break;
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
                if (hero.pos % width() >= c0.x + 1) advanceStage(); 
                break;
            case STAGE_EQUIP:
                if (hero.belongings.weapon != null && hero.belongings.weapon instanceof Shortsword) advanceStage();
                break;
            case STAGE_COMBAT:
            case STAGE_THROWING:
                boolean dummyAlive = false;
                Point center = getRoomCenter(currentStage);
                for (Mob mob : mobs) {
                    if (mob instanceof TutorialDummy && mob.isAlive()) {
                        int mX = mob.pos % width();
                        int mY = mob.pos / width();
                        if (Math.abs(mX - center.x) <= 2 && Math.abs(mY - center.y) <= 2) {
                            dummyAlive = true; 
                            break;
                        }
                    }
                }
                if (!dummyAlive) advanceStage();
                break;
            case STAGE_STRENGTH:
                if (hero.STR > initialSTR && hero.belongings.armor != null) advanceStage();
                break;
            case STAGE_CURSED:
                boolean roseEquipped = false;
                DriedRose rose = hero.belongings.getItem(DriedRose.class);
                
                if (rose != null && rose.isEquipped(hero)) {
                    roseEquipped = true;
                }
                
                if (!artifactEquipped && roseEquipped) {
                    artifactEquipped = true;
                    showTutorialMessage("Cursed Artifact", "As you can see on this artifact its cursed, for that it gives curse removing scrolls, Use the curse removing scroll to get rid of the curse.");
                } 
                else if (artifactEquipped && hero.belongings.getItem(ScrollOfRemoveCurse.class) == null) {
                    advanceStage();
                }
                break;
            case STAGE_PLANTS:
                if (hero.belongings.getItem(Sungrass.Seed.class) != null) advanceStage();
                break;
            case STAGE_TRAPS:
                Trap trap = traps.get(pointToCell(getRoomCenter(STAGE_TRAPS)));
                if (trap == null || !trap.active) advanceStage();
                break;
            case STAGE_FOOD: 
                // Tracking pick-up state safely
                if (!pickedUpRation && hero.belongings.getItem(SmallRation.class) != null) {
                    pickedUpRation = true;
                }
                // Progression activates strictly when the ration leaves the player inventory bag via eating
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
            EXP = 5;
            state = PASSIVE;
        }
        
        @Override public int damageRoll() { return 0; }
        @Override public int attackSkill(Char target) { return 0; }
        @Override public int defenseSkill(Char attacker) { return 0; }
        @Override protected boolean act() { spend(TICK); return true; }
        
        @Override public void die(Object cause) {
            super.die(cause);
            GLog.i("Dummy defeated!");
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