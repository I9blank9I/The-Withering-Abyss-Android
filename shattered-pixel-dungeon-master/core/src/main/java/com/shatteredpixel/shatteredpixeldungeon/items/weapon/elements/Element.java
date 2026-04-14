package com.shatteredpixel.shatteredpixeldungeon.items.weapon.elements;

import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public enum Element {
    NONE,
    ABYSSAL,
    LUMINOUS,
    SANGUINE,
    CHAOTIC,
    INFERNAL,
    GLACIAL,
    GILDED,
    VOLTAIC,
    CAUSTIC,
    ZEPHYR,
    TERRAN;

    private boolean seen = false;

    public static Element random() {
        if (Random.Int(10) == 0) { // 10% chance for an element
            return values()[Random.Int(1, values().length)];
        }
        return NONE;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen() {
        if (!seen) {
            seen = true;
            Journal.saveNeeded = true;
        }
    }

    public static void store(Bundle bundle) {
        boolean[] seenArray = new boolean[values().length];
        for (int i = 0; i < values().length; i++) {
            seenArray[i] = values()[i].seen;
        }
        bundle.put("elements_seen", seenArray);
    }

    public static void restore(Bundle bundle) {
        if (bundle.contains("elements_seen")) {
            boolean[] seenArray = bundle.getBooleanArray("elements_seen");
            for (int i = 0; i < seenArray.length && i < values().length; i++) {
                values()[i].seen = seenArray[i];
            }
        }
    }
}
