package com.jja.ld34.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.Ld34Game;

public abstract class Entity extends Sprite {

    protected String uniqueName;
    protected World world;
    protected Body body;

    protected boolean isInitialized = false;
    protected boolean shouldDestroy;
    protected boolean destroyed;

    public Entity(String uniqueName, World world, Vector2 initialPosition, float initialSize, short filterCategoryBit, short filterMaskBit, TextureAtlas.AtlasRegion textureRegion) {
        super(textureRegion);

        this.uniqueName = uniqueName;
        this.world = world;
        this.body = initializeBody(initialPosition, filterCategoryBit, filterMaskBit);
        this.shouldDestroy = this.destroyed = false;

        setBounds(initialPosition.x, initialPosition.y, initialSize / Ld34Game.PIXELS_PER_METER, initialSize / Ld34Game.PIXELS_PER_METER);

        EntityManager.registerEntity(this);
        isInitialized = true;
    }

    public void update(float delta) {
        if (this.shouldDestroy && !this.destroyed) {
            this.world.destroyBody(this.body);
            destroyed = true;
            onDestroyed(delta);
            EntityManager.deregisterEntity(this);
            return;
        }

        onUpdate(delta);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        this.body.getFixtureList().first().setFilterData(filter);
    }

    public void setMaskFilter(short filterBit) {
        Filter filter = new Filter();
        filter.maskBits = filterBit;
        this.body.getFixtureList().first().setFilterData(filter);
    }

    public abstract Body initializeBody(Vector2 initialPosition, short filterCategoryBit, short filterMaskBit);

    public void onUpdate(float delta) {}
    public void onDestroyed(float delta) {}
}
