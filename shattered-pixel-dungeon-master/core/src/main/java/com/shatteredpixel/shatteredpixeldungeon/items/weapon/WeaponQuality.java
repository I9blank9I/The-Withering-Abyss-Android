package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public enum WeaponQuality {
    BROKEN(0.5f, 0.1f),
    RUSTY(0.75f, 0.5f),
    USED(1.0f, 1.0f),
    MAINTAINED(1.15f, 1.5f),
    FLAWLESS(1.30f, 2.0f),
    MASTERWORK(1.50f, 10.0f);

    public final float damageMultiplier;
    public final float procMultiplier;

    WeaponQuality(float damageMultiplier, float procMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.procMultiplier = procMultiplier;
    }

    public String title() {
        return Messages.get(this, name().toLowerCase());
    }
    }
