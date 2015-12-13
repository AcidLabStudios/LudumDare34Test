package com.jja.ld34.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;

import java.util.concurrent.atomic.AtomicInteger;

public class EnvironmentObject implements Object {

    private static int nextId = 0;
    private static String getNextId() {
        return String.valueOf(nextId++);
    }

    protected String id;
    protected World world;
    protected Body body;
    protected Rectangle bounds;

    protected boolean shouldDestroy;
    protected boolean destroyed;

    public EnvironmentObject(World world, Rectangle bounds) {
        this.id = getNextId();
        this.world = world;
        this.bounds = bounds;
        this.shouldDestroy = this.destroyed = false;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((this.bounds.getX() + this.bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (this.bounds.getY() + this.bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);

        this.body = this.world.createBody(bodyDef);
        shape.setAsBox((this.bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (this.bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = FixtureFilterBit.ENVIRONMENT_BIT;
        fixtureDef.filter.maskBits = FixtureFilterBit.ALL_FLAGS;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);

        ObjectManager.registerObject(this);
    }

    @Override
    public String getId() {
        return this.id;
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
    public void update(float delta) {
    }

    @Override
    public void destroy() {
        this.world.destroyBody(this.body);
        destroyed = true;
        ObjectManager.deregisterObject(this);
    }
}
