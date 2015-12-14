package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.screens.PlayScreen;

public class Turret extends Entity {

    public static final float _width = 32f; //float specifies pixels
    public static final float _height = 48f;
    
    private String fireDirection = "LEFT";
    private Timer gameStateTimer;
    private Vector2 _initialPosition;
    
    private Texture bulletTexture = new Texture("turret_bullet/energy_ball.png");
    private static final float bulletSpeed = 2f;
    private Integer bulletOffsetX = 10;
    private Integer bulletOffsetY = 30;
    
    public Integer _turretLevel;
    
    public Turret (World world, Vector2 initialPosition, Integer level) {
        super(world, initialPosition, new Vector2(_width, _height), FixtureFilterBit.TURRET_BIT, (short) (FixtureFilterBit.ALL_FLAGS & ~FixtureFilterBit.PROJECTILE_BIT), new Texture("turret/turret.png"));

        this.gameStateTimer = new Timer();
        this.gameStateTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fireBullet();
            }
        }, 3, 3);
        //Probably make him a solid object like terrain.

        _initialPosition = initialPosition;
        _turretLevel = level;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (ExitPortal.hasBeenActivated){
            //shouldDestroy = true;
            destroy();
        }
    }

    @Override
    public void destroy() {
        this.world.destroyBody(this.body);
        destroyed = true;
        ObjectManager.deregisterObject(this);
    }
    
    public void fireBullet() {
        //Fire a bullet based on fireDirection

        if(_turretLevel == PlayScreen.currentLevel){
            new TurretBullet(this.world, new Vector2(_initialPosition.x  + bulletOffsetX, _initialPosition.y + bulletOffsetY), new Vector2(-bulletSpeed, 0), bulletTexture); //left
            new TurretBullet(this.world, new Vector2(_initialPosition.x  + bulletOffsetX, _initialPosition.y + bulletOffsetY), new Vector2(bulletSpeed, 0), bulletTexture); //right
            new TurretBullet(this.world, new Vector2(_initialPosition.x  + bulletOffsetX, _initialPosition.y + bulletOffsetY), new Vector2(0, bulletSpeed), bulletTexture); //up
            new TurretBullet(this.world, new Vector2(_initialPosition.x  + bulletOffsetX, _initialPosition.y + bulletOffsetY), new Vector2(0, -bulletSpeed), bulletTexture); //down
        }
        
        /*String localFireDirection;
        if(fireDirection == "LEFT") {
            //Spawn bullet and pass in "LEFT"
            new TurretBullet("turretbullet" + bulletsFired, this.world, new Vector2(_initialPosition), "LEFT", bulletTexture);
            bulletsFired ++;
        } if(fireDirection == "RIGHT"){
            
        } if(fireDirection == "UP") {
            
        } if (fireDirection == "DOWN"){
            
        } else {
            //Failed case
        }*/
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

    public Vector2 getPosition() {
        return this.body.getPosition();
    }
}
