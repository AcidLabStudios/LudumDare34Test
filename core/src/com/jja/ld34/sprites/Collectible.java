package com.jja.ld34.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.Ld34Game;

public abstract class Collectible extends Entity implements InteractiveEntity {

    public Collectible(String uniqueName, World world, Vector2 initialPosition, float initialSize, TextureAtlas.AtlasRegion textureRegion) {
        super(uniqueName, world, initialPosition, initialSize, Ld34Game.COLLECTIBLES_BIT, Ld34Game.PROTAGONIST_BIT, textureRegion);
    }

    @Override
    public void onCollision() {
        if (isInitialized) {
            onCollected();
            this.shouldDestroy = true;
        }
    }

    public abstract void onCollected();
}
