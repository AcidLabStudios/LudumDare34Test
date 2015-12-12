package com.jja.ld34.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.Ld34Game;

public class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (bounds.getY() + bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);

        body = this.world.createBody(bodyDef);
        shape.setAsBox((bounds.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (bounds.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }
}
