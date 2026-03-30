/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.elements;

import com.watabou.utils.Random;

public enum Element {
	NONE, ABYSSAL, LUMINOUS, SANGUINE, CHAOTIC, INFERNAL, GLACIAL, GILDED, VOLTAIC, CAUSTIC, ZEPHYR, TERRAN;

	public static Element random() {
		if (Random.Int(5) == 0) { // 20% chance for any element
			return values()[Random.Int(1, values().length - 1)]; // Skip NONE
		}
		return NONE;
	}
}