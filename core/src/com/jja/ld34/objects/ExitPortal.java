package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.scenes.Hud;

public class ExitPortal extends EnvironmentObject implements InteractiveEntity {

    private boolean hasBeenActivated;

    public ExitPortal(World world, Rectangle bounds) {
        super(world, bounds);

        this.hasBeenActivated = false;
    }

    @Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        // TODO: check to see if protagonist has collected all exit parts
        // if they have, go to the next level;
        // if they haven't, display a message that they haven't or play a sound or something to indicate they cannot proceed to the next level yet
        if (!hasBeenActivated && FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.PROTAGONIST_BIT)) {
            if (Hud.exitPartsCount == 5) {
                //TODO: Actually close the game loop
                Gdx.app.error("ExitPortal", "You win!");
                hasBeenActivated = true;
            }
        }
    }
}
