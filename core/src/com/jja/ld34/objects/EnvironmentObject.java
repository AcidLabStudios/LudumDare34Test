package com.jja.ld34.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;

public class EnvironmentObject implements Object {

    protected World world;
    protected Body body;
    protected Rectangle bounds;

    public EnvironmentObject(World world, Rectangle bounds) {
        this.world = world;
        this.bounds = bounds;

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
    }
}
