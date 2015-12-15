package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.scenes.Hud;
import com.jja.ld34.screens.PlayScreen;

public class ExitPortal extends CollectibleEntity  {

    public static boolean hasBeenActivated = false;
    public static final float SIZE = 64f;

    public ExitPortal(World world, Vector2 initialPosition) {
        /*super(world, bounds);

        this.hasBeenActivated = false;
        Gdx.app.error("ExitPortal", "Spawned!");*/

        super(world, initialPosition, SIZE, new Texture("battery/battery.png"));
        this.hasBeenActivated = false;
        this.setAlpha(0f);
    }

    @Override
    public void onCollected() {
        //Evaluate if you have 5 parts, then swap levels.
    }

    @Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        if (FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.PROTAGONIST_BIT)) {
            if (Hud.exitPartsCount == 5) {
                //TODO: Actually close the game loop
                this.shouldDestroy = true;
                hasBeenActivated = true;
            }
        }
    }

    @Override
    public Body initializeBody(Vector2 initialPosition, short filterCategoryBit, short filterMaskBit) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((initialPosition.x / Ld34Game.PIXELS_PER_METER) + ((SIZE / 2) / Ld34Game.PIXELS_PER_METER), (initialPosition.y / Ld34Game.PIXELS_PER_METER) + ((SIZE / 2) / Ld34Game.PIXELS_PER_METER));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((SIZE / 2) / Ld34Game.PIXELS_PER_METER, (SIZE / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = filterCategoryBit;
        fixtureDef.filter.maskBits = filterMaskBit;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);

        return this.body;
    }

    /*@Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        // TODO: check to see if protagonist has collected all exit parts
        // if they have, go to the next level;
        // if they haven't, display a message that they haven't or play a sound or something to indicate they cannot proceed to the next level yet
        if (!hasBeenActivated && FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.PROTAGONIST_BIT)) {
            Gdx.app.error("ExitPortal", "You need more parts!");
            if (Hud.exitPartsCount == 5) {
                //TODO: Actually close the game loop
                Gdx.app.error("ExitPortal", "You win!");
                hasBeenActivated = true;
            }
        }
    }*/
}
