package com.jja.ld34.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jja.ld34.Ld34Game;

public class Hud implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Integer time;

    private Label timeLabel;

    public Hud(SpriteBatch spriteBatch) {
        this.viewport = new FitViewport(Ld34Game.GAME_WIDTH, Ld34Game.GAME_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(this.viewport, spriteBatch);

        this.time = 0;

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        this.timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(this.timeLabel).expandX().padTop(10);
        table.row();
        this.timeLabel = new Label(this.time.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(this.timeLabel).expandX();

        this.stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Matrix4 getStageCamera() {
        return this.stage.getCamera().combined;
    }

    public void drawStage() {
        this.stage.draw();
    }
}
