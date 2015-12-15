package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.Ld34Game;

public abstract class Entity extends Sprite implements Object {

    protected Long id;
    protected World world;
    protected Body body;

    protected boolean shouldDestroy;
    protected boolean destroyed;

    public Entity(World world, Vector2 initialPosition, Vector2 initialSize, short filterCategoryBit, short filterMaskBit, Texture initialTexture) {
        super(initialTexture);

        this.world = world;
        this.body = initializeBody(initialPosition, filterCategoryBit, filterMaskBit);
        this.shouldDestroy = this.destroyed = false;

        setBounds(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2, initialSize.x / Ld34Game.PIXELS_PER_METER, initialSize.y / Ld34Game.PIXELS_PER_METER);
        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2);

        this.id = ObjectManager.registerObject(this);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean shouldDestroy() {
        return this.shouldDestroy;
    }

    @Override
    public boolean isDestroyed() {
        return this.destroyed;
    }

    @Override
    public void destroy() {
        this.world.destroyBody(this.body);
        destroyed = true;
        ObjectManager.deregisterObject(this);
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
}
