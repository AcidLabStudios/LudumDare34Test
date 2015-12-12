package com.jja.ld34.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by andrewstrauch on 12/12/15.
 */
//Object that Bernie has to collect to complete the exit to the next level.
public class ExitPart extends Collectable{
    public ExitPart(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
    
    //Need to handle the collision with the player somehow.
    
    public void onCollection() {
        //Increment to a count somewhere else keeping track of pieces gathered for that level,
        //and also update the display in the HUD to reflect the change.
    }
    public void destroy() {
        //Remove itself. Maybe should be in the parent
    }
}
