package com.jja.ld34.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jja.ld34.Ld34Game;

public class Hud implements Disposable {

    public static Integer timeLeft = 0;
    public static Integer exitPartsCount = 5;
    public static String traitDescription = "";

    private Stage stage;
    private Viewport viewport;

    private Label timeLeftLabel;
    private Label exitPartsLabel;
    private Label traitDescriptionLabel;

    public Hud(SpriteBatch spriteBatch) {
        this.viewport = new FitViewport(Ld34Game.GAME_WIDTH, Ld34Game.GAME_HEIGHT, new OrthographicCamera());
        this.stage = new Stage(this.viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        this.timeLeftLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        this.exitPartsLabel = new Label("PARTS", new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));

        table.add(this.timeLeftLabel).expandX().padTop(10);
        table.add(this.exitPartsLabel).expandX().padTop(10);
        table.row();
        
        this.timeLeftLabel = new Label(timeLeft.toString(), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        table.add(this.timeLeftLabel).expandX();
        this.exitPartsLabel = new Label(exitPartsCount.toString(), new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        table.add(this.exitPartsLabel).expandX();

        this.stage.addActor(table);

        table = new Table();
        table.bottom();
        table.setFillParent(true);
        this.traitDescriptionLabel = new Label(traitDescription, new Label.LabelStyle(new BitmapFont(), Color.FIREBRICK));
        table.add(this.traitDescriptionLabel).expandX().padBottom(10);
        table.row();

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
    
    public void update() {
        this.timeLeftLabel.setText(timeLeft.toString());
        this.exitPartsLabel.setText(exitPartsCount.toString());
        this.traitDescriptionLabel.setText(traitDescription);
    }
}
