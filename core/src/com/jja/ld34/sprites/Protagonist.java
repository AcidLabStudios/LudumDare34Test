package com.jja.ld34.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jja.ld34.Ld34Game;

public class Protagonist extends Sprite {

    private static final float BASE_SIZE = 32f;
    private static final float BASE_MOVEMENT_SPEED = 2.0f;

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    private TextureRegion idleTextureRegion;
    private Body body;
    private Direction currentDirection;

    public Protagonist(World world, TextureAtlas.AtlasRegion textureRegion) {
        super(textureRegion);

        this.idleTextureRegion = new TextureRegion(getTexture(), 0, 0, (int) getSize(), (int) getSize());
        setBounds(0, 0, getSize() / Ld34Game.PIXELS_PER_METER, getSize() / Ld34Game.PIXELS_PER_METER);
        setRegion(idleTextureRegion);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((getSize() / 2) / Ld34Game.PIXELS_PER_METER, (getSize() / 2) / Ld34Game.PIXELS_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getSize() / 2) / Ld34Game.PIXELS_PER_METER, (getSize() / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef);

        this.currentDirection = Direction.NONE;
    }

    public int getUpKey() {
        return Input.Keys.UP;
    }
    public int getDownKey() {
        return Input.Keys.DOWN;
    }
    public int getLeftKey() {
        return Input.Keys.LEFT;
    }
    public int getRightKey() {
        return Input.Keys.RIGHT;
    }

    public float getSize() {
        return BASE_SIZE;
    }

    public float getMovementSpeed() {
        return BASE_MOVEMENT_SPEED;
    }

    public float getFriction(boolean inXDir) {
        if (inXDir) {
            return -this.body.getLinearVelocity().x;
        } else {
            return -this.body.getLinearVelocity().y;
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(getUpKey()) && (currentDirection == Direction.NONE)) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), getMovementSpeed()), this.body.getWorldCenter(), true);
            currentDirection = Direction.UP;
        } else if (Gdx.input.isKeyPressed(getDownKey()) && (currentDirection == Direction.NONE)) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), -getMovementSpeed()), this.body.getWorldCenter(), true);
            currentDirection = Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(getLeftKey()) && (currentDirection == Direction.NONE)) {
            this.body.applyLinearImpulse(new Vector2(-getMovementSpeed(), getFriction(false)), this.body.getWorldCenter(), true);
            currentDirection = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(getRightKey()) && (currentDirection == Direction.NONE)) {
            this.body.applyLinearImpulse(new Vector2(getMovementSpeed(), getFriction(false)), this.body.getWorldCenter(), true);
            currentDirection = Direction.RIGHT;
        } else {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), getFriction(false)), this.body.getWorldCenter(), true);
            currentDirection = Direction.NONE;
        }
    }

    public void update(float delta) {
        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2);
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }
}
