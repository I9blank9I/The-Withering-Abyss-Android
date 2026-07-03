package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndDifficulty extends Window {

    public WndDifficulty(final Object selectedClass) {
        super();

        int width = 120;
        int yPos = 5;

        for (final Dungeon.Difficulty diff : Dungeon.Difficulty.values()) {
            RedButton btn = new RedButton(diff.title) {
                @Override
                protected void onClick() {
                    Dungeon.difficulty = diff;
                    
                    // Insert your specific game-start logic here
                    // Usually assigning the selectedClass and switching to InterlevelScene
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    Game.switchScene(InterlevelScene.class);
                    hide();
                }
            };
            btn.setRect(5, yPos, width, 20);
            add(btn);
            yPos += 25;
        }

        resize(width + 10, yPos + 5);
    }
}