package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectManager {

    private static HashMap<String, Object> registeredObjects = new HashMap<String, Object>();

    public static void registerObject(Object object) {
        if (registeredObjects.containsKey(object.getId())) {
            Gdx.app.debug("ObjectManager", "Unable to register '" + object.getId() + "' because that id is already registered, and ids must be unique");
        }

        registeredObjects.put(object.getId(), object);
        Gdx.app.debug("ObjectManager", "Registered new object: " + object.getId());
    }

    public static void deregisterObject(Object object) {
        if (registeredObjects.remove(object.getId()) == null) {
            Gdx.app.error("ObjectManager", "Unable to deregister '" + object.getId() + "' because no object with that id is currently registered");
        } else {
            Gdx.app.debug("ObjectManager", "Deregistered object: " + object.getId());
        }
    }

    public static void deregisterAllObjects() {
        for (Iterator<Object> objects = registeredObjects.values().iterator(); objects.hasNext();) {
            Object object = objects.next();
            object.destroy();
        }
    }

    public static void updateAllObjects(float delta) {
        for (Iterator<Object> objects = registeredObjects.values().iterator(); objects.hasNext();) {
            Object object = objects.next();

            if (object.shouldDestroy() && !object.isDestroyed()) {
                object.destroy();
                break;
            }

            // only update objects that have not been destroyed
            object.update(delta);
        }
    }

    public static void drawAllEntities(SpriteBatch spriteBatch) {
        for (Iterator<Object> objects = registeredObjects.values().iterator(); objects.hasNext();) {
            Object object = objects.next();
            if (object instanceof Entity) {
                ((Entity) object).draw(spriteBatch);
            }
        }
    }
}
