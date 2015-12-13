package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.Ld34Game;

public abstract class Entity extends Sprite implements Object {

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

        setBounds(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2, initialSize / Ld34Game.PIXELS_PER_METER, initialSize / Ld34Game.PIXELS_PER_METER);
        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2);

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
