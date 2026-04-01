package com.shatteredpixel.shatteredpixeldungeon.items.weapon.elements;

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

    public static Element random() {
        if (Random.Int(10) == 0) { // 10% chance for an element
            return values()[Random.Int(1, values().length)];
        }
        return NONE;
    }
}
