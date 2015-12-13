package com.jja.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.scenes.Hud;
import com.jja.ld34.objects.*;

public class PlayScreen implements Screen, ContactListener {

    private SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Player player;

    private TextureAtlas textureAtlas;

    public PlayScreen() {
        this.textureAtlas = new TextureAtlas("jja-ld34.pack");
        this.spriteBatch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Ld34Game.GAME_WIDTH / Ld34Game.PIXELS_PER_METER, Ld34Game.GAME_HEIGHT / Ld34Game.PIXELS_PER_METER, this.camera);

        this.hud = new Hud(this.spriteBatch);

        this.mapLoader = new TmxMapLoader();
        this.map = this.mapLoader.load("1-1.tmx");  // TODO
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, 1 / Ld34Game.PIXELS_PER_METER);
        this.camera.position.set(this.viewport.getWorldWidth() / 2, this.viewport.getWorldHeight() / 2, 0);

        this.world = new World(new Vector2(0, 0), true);
        this.world.setContactListener(this);
        this.debugRenderer = new Box2DDebugRenderer();

        populateWorld();
    }

    public void populateWorld() {
        if (this.map.getLayers().get("walls") != null) {
            for (MapObject object : this.map.getLayers().get("walls").getObjects()) {
                new EnvironmentObject(this.world, ((RectangleMapObject) object).getRectangle());
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'walls' layer!");
        }

        if (this.map.getLayers().get("exitparts") != null) {
            int exitPartsCount = 0;
            for (MapObject object : this.map.getLayers().get("exitparts").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new ExitPart("exitPart" + exitPartsCount, this.world, new Vector2(bounds.x, bounds.y), this.textureAtlas.findRegion("battery"));
                exitPartsCount++;
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exitparts' layer!");
        }

        if (this.map.getLayers().get("berniespawn") != null) {
            MapObject protagonistSpawnMapObject = this.map.getLayers().get("berniespawn").getObjects().get(0);
            if (protagonistSpawnMapObject != null) {
                Rectangle bounds = ((RectangleMapObject) protagonistSpawnMapObject).getRectangle();
                this.player = new Player("player", this.world, new Vector2(bounds.x, bounds.y), this.textureAtlas.findRegion("bernie"));
            } else {
                this.player = new Player("player", this.world, new Vector2(0, 0), this.textureAtlas.findRegion("bernie"));
                Gdx.app.error("PlayScreen", "Unable to find spawnpoint for player in 'berniespawn' layer of map! Fell back to spawning at (0, 0).");
            }
        } else {
            this.player = new Player("player", this.world, new Vector2(0, 0), this.textureAtlas.findRegion("bernie"));
            Gdx.app.error("PlayScreen", "Map has no 'berniespawn' layer! Fell back to spawning at (0, 0).");
        }

        if (this.map.getLayers().get("exit") != null) {
            MapObject exitSpawnMapObject = this.map.getLayers().get("exit").getObjects().get(0);
            if (exitSpawnMapObject != null) {
                new ExitPortal(this.world, ((RectangleMapObject) exitSpawnMapObject).getRectangle());
            } else {
                Gdx.app.error("PlayScreen", "Unable to find exit in 'exit' layer of map! No exit was spawned; there is no exit to this map.");
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exit' layer!");
        }
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        if (this.player != null) {
            this.player.handleInput();
        }
    }

    public void update(float delta) {
        handleInput(delta);

        // update world
        this.world.step(1 / 60f, 6, 2);

        EntityManager.updateAllEntities(delta);

        if (this.player != null) {
            // center camera on player
            this.camera.position.set(this.player.getPosition(), 0);
        }

        this.camera.update();
        this.mapRenderer.setView(this.camera);
        
        hud.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render map
        this.mapRenderer.render();

        // render debug physics output
        // TODO: remove/comment this before release
        this.debugRenderer.render(world, camera.combined);

        // render player
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.spriteBatch.begin();
        EntityManager.drawAllEntities(this.spriteBatch);
        this.spriteBatch.end();

        // render HUD
        this.spriteBatch.setProjectionMatrix(this.hud.getStageCamera());
        this.hud.drawStage();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() instanceof InteractiveEntity) {
            ((InteractiveEntity) fixtureA.getUserData()).onCollision(fixtureB.getFilterData().categoryBits);
        }

        if (fixtureB.getUserData() instanceof InteractiveEntity) {
            ((InteractiveEntity) fixtureB.getUserData()).onCollision(fixtureA.getFilterData().categoryBits);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.mapRenderer.dispose();
        this.world.dispose();
        this.debugRenderer.dispose();
        this.hud.dispose();
    }

    public static TextureRegion generateTextureRegion(Texture texture, int index) {
        return new TextureRegion(texture, 2 + (index * (int) Ld34Game.BASE_SPRITE_SIZE), 0, (int) Ld34Game.BASE_SPRITE_SIZE, (int) Ld34Game.BASE_SPRITE_SIZE);
    }
}
