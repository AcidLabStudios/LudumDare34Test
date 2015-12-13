package com.jja.ld34.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Timer;
import java.util.TimerTask;

public class Turret extends Sprite {

    private int fireInterval = 3000;
    private String fireDirection;
    
    private boolean canFire = true;
    private Timer timer = new Timer();
    
    public Turret (World world, TextureAtlas.AtlasRegion textureRegion) {
        super(textureRegion);
        
        //Probably make him a solid object like terrain.
    }
    
    public void update(float delta) {
        if (canFire) {
            timer.schedule(timerTask, fireInterval);
            canFire = false;
        }
    }

    protected TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //fire projectile at fireDirection
            //play fire animation
            canFire = true;
        }
    };

    public String getFireDirection() {
        return fireDirection;
    }
    public void setFireDirection(String fireDirection) {
        this.fireDirection = fireDirection;
    }
    public int getFireInterval() {
        return fireInterval;
    }
    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
    }
}
