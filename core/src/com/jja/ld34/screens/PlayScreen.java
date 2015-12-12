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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.scenes.Hud;
import com.jja.ld34.sprites.Protagonist;

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

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : this.map.getLayers().get(2).getObjects()) {
            com.badlogic.gdx.math.Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (rect.getY() + rect.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);

            body = this.world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / Ld34Game.PIXELS_PER_METER, (rect.getHeight() / 2) / Ld34Game.PIXELS_PER_METER);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        this.protagonist = new Protagonist(this.world, this.textureAtlas.findRegion("bernie"));
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

        // update protagonist
        this.protagonist.update(delta);

        this.camera.position.set(this.protagonist.getPosition(), 0);    // center camera on protagonist
        this.camera.update();
        this.mapRenderer.setView(this.camera);
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
        this.protagonist.draw(this.spriteBatch);
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
