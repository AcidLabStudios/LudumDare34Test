package com.jja.ld34;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Trait {

    SLIPPERY(2),
    HYPER(2),
    NAKED(2),
    REDTIE(2),
    COLONEL(2),
    REVERSE(2);

    private static Random rand = new Random();

    private int probability;

    Trait(int probability) {
        this.probability = probability;
    }

    public static List<Trait> getRandomTraits(int count) {
        List<Trait> traits = new ArrayList<Trait>();
        for (Trait trait : Trait.values()) {
            if ((trait == NAKED && (traits.contains(REDTIE) || traits.contains(COLONEL))) ||
                    (trait == REDTIE && (traits.contains(NAKED) || traits.contains(COLONEL))) ||
                    (trait == COLONEL && (traits.contains(NAKED) || traits.contains(REDTIE)))) {
                // we only currently support only one change in base player texture, so no red tie colonels, sorry!
                continue;
            }

            if (rand.nextInt(trait.probability) == 0) {
                traits.add(trait);
                if (traits.size() >= count) {
                    break;
                }
            }
        }
        return traits;
    }
}
