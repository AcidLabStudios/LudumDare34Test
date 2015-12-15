package com.jja.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
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
    //private Box2DDebugRenderer debugRenderer;

    private Player player;

    private Timer gameStateTimer;
    private int numPlayerDeaths;
    private boolean isGameOver;
    
    public static Integer currentLevel = 1;

    public PlayScreen() {
        this.spriteBatch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Ld34Game.GAME_WIDTH / Ld34Game.PIXELS_PER_METER, Ld34Game.GAME_HEIGHT / Ld34Game.PIXELS_PER_METER, this.camera);

        this.hud = new Hud(this.spriteBatch);
        
        loadMyMap("1-1.tmx");
        this.camera.position.set(this.viewport.getWorldWidth() / 2, this.viewport.getWorldHeight() / 2, 0);

        this.world = new World(new Vector2(0, 0), true);
        this.world.setContactListener(this);
        //this.debugRenderer = new Box2DDebugRenderer();

        this.numPlayerDeaths = 0;
        this.isGameOver = false;

        populateWorld();
        this.gameStateTimer = new Timer();
        this.gameStateTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (isGameOver) {
                    return;
                }

                updateGameState();
            }
        }, 1, 1);
        setNewTimeLeft();
    }
    
    public void loadMyMap(String myMap) {
        this.mapLoader = new TmxMapLoader();
        this.map = this.mapLoader.load(myMap);
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, 1 / Ld34Game.PIXELS_PER_METER);
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
            for (MapObject object : this.map.getLayers().get("exitparts").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new ExitPart(this.world, new Vector2(bounds.x, bounds.y));
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exitparts' layer!");
        }

        spawnPlayer();

        if (this.map.getLayers().get("exit") != null) {
            for (MapObject object : this.map.getLayers().get("exit").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new ExitPortal(this.world, new Vector2(bounds.x+ 15, bounds.y));
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exit' layer!");
        }
        
        
        /*if (this.map.getLayers().get("exit") != null) {
            MapObject exitSpawnMapObject = this.map.getLayers().get("exit").getObjects().get(0);
            if (exitSpawnMapObject != null) {
                new ExitPortal(this.world, ((RectangleMapObject) exitSpawnMapObject).getRectangle());
                Gdx.app.error("PlayScreen", "Exit Spawned!");
            } else {
                Gdx.app.error("PlayScreen", "Unable to find exit in 'exit' layer of map! No exit was spawned; there is no exit to this map.");
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'exit' layer!");
        }*/

        if (this.map.getLayers().get("turretspawn") != null) {
            for (MapObject object : this.map.getLayers().get("turretspawn").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new Turret(this.world, new Vector2(bounds.x, bounds.y), currentLevel);
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'turretspawn' layer!");
        }

        spawnTrumps();
    }

    public void spawnPlayer() {
        if (this.map.getLayers().get("berniespawn") != null) {
            MapObject protagonistSpawnMapObject = this.map.getLayers().get("berniespawn").getObjects().get(0);
            if (protagonistSpawnMapObject != null) {
                Rectangle bounds = ((RectangleMapObject) protagonistSpawnMapObject).getRectangle();
                this.player = new Player(this.world, new Vector2(bounds.x, bounds.y));
            } else {
                this.player = new Player(this.world, new Vector2(0, 0));
                Gdx.app.error("PlayScreen", "Unable to find spawnpoint for player in 'berniespawn' layer of map! Fell back to spawning at (0, 0).");
            }
        } else {
            this.player = new Player(this.world, new Vector2(0, 0));
            Gdx.app.error("PlayScreen", "Map has no 'berniespawn' layer! Fell back to spawning at (0, 0).");
        }
    }

    public void spawnTrumps() {
        if (this.map.getLayers().get("trumpspawn") != null) {
            for (MapObject object : this.map.getLayers().get("trumpspawn").getObjects()) {
                Rectangle bounds = ((RectangleMapObject) object).getRectangle();
                new TrumpClone(this.world, new Vector2(bounds.x, bounds.y));
            }
        } else {
            Gdx.app.error("PlayScreen", "Map has no 'trumpspawn' layer!");
        }
    }

    public void handleInput(float delta) {
        if (this.player != null) {
            this.player.handleInput();
        }
    }

    public void updateGameState() {
        // continue counting down time left
        if (Hud.timeLeft > 0) {
            Hud.timeLeft--;
        }

        if (Hud.timeLeft == 0) {    // respawn condition
            if (this.player != null && !this.player.isDying()) {
                this.player.kill();
            }
        }

        if (this.player != null && this.player.isDestroyed()) {
            this.numPlayerDeaths++;
            if (setNewTimeLeft() == 0) {
                // GAME OVER BITCH
                // TODO: restart the current level
                Gdx.app.error("PlayScreen", "GAME OVER");
                isGameOver = true;
            } else {
                // if there isn't currently a player on the field and there's still time on the clock, spawn a new player
                spawnPlayer();
                spawnTrumps();  // also respawn all trumps
            }
        }
        
        if(ExitPortal.hasBeenActivated){
            this.isGameOver = true;
            ObjectManager.deregisterAllObjects();
            ExitPortal.hasBeenActivated = false;
            currentLevel++;
            this.map.dispose();
            if(currentLevel == 2){
                loadMyMap("1-2.tmx");
            } else if (currentLevel == 3){
                loadMyMap("1-3.tmx");
            } else if (currentLevel == 4){
                loadMyMap("1-4.tmx");
            }
            populateWorld();
            numPlayerDeaths = 0;
            Hud.exitPartsCount = 0;
            setNewTimeLeft();
            isGameOver = false;
        }
    }

    public int setNewTimeLeft() {
        int newTimeLeft = Math.max(((int) (60f / (this.numPlayerDeaths + 1))) - (this.numPlayerDeaths + 1), 0);
        Hud.timeLeft = newTimeLeft;
        return newTimeLeft;
    }

    public void update(float delta) {
        handleInput(delta);

        // update world
        this.world.step(1 / 60f, 6, 2);

        ObjectManager.updateAllObjects(delta);

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
        //this.debugRenderer.render(world, camera.combined);

        // render player
        this.spriteBatch.setProjectionMatrix(camera.combined);
        this.spriteBatch.begin();
        ObjectManager.drawAllEntities(this.spriteBatch);
        this.spriteBatch.end();

        // render HUD
        this.spriteBatch.setProjectionMatrix(this.hud.getStageCamera());
        this.hud.drawStage();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() instanceof InteractiveObject) {
            ((InteractiveObject) fixtureA.getUserData()).onCollision(fixtureB.getFilterData().categoryBits);
        }

        if (fixtureB.getUserData() instanceof InteractiveObject) {
            ((InteractiveObject) fixtureB.getUserData()).onCollision(fixtureA.getFilterData().categoryBits);
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
    public void show() {
    }

    @Override
    public void dispose() {
        this.map.dispose();
        this.mapRenderer.dispose();
        this.world.dispose();
        //this.debugRenderer.dispose();
        this.hud.dispose();
    }
}
