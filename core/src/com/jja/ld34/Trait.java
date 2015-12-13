package com.jja.ld34;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Trait {

    SLIPPERY(2),
    HYPER(2);

    private static Random rand = new Random();

    private int probability;

    Trait(int probability) {
        this.probability = probability;
    }

    public static List<Trait> getRandomTraits(int count) {
        List<Trait> traits = new ArrayList<Trait>();
        for (Trait trait : Trait.values()) {
            if (rand.nextInt(trait.probability) == 0) {
                traits.add(trait);
            }

            if (traits.size() >= count) {
                break;
            }
        }
        return traits;
    }
}
