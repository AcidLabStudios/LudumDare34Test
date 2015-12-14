package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.FixtureFilterBit;

public abstract class CollectibleEntity extends Entity implements InteractiveObject {

    public CollectibleEntity(World world, Vector2 initialPosition, float initialSize, Texture initialTexture) {
        super(world, initialPosition, new Vector2(initialSize, initialSize), FixtureFilterBit.COLLECTIBLES_BIT, FixtureFilterBit.PROTAGONIST_BIT, initialTexture);
    }

    @Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        if (FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.PROTAGONIST_BIT)) {
            onCollected();
            this.shouldDestroy = true;
        }
    }

    public abstract void onCollected();
}
