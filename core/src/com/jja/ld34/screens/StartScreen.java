package com.jja.ld34.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jja.ld34.Ld34Game;
import com.jja.ld34.objects.ObjectManager;

import java.awt.event.InputEvent;

/**
 * Created by andrewstrauch on 12/14/15.
 */

public class StartScreen implements Screen, ContactListener, InputProcessor {
    //public static AssetManager manager;
    
    private SpriteBatch spriteBatch;
    private Texture splsh;
    private Game myGame;

    public StartScreen(Game g) {
        myGame = g;
        
        /*manager = new AssetManager();
        manager.load("feelthebernmetal.mp3", Music.class);
        //manager.load(); add in SFX per line
        manager.finishLoading();

        //music = manager.get("feelthebernmetal.mp3", Music.class);
        music.setLooping(true);
        music.play();*/
        
        
    }
    
    @Override
    public void beginContact(Contact contact) {
        
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
    public void show() {
        spriteBatch = new SpriteBatch();
        splsh = new Texture(Gdx.files.internal("startscreen/bernie.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(splsh, 0, 0);
        spriteBatch.end();
        
        
        if (Gdx.input.justTouched()){
            myGame.setScreen(new PlayScreen());
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        splsh.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
