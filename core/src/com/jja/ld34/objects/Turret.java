package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;

public class Turret extends Entity {

    public static final float _width = 32f; //float specifies pixels
    public static final float _height = 48f;
    
    private String fireDirection = "LEFT";
    private Timer gameStateTimer;
    
    public Turret (String uniqueName, World world, Vector2 initialPosition) {
        super(uniqueName, world, initialPosition, new Vector2(_width, _height), FixtureFilterBit.ENEMY_BIT, FixtureFilterBit.ALL_FLAGS, new Texture("turret/turret.png"));

        this.gameStateTimer = new Timer();
        this.gameStateTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fireBullet();
            }
        }, 2, 2);
        //Probably make him a solid object like terrain.
    }
    
    public void fireBullet() {
        //Fire a bullet based on fireDirection
        String localFireDirection;
        if(fireDirection == "LEFT") {
            
        } else if(fireDirection == "RIGHT"){
            
        } else if(fireDirection == "UP") {
            
        } else if (fireDirection == "DOWN"){
            
        } else {
            //Failed case
        }
    }
    
    public String getFireDirection() {
        return fireDirection;
    }
    public void setFireDirection(String fireDirection) {
        this.fireDirection = fireDirection;
    }

    @Override
    public Body initializeBody(Vector2 initialPosition, short filterCategoryBit, short filterMaskBit) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((initialPosition.x / Ld34Game.PIXELS_PER_METER) + ((_width / 2) / Ld34Game.PIXELS_PER_METER), (initialPosition.y / Ld34Game.PIXELS_PER_METER) + ((_width / 2) / Ld34Game.PIXELS_PER_METER));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((_width / 2) / Ld34Game.PIXELS_PER_METER, (_width / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = filterCategoryBit;
        fixtureDef.filter.maskBits = filterMaskBit;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);

        return this.body;
    }
}
