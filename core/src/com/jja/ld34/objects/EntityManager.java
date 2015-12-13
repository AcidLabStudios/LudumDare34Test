package com.jja.ld34.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class EntityManager {

    private static HashMap<String, Entity> entities = new HashMap<String, Entity>();;

    public static void registerEntity(Entity entity) {
        entities.put(entity.uniqueName, entity);
    }

    public static void deregisterEntity(Entity entity) {
        entities.remove(entity.uniqueName);
    }

    public static void deregisterAllEntities() {
        entities.clear();
    }

    public static void updateAllEntities(float delta) {
        for (Entity entity : entities.values()) {
            entity.update(delta);
        }
    }

    public static void drawAllEntities(SpriteBatch spriteBatch) {
        for (Entity entity : entities.values()) {
            entity.draw(spriteBatch);
        }
    }
}
