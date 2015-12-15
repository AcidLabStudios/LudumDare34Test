package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.jja.ld34.FixtureFilterBit;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.Trait;
import com.jja.ld34.scenes.Hud;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Player extends Entity implements InteractiveObject {

    private static final int SPRITE_SIZE = 32;  // in px
    private static final float BASE_SIZE = 32f;
    private static final float BASE_MOVEMENT_SPEED = 2f;

    public enum State {
        IDLING,
        MOVING,
        DYING
    }

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Body body;

    private Direction currentDirection;
    private Direction previousDirection;
    private State currentState;
    private State previousState;
    private HashMap<Direction, TextureRegion> idlingTextureRegionMap;
    private HashMap<Direction, Animation> movingAnimationMap;
    private float animationTimer;
    private List<Trait> currentTraits;
    private Sound deathSound;
    private Sound respawnSound;

    public Player(World world, Vector2 initialPosition) {
        super(world, initialPosition, new Vector2(BASE_SIZE, BASE_SIZE), FixtureFilterBit.PROTAGONIST_BIT, FixtureFilterBit.ALL_FLAGS, new Texture("bernie/bernie.png"));

        this.currentDirection = this.previousDirection = Direction.DOWN;
        this.currentState = this.previousState = State.IDLING;
        this.animationTimer = 0;
        this.currentTraits = Trait.getRandomTraits(4);  // TODO: more traits per level?
        Hud.traitDescription = Arrays.asList(this.currentTraits).toString().replaceAll("[\\[\\]]", "").replace(", ", "") + "BERN";

        if (currentTraits.contains(Trait.NAKED)) {
            setTexture(new Texture("bernie/bernie_naked.png"));
        } else if (currentTraits.contains(Trait.REDTIE)) {
            setTexture(new Texture("bernie/bernie_redtie.png"));
        } else if (currentTraits.contains(Trait.COLONEL)) {
            setTexture(new Texture("bernie/bernie_colonel.png"));
        }

        // setup idling texture regions
        this.idlingTextureRegionMap = new HashMap<Direction, TextureRegion>(4);
        this.idlingTextureRegionMap.put(Direction.DOWN, new TextureRegion(getTexture(), 0, 0, SPRITE_SIZE, SPRITE_SIZE));
        this.idlingTextureRegionMap.put(Direction.LEFT, new TextureRegion(getTexture(), 4 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        this.idlingTextureRegionMap.put(Direction.RIGHT, new TextureRegion(getTexture(), 7 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        this.idlingTextureRegionMap.put(Direction.UP, new TextureRegion(getTexture(), 10 * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));

        // setup animations
        this.movingAnimationMap = new HashMap<Direction, Animation>(4);
        Array<TextureRegion> frames;
        frames = new Array<TextureRegion>();
        for (int i = 0; i <= 2; i++) {
            frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        }
        this.movingAnimationMap.put(Direction.DOWN, new Animation(getAnimationFramerate(), frames));
        frames = new Array<TextureRegion>();
        for (int i = 3; i <= 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        }
        this.movingAnimationMap.put(Direction.LEFT, new Animation(getAnimationFramerate(), frames));
        frames = new Array<TextureRegion>();
        for (int i = 6; i <= 8; i++) {
            frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        }
        this.movingAnimationMap.put(Direction.RIGHT, new Animation(getAnimationFramerate(), frames));
        frames = new Array<TextureRegion>();
        for (int i = 9; i <= 11; i++) {
            frames.add(new TextureRegion(getTexture(), i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE));
        }
        this.movingAnimationMap.put(Direction.UP, new Animation(getAnimationFramerate(), frames));

        setRegion(this.idlingTextureRegionMap.get(this.currentDirection));

        this.deathSound = Gdx.audio.newSound(Gdx.files.internal("bernie/death.mp3"));
        this.respawnSound = Gdx.audio.newSound(Gdx.files.internal("bernie/respawn.mp3"));
        this.respawnSound.play(0.5f);
    }

    public int getUpKey() {
        if (currentTraits.contains(Trait.REVERSE)) {
            return Input.Keys.DOWN;
        } else {
            return Input.Keys.UP;
        }
    }
    public int getDownKey() {
        if (currentTraits.contains(Trait.REVERSE)) {
            return Input.Keys.UP;
        } else {
            return Input.Keys.DOWN;
        }
    }
    public int getLeftKey() {
        if (currentTraits.contains(Trait.REVERSE)) {
            return Input.Keys.RIGHT;
        } else {
            return Input.Keys.LEFT;
        }
    }
    public int getRightKey() {
        if (currentTraits.contains(Trait.REVERSE)) {
            return Input.Keys.LEFT;
        } else {
            return Input.Keys.RIGHT;
        }
    }

    public float getSize() {
        return BASE_SIZE;
    }

    public float getMovementSpeed() {
        if (currentTraits.contains(Trait.HYPER)) {
            return BASE_MOVEMENT_SPEED * 2;
        } else {
            return BASE_MOVEMENT_SPEED;
        }
    }
    public float getAnimationFramerate() {
        return 1 / 15f;
    }

    public float getFriction(boolean inXDir) {
        if (inXDir) {
            if (currentTraits.contains(Trait.SLIPPERY)) {
                return -this.body.getLinearVelocity().x * 0.05f;
            } else {
                return -this.body.getLinearVelocity().x;
            }
        } else {
            if (currentTraits.contains(Trait.SLIPPERY)) {
                return -this.body.getLinearVelocity().y * 0.05f;
            } else {
                return -this.body.getLinearVelocity().y;
            }
        }
    }

    public void handleInput() {
        if (this.currentState == State.DYING) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), getFriction(false)), this.body.getWorldCenter(), true);
            return;
        }

        if (Gdx.input.isKeyJustPressed(getUpKey())) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), getMovementSpeed()), this.body.getWorldCenter(), true);

            this.currentDirection = Direction.UP;
            this.currentState = State.MOVING;
            this.previousDirection = this.currentDirection;
            this.previousState = this.currentState;
        } else if (Gdx.input.isKeyJustPressed(getDownKey())) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), -getMovementSpeed()), this.body.getWorldCenter(), true);

            this.currentDirection = Direction.DOWN;
            this.currentState = State.MOVING;
            this.previousDirection = this.currentDirection;
            this.previousState = this.currentState;
        } else if (Gdx.input.isKeyJustPressed(getLeftKey())) {
            this.body.applyLinearImpulse(new Vector2(-getMovementSpeed(), getFriction(false)), this.body.getWorldCenter(), true);

            this.currentDirection = Direction.LEFT;
            this.currentState = State.MOVING;
            this.previousDirection = this.currentDirection;
            this.previousState = this.currentState;
        } else if (Gdx.input.isKeyJustPressed(getRightKey())) {
            this.body.applyLinearImpulse(new Vector2(getMovementSpeed(), getFriction(false)), this.body.getWorldCenter(), true);

            this.currentDirection = Direction.RIGHT;
            this.currentState = State.MOVING;
            this.previousDirection = this.currentDirection;
            this.previousState = this.currentState;
        } else if (!Gdx.input.isKeyPressed(getUpKey()) && !Gdx.input.isKeyPressed(getDownKey()) && !Gdx.input.isKeyPressed(getLeftKey()) && !Gdx.input.isKeyPressed(getRightKey())) {
            this.body.applyLinearImpulse(new Vector2(getFriction(true), getFriction(false)), this.body.getWorldCenter(), true);

            this.currentState = State.IDLING;
            this.previousState = this.currentState;
        }
    }

    private TextureRegion getFrame(float delta) {
        if (this.previousState != this.currentState || this.previousDirection != this.currentDirection) {
            this.animationTimer = 0;
        } else {
            this.animationTimer += delta;
        }

        TextureRegion textureRegion;
        switch (this.currentState) {
            case MOVING:
                textureRegion = this.movingAnimationMap.get(this.currentDirection).getKeyFrame(this.animationTimer, true);
                break;
            default:
                textureRegion = this.idlingTextureRegionMap.get(this.currentDirection);
                break;
        }

        return textureRegion;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    @Override
    public Body initializeBody(Vector2 initialPosition, short filterCategoryBit, short filterMaskBit) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((initialPosition.x / Ld34Game.PIXELS_PER_METER) + ((getSize() / 2) / Ld34Game.PIXELS_PER_METER), (initialPosition.y / Ld34Game.PIXELS_PER_METER) + ((getSize() / 2) / Ld34Game.PIXELS_PER_METER));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getSize() / 2) / Ld34Game.PIXELS_PER_METER, (getSize() / 2) / Ld34Game.PIXELS_PER_METER);
        fixtureDef.filter.categoryBits = filterCategoryBit;
        fixtureDef.filter.maskBits = filterMaskBit;
        fixtureDef.shape = shape;
        this.body.createFixture(fixtureDef).setUserData(this);

        return body;
    }

    public void kill() {
        if (this.currentState == State.DYING) {
            return; // you can't die twice, bitch
        }

        this.currentState = State.DYING;
        this.deathSound.play(0.5f);

        final Timer fadeOutDeathTimer = new Timer();
        fadeOutDeathTimer.scheduleTask(new Timer.Task() {
            private float lastAlpha = 1f;

            @Override
            public void run() {
                if (isDestroyed()) {
                    fadeOutDeathTimer.clear();
                    return;
                }

                if (lastAlpha <= 0) {
                    fadeOutDeathTimer.clear();
                    Hud.timeLeft = 0;
                    shouldDestroy = true;
                } else {
                    lastAlpha = Math.max(0, lastAlpha - 0.2f);
                    setAlpha(lastAlpha);
                }
            }
        }, 0.1f, 0.1f);
    }

    public boolean isDying() {
        return (this.currentState == State.DYING);
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public void onCollision(short collidingFixtureFilterCategoryBits) {
        if (!FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.ENVIRONMENT_BIT) && 
                !FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.PROTAGONIST_BIT) && 
                !FixtureFilterBit.contains(collidingFixtureFilterCategoryBits, FixtureFilterBit.COLLECTIBLES_BIT)) {
            kill();
        }
    }

    @Override
    public void destroy() {
        this.deathSound.dispose();
        this.respawnSound.dispose();

        super.destroy();
    }
}
