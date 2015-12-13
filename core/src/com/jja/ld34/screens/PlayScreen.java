package com.jja.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.jja.ld34.sprites.*;

public class PlayScreen implements Screen {

    private SpriteBatch spriteBatch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Protagonist protagonist;

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
        this.debugRenderer = new Box2DDebugRenderer();

        populateWorld();
    }

    public void populateWorld() {
        if (this.map.getLayers().get("walls") != null) {
            for (MapObject object : this.map.getLayers().get("walls").getObjects()) {
                new EnvironmentEntity(this.world, ((RectangleMapObject) object).getRectangle());
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'walls' layer!");
        }

        if (this.map.getLayers().get("exitparts") != null) {
            int exitPartsCount = 0;
            for (MapObject object : this.map.getLayers().get("exitparts").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new ExitPart("exitPart" + exitPartsCount, this.world, new Vector2(bounds.x, bounds.y), this.textureAtlas.findRegion("bernie")); // TODO: pass an actual exit part texture region here
                exitPartsCount++;
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exitparts' layer!");
        }

        if (this.map.getLayers().get("berniespawn") != null) {
            MapObject protagonistSpawnMapObject = this.map.getLayers().get("berniespawn").getObjects().get(0);
            if (protagonistSpawnMapObject != null) {
                Rectangle bounds = ((RectangleMapObject) protagonistSpawnMapObject).getRectangle();
                this.protagonist = new Protagonist("protagonist", this.world, new Vector2(bounds.x, bounds.y), this.textureAtlas.findRegion("bernie"));
            } else {
                this.protagonist = new Protagonist("protagonist", this.world, new Vector2(0, 0), this.textureAtlas.findRegion("bernie"));
                Gdx.app.error("PlayScreen", "Unable to find spawnpoint for protagonist in 'berniespawn' layer of map! Fell back to spawning at (0, 0).");
            }
        } else {
            this.protagonist = new Protagonist("protagonist", this.world, new Vector2(0, 0), this.textureAtlas.findRegion("bernie"));
            Gdx.app.error("PlayScreen", "Map has no 'berniespawn' layer! Fell back to spawning at (0, 0).");
        }

        if (this.map.getLayers().get("exit") != null) {
            MapObject exitSpawnMapObject = this.map.getLayers().get("exit").getObjects().get(0);
            if (exitSpawnMapObject != null) {
                new GoalEntity(this.world, ((RectangleMapObject) exitSpawnMapObject).getRectangle());
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
        this.protagonist.handleInput();
    }

    public void update(float delta) {
        handleInput(delta);

        // update world
        this.world.step(1 / 60f, 6, 2);

        EntityManager.updateAllEntities(delta);

        this.camera.position.set(this.protagonist.getPosition(), 0);    // center camera on protagonist
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

        // render protagonist
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.spriteBatch.begin();
        EntityManager.drawAllEntities(this.spriteBatch);
        this.spriteBatch.end();

        // render HUD
        this.spriteBatch.setProjectionMatrix(this.hud.getStageCamera());
        this.hud.drawStage();
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
}
