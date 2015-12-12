package com.jja.ld34.sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jja.ld34.Ld34Game;

public class Collectable extends InteractiveTileObject {

    public Collectable(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
