package com.jja.ld34.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.scenes.Hud;

public class GoalEntity extends EnvironmentEntity implements InteractiveEntity {

    public GoalEntity(World world, Rectangle bounds) {
        super(world, bounds);
    }

    @Override
    public void onCollision() {
        // TODO: check to see if protagonist has collected all exit parts
        // if they have, go to the next level;
        // if they haven't, display a message that they haven't or play a sound or something to indicate they cannot proceed to the next level yet
        
        if(Hud.exitPartsCount == 5) {
            //TODO: Actually close the game loop
            Gdx.app.error("GoalEntity", "You win!");
        }
    }
}
