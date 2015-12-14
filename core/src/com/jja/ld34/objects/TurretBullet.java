package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;

/**
 * Created by andrewstrauch on 12/13/15.
 */
public class TurretBullet extends Entity implements InteractiveObject {
    public static final float _width = 16f; //float specifies pixels
    public static final float _height = 16f;

    private Direction currentDirection;
    private State currentState;
    private Direction previousDirection;
    private State previousState;
    
    public TurretBullet(World world, Vector2 initialPosition, Vector2 fireDirection, Texture texture) {
        super(world, initialPosition, new Vector2(_width, _height), FixtureFilterBit.PROJECTILE_BIT, (short) (FixtureFilterBit.ALL_FLAGS & ~FixtureFilterBit.TURRET_BIT & ~FixtureFilterBit.PROJECTILE_BIT), texture);

        this.body.applyLinearImpulse(fireDirection, this.body.getWorldCenter(), true);
        this.currentDirection = Direction.LEFT;
        this.currentState = State.MOVING;
        this.previousDirection = this.currentDirection;
        this.previousState = this.currentState;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2);
    }
    
    
    @Override
    public Body initializeBody(Vector2 initialPosition, short filterCategoryBit, short filterMaskBit) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((initialPosition.x / Ld34Game.PIXELS_PER_METER) + ((_width / 2) / Ld34Game.PIXELS_PER_METER), (initialPosition.y / Ld34Game.PIXELS_PER_METER) + ((_width / 2) / Ld34Game.PIXELS_PER_METER));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = filterCategoryBit;
        fixtureDef.filter.maskBits = filterMaskBit;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);

        return this.body;
    }

    /*public float getMovementSpeed() {
        return BASE_MOVEMENT_SPEED;
    }*/

    @Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        this.shouldDestroy = true;
    }

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public enum State {
        IDLING,
        MOVING
    }
}