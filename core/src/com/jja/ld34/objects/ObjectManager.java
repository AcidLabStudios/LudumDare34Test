package com.jja.ld34.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectManager {

    private static long nextId = 0;
    private static HashMap<Long, Object> registeredObjects = new HashMap<Long, Object>();

    public static boolean isObjectRegistered(String id) {
        return registeredObjects.containsKey(id);
    }

    public static long registerObject(Object object) {
        registeredObjects.put(nextId, object);
        return nextId++;
    }

    public static void deregisterObject(Object object) {
        registeredObjects.remove(object.getId());
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
