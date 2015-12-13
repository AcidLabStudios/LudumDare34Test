package com.jja.ld34.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.Ld34Game;

public class EnvironmentEntity {

    protected World world;
    protected Body body;
    protected Rectangle bounds;

    public EnvironmentEntity(World world, Rectangle bounds) {
        this.world = world;
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((this.bounds.getX() + this.bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (this.bounds.getY() + this.bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);

        this.body = this.world.createBody(bodyDef);
        shape.setAsBox((this.bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (this.bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = Ld34Game.ENVIRONMENT_BIT;
        fixtureDef.filter.maskBits = Ld34Game.ALL_FLAGS;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);
    }
}
