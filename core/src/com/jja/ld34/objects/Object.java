package com.jja.ld34.objects;

public interface Object {

    String getId();
    boolean shouldDestroy();
    boolean isDestroyed();
    void update(float delta);
    void destroy();
}
