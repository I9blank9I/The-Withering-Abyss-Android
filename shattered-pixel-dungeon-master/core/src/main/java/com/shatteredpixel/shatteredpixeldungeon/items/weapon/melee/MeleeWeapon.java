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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Guided;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWeapon;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Blessing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.elements.Element;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MeleeWeapon extends Weapon {

	public enum Grade {
		BROKEN, RUSTY, USED, MAINTAINED, FLAWLESS, MASTERWORK;
		
		public static Grade random() {
			int roll = Random.Int(100);
			if (roll < 40) return BROKEN;
			if (roll < 70) return RUSTY;
			if (roll < 85) return USED;
			if (roll < 95) return MAINTAINED;
			if (roll < 99) return FLAWLESS;
			return MASTERWORK;
		}
	}

	public Grade grade = Grade.random();
	private static final String GRADE = "grade";
	
	public Element element = Element.random();
	private static final String ELEMENT = "element";
	
	public Blessing blessing;
	private static final String BLESSING = "blessing";
	
	private int weaponLevel = 0;
	private int weaponXP = 0;
	private static final String WEAPON_LEVEL = "weapon_level";
	private static final String WEAPON_XP = "weapon_xp";
	
	private ArrayList<Enchantment> additionalEnchantments = new ArrayList<>();
	private static final String ADDITIONAL_ENCHANTMENTS = "additional_enchantments";
	
	public ArrayList<Enchantment> getAllEnchantments() {
		ArrayList<Enchantment> all = new ArrayList<>();
		if (enchantment != null) all.add(enchantment);
		all.addAll(additionalEnchantments);
		return all;
	}
	
	public void gainXP(int xp) {
		if (grade != Grade.MASTERWORK) return;
		weaponXP += xp;
		while (weaponXP >= (weaponLevel + 1) * 100 && weaponLevel < 60) {
			weaponLevel++;
			// Unlock additional enchantments at certain levels
			if (weaponLevel >= 10 && additionalEnchantments.size() < 1) {
				additionalEnchantments.add(Enchantment.random());
			} else if (weaponLevel >= 30 && additionalEnchantments.size() < 2) {
				additionalEnchantments.add(Enchantment.random());
			} else if (weaponLevel >= 60 && additionalEnchantments.size() < 3) {
				additionalEnchantments.add(Enchantment.random());
			}
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GRADE, grade.name());
		bundle.put(ELEMENT, element.name());
		bundle.put(WEAPON_LEVEL, weaponLevel);
		bundle.put(WEAPON_XP, weaponXP);
		bundle.put(ADDITIONAL_ENCHANTMENTS, additionalEnchantments);
		bundle.put(BLESSING, blessing);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(GRADE)) {
			grade = Grade.valueOf(bundle.getString(GRADE));
		}
		if (bundle.contains(ELEMENT)) {
			element = Element.valueOf(bundle.getString(ELEMENT));
		}
		weaponLevel = bundle.getInt(WEAPON_LEVEL);
		weaponXP = bundle.getInt(WEAPON_XP);
		additionalEnchantments = new ArrayList<>();
		for (Bundlable b : bundle.getCollection(ADDITIONAL_ENCHANTMENTS)) {
			if (b != null) additionalEnchantments.add((Enchantment) b);
		}
		blessing = (Blessing) bundle.get(BLESSING);
	}

	public static String AC_ABILITY = "ABILITY";

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.DUELIST){
			Buff.affect(ch, Charger.class);
		}
	}

	@Override
	public String defaultAction() {
		if (Dungeon.hero != null && (Dungeon.hero.heroClass == HeroClass.DUELIST
			|| Dungeon.hero.hasTalent(Talent.SWIFT_EQUIP))){
			return AC_ABILITY;
		} else {
			return super.defaultAction();
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && hero.heroClass == HeroClass.DUELIST){
			actions.add(AC_ABILITY);
		}
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (action.equals(AC_ABILITY)){
			return Messages.upperCase(Messages.get(this, "ability_name"));
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_ABILITY)){
			usesTargeting = false;
			if (!isEquipped(hero)) {
				if (hero.hasTalent(Talent.SWIFT_EQUIP)){
					if (hero.buff(Talent.SwiftEquipCooldown.class) == null
						|| hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
						execute(hero, AC_EQUIP);
					} else if (hero.heroClass == HeroClass.DUELIST) {
						GLog.w(Messages.get(this, "ability_need_equip"));
					}
				} else if (hero.heroClass == HeroClass.DUELIST) {
					GLog.w(Messages.get(this, "ability_need_equip"));
				}
			} else if (hero.heroClass != HeroClass.DUELIST){
				//do nothing
			} else if (STRReq() > hero.STR()){
				GLog.w(Messages.get(this, "ability_low_str"));
			} else if ((Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge) < abilityChargeUse(hero, null)) {
				GLog.w(Messages.get(this, "ability_no_charge"));
			} else {

				if (targetingPrompt() == null){
					duelistAbility(hero, hero.pos);
					updateQuickslot();
				} else {
					usesTargeting = useTargeting();
					GameScene.selectCell(new CellSelector.Listener() {
						@Override
						public void onSelect(Integer cell) {
							if (cell != null) {
								duelistAbility(hero, cell);
								updateQuickslot();
							}
						}

						@Override
						public String prompt() {
							return targetingPrompt();
						}
					});
				}
			}
		}
	}

	//leave null for no targeting
	public String targetingPrompt(){
		return null;
	}

	public boolean useTargeting(){
		return targetingPrompt() != null;
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return dst; //weapon abilities do not use projectile logic, no autoaim
	}

	protected void duelistAbility( Hero hero, Integer target ){
		//do nothing by default
	}

	protected void beforeAbilityUsed(Hero hero, Char target){
		hero.belongings.abilityWeapon = this;
		Charger charger = Buff.affect(hero, Charger.class);

		charger.partialCharge -= abilityChargeUse(hero, target);
		while (charger.partialCharge < 0 && charger.charges > 0) {
			charger.charges--;
			charger.partialCharge++;
		}

		if (hero.heroClass == HeroClass.DUELIST
				&& hero.hasTalent(Talent.AGGRESSIVE_BARRIER)
				&& (hero.HP / (float)hero.HT) <= 0.5f){
			int shieldAmt = 1 + 2*hero.pointsInTalent(Talent.AGGRESSIVE_BARRIER);
			Buff.affect(hero, Barrier.class).setShield(shieldAmt);
			hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shieldAmt), FloatingText.SHIELDING);
		}

		updateQuickslot();
	}

	protected void afterAbilityUsed( Hero hero ){
		hero.belongings.abilityWeapon = null;
		if (hero.hasTalent(Talent.PRECISE_ASSAULT)){
			Buff.prolong(hero, Talent.PreciseAssaultTracker.class, hero.cooldown()+4f);
		}
		if (hero.hasTalent(Talent.VARIED_CHARGE)){
			Talent.VariedChargeTracker tracker = hero.buff(Talent.VariedChargeTracker.class);
			if (tracker == null || tracker.weapon == getClass() || tracker.weapon == null){
				Buff.affect(hero, Talent.VariedChargeTracker.class).weapon = getClass();
			} else {
				tracker.detach();
				Charger charger = Buff.affect(hero, Charger.class);
				charger.gainCharge(hero.pointsInTalent(Talent.VARIED_CHARGE) / 6f);
				ScrollOfRecharging.charge(hero);
			}
		}
		if (hero.hasTalent(Talent.COMBINED_LETHALITY)) {
			Talent.CombinedLethalityAbilityTracker tracker = hero.buff(Talent.CombinedLethalityAbilityTracker.class);
			if (tracker == null || tracker.weapon == this || tracker.weapon == null){
				Buff.affect(hero, Talent.CombinedLethalityAbilityTracker.class, hero.cooldown()).weapon = this;
			} else {
				//we triggered the talent, so remove the tracker
				tracker.detach();
			}
		}
		if (hero.hasTalent(Talent.COMBINED_ENERGY)){
			Talent.CombinedEnergyAbilityTracker tracker = hero.buff(Talent.CombinedEnergyAbilityTracker.class);
			if (tracker == null || !tracker.monkAbilused){
				Buff.prolong(hero, Talent.CombinedEnergyAbilityTracker.class, 5f).wepAbilUsed = true;
			} else {
				tracker.wepAbilUsed = true;
				Buff.affect(hero, MonkEnergy.class).processCombinedEnergy(tracker);
			}
		}
		if (hero.buff(Talent.CounterAbilityTacker.class) != null){
			Charger charger = Buff.affect(hero, Charger.class);
			charger.gainCharge(hero.pointsInTalent(Talent.COUNTER_ABILITY)*0.375f);
			hero.buff(Talent.CounterAbilityTacker.class).detach();
		}
	}

	public static void onAbilityKill( Hero hero, Char killed ){
		if (killed.alignment == Char.Alignment.ENEMY && hero.hasTalent(Talent.LETHAL_HASTE)){
			//effectively 3/5 turns of greater haste
			Buff.affect(hero, GreaterHaste.class).set(2 + 2*hero.pointsInTalent(Talent.LETHAL_HASTE));
		}
	}

	protected int baseChargeUse(Hero hero, Char target){
		return 1; //abilities use 1 charge by default
	}

	@Override
	public Weapon enchant(Enchantment ench) {
		// For Masterwork weapons, convert curses to blessings
		if (grade == Grade.MASTERWORK && ench != null && ench.curse()) {
			blessing = Blessing.random();
			// Don't set the curse enchantment, replace it with a random non-curse enchantment
			ench = Enchantment.random();
		}
		
		Weapon result = super.enchant(ench);
		
		// Chance to get a blessing based on weapon grade
		float blessingChance = 0.15f; // 15% for non-Masterwork
		if (grade == Grade.MASTERWORK) {
			blessingChance = 1.0f; // 100% for Masterwork
		}
		
		if (blessing == null && Random.Float() < blessingChance) {
			blessing = Blessing.random();
		}
		
		return result;
	}

	@Override
	public Item upgrade(boolean enchant) {
		if (grade == Grade.BROKEN && Random.Int(2) == 0) {
			grade = Grade.RUSTY;
			GLog.p("The upgrade scroll repaired the broken weapon into a rusty one!");
			return this;
		}
		Item result = super.upgrade(enchant);
		if (grade == Grade.MASTERWORK) {
			cursed = false; // Masterwork is always blessed
		}
		return result;
	}

	public final float abilityChargeUse(Hero hero, Char target){
		return baseChargeUse(hero, target);
	}

	public int tier;

	@Override
	public int min() {
		int baseMin = min(0);
		int lvl = level();
		int addedMin = 0;
		switch(grade) {
			case BROKEN: addedMin = 0; break;
			case RUSTY: addedMin = 0; break;
			case USED: addedMin = lvl; break;
			case MAINTAINED: addedMin = lvl; break;
			case FLAWLESS: addedMin = lvl * 2; break;
			case MASTERWORK: addedMin = lvl * 3; break;
		}
		return baseMin + addedMin;
	}

	@Override
	public int max() {
		int baseMax = max(0);
		int lvl = level();
		int addedMax = 0;
		switch(grade) {
			case BROKEN: addedMax = lvl; break;
			case RUSTY: addedMax = lvl * (tier / 2); break;
			case USED: addedMax = lvl * tier; break;
			case MAINTAINED: addedMax = lvl * (tier + 1); break;
			case FLAWLESS: addedMax = lvl * (tier + 2); break;
			case MASTERWORK: addedMax = lvl * (tier * 2); break;
		}
		return baseMax + addedMax;
	}

	@Override
	public int min(int lvl) {
		return  tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl) {
		return  5*(tier+1) +    //base
				lvl*(tier+1);   //level scaling
	}

	public int STRReq(int lvl){
		int req = STRReq(tier, 0);
		int reduction = 0;
		switch(grade) {
			case BROKEN: reduction = 0; break;
			case RUSTY: reduction = lvl / 2; break;
			case USED: reduction = lvl; break;
			case MAINTAINED: reduction = lvl; break;
			case FLAWLESS: reduction = lvl; break;
			case MASTERWORK: reduction = lvl * 2; break;
		}
		req -= reduction;
		if (masteryPotionBonus){
			req -= 2;
		}
		return Math.max(1, req);
	}

	private static boolean evaluatingTwinUpgrades = false;
	@Override
	public int buffedLvl() {
		if (!evaluatingTwinUpgrades && Dungeon.hero != null && isEquipped(Dungeon.hero) && Dungeon.hero.hasTalent(Talent.TWIN_UPGRADES)){
			KindOfWeapon other = null;
			if (Dungeon.hero.belongings.weapon() != this) other = Dungeon.hero.belongings.weapon();
			if (Dungeon.hero.belongings.secondWep() != this) other = Dungeon.hero.belongings.secondWep();

			if (other instanceof MeleeWeapon) {
				evaluatingTwinUpgrades = true;
				int otherLevel = other.buffedLvl();
				evaluatingTwinUpgrades = false;

				//weaker weapon needs to be 2/1/0 tiers lower, based on talent level
				if ((tier + (3 - Dungeon.hero.pointsInTalent(Talent.TWIN_UPGRADES))) <= ((MeleeWeapon) other).tier
						&& otherLevel > super.buffedLvl()) {
					return otherLevel;
				}

			}
		}
		return super.buffedLvl();
	}

	@Override
	public int damageRoll(Char owner) {
		int damage;
		
		// Chaotic element: 1-100 damage instead of normal range
		if (element == Element.CHAOTIC) {
			damage = Random.Int(100) + 1;
		} else {
			damage = augment.damageFactor(super.damageRoll( owner ));
		}
		
		float multiplier = 1.0f;
		switch (grade) {
			case BROKEN: multiplier = 0.50f; break;
			case RUSTY: multiplier = 0.75f; break;
			case USED: multiplier = 1.0f; break;
			case MAINTAINED: multiplier = 1.15f; break;
			case FLAWLESS: multiplier = 1.30f; break;
			case MASTERWORK: multiplier = 1.50f; break;
		}
		damage = (int)(damage * multiplier);

		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Hero.heroDamageIntRange( 0, exStr );
			}
		}
		return damage;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		int finalDamage = damage;
		
		// Gain XP for Masterwork
		gainXP(damage);
		
		// Handle blessing effects for all grades
		if (blessing != null) {
			finalDamage = blessing.proc(this, attacker, defender, finalDamage, grade == Grade.MASTERWORK ? weaponLevel : 0);
		}

		// Handle elemental effects
		finalDamage = procElemental(attacker, defender, finalDamage);

		float chanceMult = 1.0f;
		switch(grade) {
			case BROKEN: chanceMult = 0.1f; break;
			case RUSTY: chanceMult = 0.5f; break;
			case USED: chanceMult = 1.0f; break;
			case MAINTAINED: chanceMult = 1.5f; break;
			case FLAWLESS: chanceMult = 2.0f; break;
			case MASTERWORK: chanceMult = 10.0f; break;
		}

		// Get all enchantments
		ArrayList<Enchantment> allEnchants = getAllEnchantments();
		
		for (Enchantment ench : allEnchants) {
			if (chanceMult < 1.0f) {
				if (Random.Float() < chanceMult) {
					finalDamage = ench.proc(this, attacker, defender, finalDamage);
				}
			} else {
				finalDamage = ench.proc(this, attacker, defender, finalDamage);
				if (chanceMult > 1.0f && Random.Float() < (chanceMult - 1.0f)) {
					finalDamage = ench.proc(this, attacker, defender, finalDamage);
				}
			}
		}
		
		return finalDamage;
	}

	@Override
	public String name() {
		String name = super.name();
		
		// Add grade prefix (capitalize first letter)
		String gradeStr = grade.name().toLowerCase();
		gradeStr = gradeStr.substring(0, 1).toUpperCase() + gradeStr.substring(1);
		name = gradeStr + " " + name;
		
		// Add element (capitalize first letter)
		if (element != Element.NONE) {
			String elementStr = element.name().toLowerCase();
			elementStr = elementStr.substring(0, 1).toUpperCase() + elementStr.substring(1);
			name = elementStr + " " + name;
		}
		
		// Add blessing
		if (blessing != null) {
			name += " of " + blessing.name();
		}
		
		return name;
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = super.reachFactor(owner);
		// Zephyr element: +1 reach
		if (element == Element.ZEPHYR) {
			reach += 1;
		}
		return reach;
	}

	private int procElemental(Char attacker, Char defender, int damage) {
		if (element == Element.NONE) return damage;

		switch (element) {
			case ABYSSAL:
				// Blind enemy and extinguish lights
				if (Random.Int(4) == 0) {
					Buff.prolong(defender, Blindness.class, Blindness.DURATION);
				}
				break;
			case LUMINOUS:
				// Holy damage to undead/demonic, reveal invisibility
				if (defender.properties().contains(Char.Property.UNDEAD) || 
					defender.properties().contains(Char.Property.DEMONIC)) {
					damage = (int)(damage * 1.5f); // Holy damage bonus
				}
				// Reveal invisibility
				Buff.detach(defender, Invisibility.class);
				break;
			case SANGUINE:
				// Damage based on missing health, create blood pools
				if (attacker instanceof Hero) {
					Hero hero = (Hero) attacker;
					float healthPercent = 1.0f - ((float)hero.HP / hero.HT);
					damage = (int)(damage * (1.0f + healthPercent)); // More damage when low HP
				}
				// Heal attacker from blood
				if (Random.Int(4) == 0 && attacker instanceof Hero) {
					int heal = Math.max(1, damage / 10);
					attacker.HP = Math.min(attacker.HT, attacker.HP + heal);
					attacker.sprite.emitter().burst(BloodParticle.FACTORY, 5);
				}
				break;
			case CHAOTIC:
				// Random damage variance and random debuffs
				// Damage variance is handled in damageRoll override
				// Random debuff
				if (Random.Int(6) == 0) {
					// Using a raw 'Class' bypasses the strict generic check
					Class debuff = (Class) Random.oneOf(
							Weakness.class, Vulnerable.class, Cripple.class,
							Blindness.class, Terror.class, Slow.class, Hex.class
					);
					// We add an 'f' to 3 so Java knows it is a float!
					Buff.affect(defender, debuff, 3f + buffedLvl());
				}
				break;
			case INFERNAL:
				// Fire damage, prevent regeneration
				if (Random.Int(3) == 0) {
					Buff.affect(defender, Burning.class).reignite(defender);
				}
				break;
			case GLACIAL:
				// Slow movement, can freeze
				if (Random.Int(4) == 0) {
					Buff.affect(defender, Frost.class, Frost.DURATION);
				} else if (Random.Int(8) == 0) {
					Buff.prolong(defender, Chill.class, Chill.DURATION * 2);
				}
				break;
			case GILDED:
				// More gold drops, damage from gold
				if (attacker instanceof Hero) {
					int goldBonus = Dungeon.gold / 10; // 10% of total gold as damage
					damage += goldBonus;
				}
				break;
			case VOLTAIC:
				// Chain lightning
				if (Random.Int(5) == 0) {
					// Simple chain lightning: damage nearby enemies
					for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
						int pos = defender.pos + PathFinder.NEIGHBOURS8[i];
						Char ch = Actor.findChar(pos);
						if (ch != null && ch != attacker && ch.alignment != attacker.alignment) {
							ch.damage(damage / 3, this);
							ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
						}
					}
				}
				break;
			case CAUSTIC:
				// Reduce enemy armor
				if (Random.Int(4) == 0) {
					Buff.affect(defender, Corrosion.class).set(5f, 1 + buffedLvl() / 3);
				}
				break;
			case ZEPHYR:
				// Extended reach (handled in reach factor)
				break;
			case TERRAN:
				// Knockback
				if (Random.Int(6) == 0) {
					Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.PROJECTILE);
					WandOfBlastWave.throwChar(defender, trajectory, 1, false, false, this);
				}
				break;
		}
		return damage;
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		Enchantment temp = null;
		if (grade == Grade.MASTERWORK && enchantment != null && enchantment.curse()) {
			temp = enchantment;
			enchantment = null; // Suppresses original curse's negative accuracy modifiers.
		}
		float acc = super.accuracyFactor(owner, target);
		if (temp != null) enchantment = temp;

		if (grade == Grade.MAINTAINED) acc *= 1.1f;
		if (grade == Grade.MASTERWORK) acc *= 1.5f;

		// Guided blessing provides guaranteed hits
		if (blessing instanceof Guided) {
			if (Random.Int(4) == 0) return Float.POSITIVE_INFINITY;
		}
		return acc;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		// Show blessing glow for blessed weapons
		if (blessing != null) {
			return blessing.glowing();
		}
		// Show elemental glow for elemental weapons
		if (element != Element.NONE) {
			return getElementalGlowing();
		}
		return super.glowing();
	}

	private ItemSprite.Glowing getElementalGlowing() {
		switch (element) {
			case ABYSSAL: return new ItemSprite.Glowing(0x000000); // Black
			case LUMINOUS: return new ItemSprite.Glowing(0xFFFFFF); // White
			case SANGUINE: return new ItemSprite.Glowing(0x8B0000); // Dark red
			case CHAOTIC: return new ItemSprite.Glowing(0xFF00FF); // Magenta
			case INFERNAL: return new ItemSprite.Glowing(0xFF4500); // Orange red
			case GLACIAL: return new ItemSprite.Glowing(0x00BFFF); // Deep sky blue
			case GILDED: return new ItemSprite.Glowing(0xFFD700); // Gold
			case VOLTAIC: return new ItemSprite.Glowing(0x00FFFF); // Cyan
			case CAUSTIC: return new ItemSprite.Glowing(0x32CD32); // Lime green
			case ZEPHYR: return new ItemSprite.Glowing(0xF0F8FF); // Alice blue
			case TERRAN: return new ItemSprite.Glowing(0x8B4513); // Saddle brown
			default: return null;
		}
	}

	@Override
	public float delayFactor(Char owner) {
		float d = super.delayFactor(owner);
		if (grade == Grade.FLAWLESS) d *= 0.9f; // 10% faster attacks!
		if (grade == Grade.MASTERWORK) d *= 0.75f; // 25% faster attacks!
		return d;
	}

	@Override
	public String info() {

		String info = super.info();
		
		info += "\n\nQuality Grade: " + grade.name();
		if (grade == Grade.MASTERWORK) {
			info += " (Level " + weaponLevel + ")";
		}

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (Dungeon.hero != null) {
				if (STRReq() > Dungeon.hero.STR()) {
					info += " " + Messages.get(Weapon.class, "too_heavy");
				} else if (Dungeon.hero.STR() > STRReq()) {
					info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
				}
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (Dungeon.hero != null && STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		String statsInfo = statsInfo();
		if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

		switch (augment) {
			case SPEED:
				info += " " + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += " " + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
				&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", Messages.get(HolyWeapon.class, "ench_name", Messages.get(Enchantment.class, "enchant"))));
			info += " " + Messages.get(HolyWeapon.class, "ench_desc");
		} else if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}

		// Additional enchantments for Masterwork
		if (grade == Grade.MASTERWORK && !additionalEnchantments.isEmpty()) {
			for (Enchantment ench : additionalEnchantments) {
				info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", ench.name()));
				info += " " + ench.desc();
			}
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			if (enchantment != null && enchantment.curse()) {
				info += "\n\n" + Messages.get(Weapon.class, "weak_cursed");
			} else {
				info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
			}
		}

		//the mage's staff has no ability as it can only be gained by the mage
		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.DUELIST && !(this instanceof MagesStaff)){
			info += "\n\n" + abilityInfo();
		}
		
		return info;
	}
	
	public String statsInfo(){
		return Messages.get(this, "stats_desc");
	}

	public String abilityInfo() {
		return Messages.get(this, "ability_desc");
	}

	public String upgradeAbilityStat(int level){
		return null;
	}

	@Override
	public String status() {
		if (isEquipped(Dungeon.hero)
				&& Dungeon.hero.buff(Charger.class) != null) {
			Charger buff = Dungeon.hero.buff(Charger.class);
			return buff.charges + "/" + buff.chargeCap();
		} else {
			return super.status();
		}
	}

	@Override
	public int value() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public static class Charger extends Buff implements ActionIndicator.Action {

		{
			//so that duelist keeps weapon charge on ankh revive
			revivePersists = true;
		}

		public int charges = 2;
		public float partialCharge;

		@Override
		public boolean act() {
			if (charges < chargeCap()){
				if (Regeneration.regenOn()){
					//60 to 45 turns per charge
					float chargeToGain = 1/(60f-1.5f*(chargeCap()-charges));

					//40 to 30 turns per charge for champion
					if (Dungeon.hero.subClass == HeroSubClass.CHAMPION){
						chargeToGain *= 1.5f;
					}

					//50% slower charge gain with brawler's stance enabled, even if buff is inactive
					if (Dungeon.hero.buff(RingOfForce.BrawlersStance.class) != null){
						chargeToGain *= 0.50f;
					}

					partialCharge += chargeToGain;
				}

				int points = ((Hero)target).pointsInTalent(Talent.WEAPON_RECHARGING);
				if (points > 0 && target.buff(Recharging.class) != null || target.buff(ArtifactRecharge.class) != null){
					//1 every 15 turns at +1, 10 turns at +2
					partialCharge += 1/(20f - 5f*points);
				}

				if (partialCharge >= 1){
					charges++;
					partialCharge--;
					updateQuickslot();
				}
			} else {
				partialCharge = 0;
			}

			if (ActionIndicator.action != this && Dungeon.hero.subClass == HeroSubClass.CHAMPION) {
				ActionIndicator.setAction(this);
			}

			spend(TICK);
			return true;
		}

		@Override
		public void fx(boolean on) {
			if (on && Dungeon.hero.subClass == HeroSubClass.CHAMPION) {
				ActionIndicator.setAction(this);
			}
		}

		@Override
		public void detach() {
			super.detach();
			ActionIndicator.clearAction(this);
		}

		public int chargeCap(){
			//caps at level 19 with 8 or 10 charges
			if (Dungeon.hero.subClass == HeroSubClass.CHAMPION){
				return Math.min(10, 4 + (Dungeon.hero.lvl - 1) / 3);
			} else {
				return Math.min(8, 2 + (Dungeon.hero.lvl - 1) / 3);
			}
		}

		public void gainCharge( float charge ){
			if (charges < chargeCap()) {
				partialCharge += charge;
				while (partialCharge >= 1f) {
					charges++;
					partialCharge--;
				}
				if (charges >= chargeCap()){
					partialCharge = 0;
					charges = chargeCap();
				}
				updateQuickslot();
			}
		}

		public static final String CHARGES          = "charges";
		private static final String PARTIALCHARGE   = "partialCharge";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CHARGES, charges);
			bundle.put(PARTIALCHARGE, partialCharge);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			charges = bundle.getInt(CHARGES);
			partialCharge = bundle.getFloat(PARTIALCHARGE);
		}

		@Override
		public String actionName() {
			return Messages.get(MeleeWeapon.class, "swap");
		}

		@Override
		public int actionIcon() {
			return HeroIcon.WEAPON_SWAP;
		}

		@Override
		public Visual primaryVisual() {
			Image ico;
			if (Dungeon.hero.belongings.weapon == null){
				ico = new HeroIcon(this);
 			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.weapon);
			}
			ico.width += 4; //shift slightly to the left to separate from smaller icon
			return ico;
		}

		@Override
		public Visual secondaryVisual() {
			Image ico;
			if (Dungeon.hero.belongings.secondWep == null){
				ico = new HeroIcon(this);
			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.secondWep);
			}
			ico.scale.set(PixelScene.align(0.51f));
			ico.brightness(0.6f);
			return ico;
		}

		@Override
		public int indicatorColor() {
			return 0x5500BB;
		}

		@Override
		public void doAction() {
			if (Dungeon.hero.subClass != HeroSubClass.CHAMPION){
				return;
			}

			if (Dungeon.hero.belongings.secondWep == null && Dungeon.hero.belongings.backpack.items.size() >= Dungeon.hero.belongings.backpack.capacity()){
				GLog.w(Messages.get(MeleeWeapon.class, "swap_full"));
				return;
			}

			KindOfWeapon temp = Dungeon.hero.belongings.weapon;
			Dungeon.hero.belongings.weapon = Dungeon.hero.belongings.secondWep;
			Dungeon.hero.belongings.secondWep = temp;

			Dungeon.hero.sprite.operate(Dungeon.hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

			ActionIndicator.setAction(this);
			Item.updateQuickslot();
			AttackIndicator.updateState();
		}
	}

}
