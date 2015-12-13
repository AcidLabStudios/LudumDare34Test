package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;

public abstract class CollectibleEntity extends Entity implements InteractiveEntity {

    public CollectibleEntity(String uniqueName, World world, Vector2 initialPosition, float initialSize, TextureAtlas.AtlasRegion textureRegion) {
        super(uniqueName, world, initialPosition, initialSize, FixtureFilterBit.COLLECTIBLES_BIT, FixtureFilterBit.PROTAGONIST_BIT, textureRegion);
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
