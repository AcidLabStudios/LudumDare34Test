package com.jja.ld34;


public class FixtureFilterBit {

    public static final short DEFAULT_BIT = 1;
    public static final short ENVIRONMENT_BIT = 2;
    public static final short PROTAGONIST_BIT = 4;
    public static final short COLLECTIBLES_BIT = 8;
    public static final short ENEMY_BIT = 16;
    public static final short PROJECTILE_BIT = 32;
    public static final short TURRET_BIT = 64;
    public static final short ALL_FLAGS = DEFAULT_BIT | ENVIRONMENT_BIT | PROTAGONIST_BIT | COLLECTIBLES_BIT | ENEMY_BIT | PROJECTILE_BIT | TURRET_BIT;

    public static boolean contains(short b1, short b2) {
        return (b1 & b2) != 0;
    }
}
