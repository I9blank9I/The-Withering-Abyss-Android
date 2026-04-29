package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Smite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Guided;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Dazzling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Explosive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.elements.Element;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class Weapon extends KindOfWeapon {

	public float    ACC = 1f;  // Accuracy modifier
	public float   DLY    = 1f;  // Speed modifier
	public int      RCH = 1;    // Reach modifier (only applies to melee hits)

	public enum Augment {
		SPEED   (0.7f, 2/3f),
		DAMAGE  (1.5f, 5/3f),
		NONE   (1.0f, 1f);

		private float damageFactor;
		private float delayFactor;

		Augment(float dmg, float dly){
			damageFactor = dmg;
			delayFactor = dly;
		}

		public int damageFactor(int dmg){
			return Math.round(dmg * damageFactor);
		}

		public float damageFactor(float dmg){
			return dmg * damageFactor;
		}

		public float delayFactor(float dly){
			return dly * delayFactor;
		}
	}

	public Augment augment = Augment.NONE;

	// --- CUSTOM VARIABLES (Quality, Elements, Blessings) ---
	public WeaponQuality quality = WeaponQuality.USED;
	public Element element = Element.random();
	public com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Blessing blessing;

	public void applyQuality( WeaponQuality q ) { this.quality = q; }
	public void applyBlessing( com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Blessing b ) { this.blessing = b; }

	protected int usesToID(){ return 0; }
	protected float usesLeftToID = usesToID();
	protected float availableUsesToID = usesToID()/2f;

	public Enchantment enchantment;
	public boolean enchantHardened = false;
	public boolean curseInfusionBonus = false;
	public boolean masteryPotionBonus = false;

	@Override
	public int proc( Char attacker, Char defender, int damage ) {

		if (this.blessing != null && attacker instanceof Hero) {
			damage = this.blessing.proc(this, attacker, defender, damage, level());
		}

		boolean becameAlly = false;
		boolean wasAlly = defender.alignment == Char.Alignment.ALLY;
		if (attacker.buff(MagicImmune.class) == null) {
			Enchantment trinityEnchant = null;
			if (Dungeon.hero.buff(BodyForm.BodyFormBuff.class) != null && this instanceof MeleeWeapon
					&& (attacker == Dungeon.hero || attacker instanceof MirrorImage || attacker instanceof ShadowClone.ShadowAlly)){
				trinityEnchant = Dungeon.hero.buff(BodyForm.BodyFormBuff.class).enchant();
				if (enchantment != null && trinityEnchant != null && trinityEnchant.getClass() == enchantment.getClass()){
					trinityEnchant = null;
				}
			}

			if (attacker instanceof Hero && isEquipped((Hero) attacker)
					&& attacker.buff(HolyWeapon.HolyWepBuff.class) != null){
				if (enchantment != null &&
						(((Hero) attacker).subClass == HeroSubClass.PALADIN || hasCurseEnchant())){
					damage = enchantment.proc(this, attacker, defender, damage);
					if (defender.alignment == Char.Alignment.ALLY && !wasAlly){
						becameAlly = true;
					}
				}
				if (defender.isAlive() && !becameAlly && trinityEnchant != null){
					damage = trinityEnchant.proc(this, attacker, defender, damage);
				}
				if (defender.isAlive() && !becameAlly) {
					int dmg = ((Hero) attacker).subClass == HeroSubClass.PALADIN ? 6 : 2;
					defender.damage(Math.round(dmg * Enchantment.genericProcChanceMultiplier(attacker)), HolyWeapon.INSTANCE);
				}
			} else {
				if (enchantment != null) {
					damage = enchantment.proc(this, attacker, defender, damage);
					if (defender.alignment == Char.Alignment.ALLY && !wasAlly){
						becameAlly = true;
					}
				}

				if (defender.isAlive() && !becameAlly && trinityEnchant != null){
					damage = trinityEnchant.proc(this, attacker, defender, damage);
				}
			}

			if (attacker instanceof Hero && isEquipped((Hero) attacker) &&
					attacker.buff(Smite.SmiteTracker.class) != null && !becameAlly){
				defender.damage(Smite.bonusDmg((Hero) attacker, defender), Smite.INSTANCE);
			}
		}

		// Proc Elements!
		damage = procElemental(attacker, defender, damage);

		if (this instanceof MissileWeapon
				&& ((MissileWeapon) this).durabilityLeft() <= ((MissileWeapon) this).durabilityPerUse()
				&& ((MissileWeapon) this).parent == null){
			return damage;
		}

		if (!levelKnown && attacker == Dungeon.hero) {
			float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
			availableUsesToID -= uses;
			usesLeftToID -= uses;
			if (usesLeftToID <= 0) {
				if (ShardOfOblivion.passiveIDDisabled()){
					if (usesLeftToID > -1){
						GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), name());
					}
					setIDReady();
				} else {
					identify();
					GLog.p(Messages.get(Weapon.class, "identify"));
					Badges.validateItemLevelAquired(this);
				}
			}
		}

		return damage;
	}

	private int procElemental(Char attacker, Char defender, int damage) {
		if (element == null || element == Element.NONE) return damage;

		switch (element) {
			case ABYSSAL:
				if (Random.Int(4) == 0) Buff.prolong(defender, Blindness.class, Blindness.DURATION);
				break;
			case LUMINOUS:
				if (defender.properties().contains(Char.Property.UNDEAD) ||
						defender.properties().contains(Char.Property.DEMONIC)) {
					damage = (int)(damage * 1.5f);
				}
				Buff.detach(defender, Invisibility.class);
				break;
			case SANGUINE:
				if (attacker instanceof Hero) {
					Hero hero = (Hero) attacker;
					float healthPercent = 1.0f - ((float)hero.HP / hero.HT);
					damage = (int)(damage * (1.0f + healthPercent));
				}
				if (Random.Int(4) == 0 && attacker instanceof Hero) {
					int heal = Math.max(1, damage / 10);
					attacker.HP = Math.min(attacker.HT, attacker.HP + heal);
					attacker.sprite.emitter().burst(BloodParticle.FACTORY, 5);
				}
				break;
			case CHAOTIC:
				if (Random.Int(6) == 0) {
					Class debuff = (Class) Random.oneOf(Weakness.class, Vulnerable.class, Cripple.class, Blindness.class, Terror.class, Slow.class, Hex.class);
					Buff.affect(defender, debuff, 3f + level());
				}
				break;
			case INFERNAL:
				if (Random.Int(3) == 0) Buff.affect(defender, Burning.class).reignite(defender);
				break;
			case GLACIAL:
				if (Random.Int(4) == 0) Buff.affect(defender, Frost.class, Frost.DURATION);
				else if (Random.Int(8) == 0) Buff.prolong(defender, Chill.class, Chill.DURATION * 2);
				break;
			case GILDED:
				if (attacker instanceof Hero) damage += Dungeon.gold / 10;
				break;
			case VOLTAIC:
				if (Random.Int(5) == 0) {
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
				if (Random.Int(4) == 0) Buff.affect(defender, Corrosion.class).set(5f, 1 + level() / 3);
				break;
			case ZEPHYR:
				break;
			case TERRAN:
				if (Random.Int(6) == 0) {
					Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.PROJECTILE);
					WandOfBlastWave.throwChar(defender, trajectory, 1, false, false, this);
				}
				break;
		}
		return damage;
	}

	public void onHeroGainExp( float levelPercent, Hero hero ){
		levelPercent *= Talent.itemIDSpeedFactor(hero, this);
		if (!levelKnown && (isEquipped(hero) || this instanceof MissileWeapon)
				&& availableUsesToID <= usesToID()/2f) {
			availableUsesToID = Math.min(usesToID()/2f, availableUsesToID + levelPercent * usesToID());
		}
	}

	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String ENCHANTMENT        = "enchantment";
	private static final String ENCHANT_HARDENED = "enchant_hardened";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String MASTERY_POTION_BONUS = "mastery_potion_bonus";
	private static final String AUGMENT            = "augment";

	// SAVE TAGS

	private static final String GLORY_KILLS_TAG = "glory_kills_v5";
	private static final String QUALITY_TAG  = "quality_v7";
	private static final String BLESSING_TAG = "blessing_v7";
	private static final String ELEMENT_TAG = "element_v7";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( ENCHANT_HARDENED, enchantHardened );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( MASTERY_POTION_BONUS, masteryPotionBonus );
		bundle.put( AUGMENT, augment );

		// 1. SAVE ENUMS AS STRINGS: This completely prevents the Java Reflection crash!
		bundle.put( QUALITY_TAG, quality != null ? quality.name() : "USED" );
		bundle.put( ELEMENT_TAG, element != null ? element.name() : "NONE" );

		// 2. SAVE BLESSING AS BUNDLABLE: The engine safely handles this natively!
		if (blessing != null) {
			bundle.put( BLESSING_TAG, blessing );
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getFloat( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getFloat( AVAILABLE_USES );
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		enchantHardened = bundle.getBoolean( ENCHANT_HARDENED );
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		masteryPotionBonus = bundle.getBoolean( MASTERY_POTION_BONUS );
		augment = bundle.getEnum(AUGMENT, Augment.class);

		// 1. LOAD ENUMS FROM STRINGS SAFELY
		try {
			quality = WeaponQuality.valueOf( bundle.getString(QUALITY_TAG) );
		} catch (Exception e) {
			quality = WeaponQuality.USED;
		}

		try {
			element = Element.valueOf( bundle.getString(ELEMENT_TAG) );
		} catch (Exception e) {
			element = Element.NONE;
		}

		// 2. LOAD BLESSING SAFELY
		try {
			if (bundle.contains(BLESSING_TAG)) {
				blessing = (com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Blessing) bundle.get(BLESSING_TAG);
			} else {
				blessing = null;
			}
		} catch (Exception e) {
			blessing = null;
		}
	}

	@Override
	public void reset() {
		super.reset();
		usesLeftToID = usesToID();
		availableUsesToID = usesToID()/2f;
	}

	@Override
	public boolean collect(Bag container) {
		if(super.collect(container)){
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && enchantment != null){
				Catalog.setSeen(enchantment.getClass());
				Statistics.itemTypesDiscovered.add(enchantment.getClass());
			}
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && blessing != null){
				Catalog.setSeen(blessing.getClass());
				Statistics.itemTypesDiscovered.add(blessing.getClass());
			}
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && element != null && element != Element.NONE){
				element.setSeen();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item identify(boolean byHero) {



		if (enchantment != null && Dungeon.hero != null && Dungeon.hero.isAlive()){
			Catalog.setSeen(enchantment.getClass());
			Statistics.itemTypesDiscovered.add(enchantment.getClass());
		}
		if (blessing != null && Dungeon.hero != null && Dungeon.hero.isAlive()){
			Catalog.setSeen(blessing.getClass());
			Statistics.itemTypesDiscovered.add(blessing.getClass());
		}
		if (element != null && element != Element.NONE && Dungeon.hero != null && Dungeon.hero.isAlive()){
			element.setSeen();
		}
		return super.identify(byHero);
	}

	public void setIDReady(){ usesLeftToID = -1; }
	public boolean readyToIdentify(){ return !isIdentified() && usesLeftToID <= 0; }

	@Override
	public float accuracyFactor(Char owner, Char target) {
		int encumbrance = 0;
		if( owner instanceof Hero ){
			encumbrance = STRReq() - ((Hero)owner).STR();
		}

		float ACC = this.ACC;
		if (owner.buff(Wayward.WaywardBuff.class) != null && enchantment instanceof Wayward){
			ACC /= 5;
		}
		return encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
	}

	@Override
	public float delayFactor( Char owner ) {
		return baseDelay(owner) * (1f/speedMultiplier(owner));
	}

	protected float baseDelay( Char owner ){
		float delay = augment.delayFactor(this.DLY);
		if (owner instanceof Hero) {
			int encumbrance = STRReq() - ((Hero)owner).STR();
			if (encumbrance > 0){
				delay *= Math.pow( 1.2, encumbrance );
			}
		}
		return delay;
	}

	protected float speedMultiplier(Char owner ){
		float multi = RingOfFuror.attackSpeedMultiplier(owner);
		if (owner.buff(Scimitar.SwordDance.class) != null){
			multi += 0.6f;
		}
		return multi;
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = RCH;
		if (element == Element.ZEPHYR) reach += 1;

		if (owner instanceof Hero && RingOfForce.fightingUnarmed((Hero) owner)){
			reach = 1;
			if (!RingOfForce.unarmedGetsWeaponEnchantment((Hero) owner)){
				return reach;
			}
		}
		if (owner instanceof Hero && owner.buff(AscendedForm.AscendBuff.class) != null){
			reach += 2;
		}
		if (hasEnchant(Projecting.class, owner)){
			return reach + Math.round(Enchantment.genericProcChanceMultiplier(owner));
		} else {
			return reach;
		}
	}

	public int STRReq(){ return STRReq(level()); }
	public abstract int STRReq(int lvl);
	protected static int STRReq(int tier, int lvl){
		lvl = Math.max(0, lvl);
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public int level() {
		int level = super.level();
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}

	@Override
	public Item upgrade() { return upgrade(false); }

	public Item upgrade(boolean enchant ) {
		if (enchant){
			if (enchantment == null){
				enchant(Enchantment.random());
			}
		} else if (enchantment != null) {
			if (enchantHardened){
				if (level() >= 6 && Random.Float(10) < Math.pow(2, level()-6)){
					enchantHardened = false;
				}
			} else if (hasCurseEnchant()) {
				if (Random.Int(3) == 0) enchant(null);
			} else if (level() >= 4 && Random.Float(10) < Math.pow(2, level()-4)){
				enchant(null);
			}
		}
		cursed = false;
		return super.upgrade();
	}

	// --- DYNAMIC NAME ---
	@Override
	public String name() {
		String wepName = super.name();

		// Strip existing prefixes
		wepName = wepName.replace("Broken ", "").replace("Rusty ", "").replace("Maintained ", "").replace("Flawless ", "").replace("Masterwork ", "");

		String qPrefix = "";
		if (quality == WeaponQuality.BROKEN) qPrefix = "Broken ";
		else if (quality == WeaponQuality.RUSTY) qPrefix = "Rusty ";
		else if (quality == WeaponQuality.MAINTAINED) qPrefix = "Maintained ";
		else if (quality == WeaponQuality.FLAWLESS) qPrefix = "Flawless ";
		else if (quality == WeaponQuality.MASTERWORK) qPrefix = "Masterwork ";
		wepName = qPrefix + wepName;

		// Elements (WITH INDIVIDUAL COLOR MARKUPS!)
		if (element != null && element != Element.NONE) {
			String elementStr = element.name().toLowerCase();
			elementStr = elementStr.substring(0, 1).toUpperCase() + elementStr.substring(1);

			String elemColor = "";
			switch (element) {
				case ABYSSAL:  elemColor = "[#553377]"; break; // Purple
				case LUMINOUS: elemColor = "[#FFFFFF]"; break;
				case SANGUINE: elemColor = "[#8B0000]"; break;
				case CHAOTIC:  elemColor = "[#FF00FF]"; break;
				case INFERNAL: elemColor = "[#FF4500]"; break;
				case GLACIAL:  elemColor = "[#00BFFF]"; break;
				case GILDED:   elemColor = "[#FFD700]"; break;
				case VOLTAIC:  elemColor = "[#00FFFF]"; break;
				case CAUSTIC:  elemColor = "[#32CD32]"; break;
				case ZEPHYR:   elemColor = "[#F0F8FF]"; break;
				case TERRAN:   elemColor = "[#8B4513]"; break;
			}

			// Inject the specific element color into the name! (Notice the spaces around the tags)
			wepName = elemColor + " " + elementStr + " [] " + wepName;
		}

		// Blessing (WITH GOLD TEXT MARKUP!)
		if (blessing != null) {
			wepName = "[#FFD700] " + blessing.name() + " [] " + wepName;
		}

		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
				&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
			return Messages.get(HolyWeapon.class, "ench_name", wepName);
		} else {
			return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name(wepName) : wepName;
		}
	}


	// --- DYNAMIC INFO TEXT ---
	@Override
	public String info() {
		String desc = super.info();

		desc = desc.replace("It is rusty.", "").replace("This weapon is broken.", "");

		// 1. EXPLICIT QUALITY GRADES
		String qualityDesc = "";
		if (quality == WeaponQuality.BROKEN) {
			qualityDesc = "\n\nQuality: [#888888]" + quality.title() + "[]\nThis weapon is severely broken and barely usable.";
		} else if (quality == WeaponQuality.RUSTY) {
			qualityDesc = "\n\nQuality: [#A52A2A]" + quality.title() + "[]\nThis weapon is rusty and dulled with age.";
		} else if (quality == WeaponQuality.USED) {
			qualityDesc = "\n\nQuality: [#FFFFFF]" + quality.title() + "[]\nThis weapon has seen some use, but is reliable.";
		} else if (quality == WeaponQuality.MAINTAINED) {
			qualityDesc = "\n\nQuality: [#00FF00]" + quality.title() + "[]\nThis weapon has been exceptionally well maintained.";
		} else if (quality == WeaponQuality.FLAWLESS) {
			qualityDesc = "\n\nQuality: [#00FFFF]" + quality.title() + "[]\nThis weapon is flawless and shines brilliantly.";
		} else if (quality == WeaponQuality.MASTERWORK) {
			qualityDesc = "\n\nQuality: [#FFD700]" + quality.title() + "[]\nThis weapon is a true masterwork, probably made by a good Smith =).";
		}

		// 2. EXPLICIT ELEMENT DESCRIPTIONS
		String elementDesc = "";
		if (element != null && element != Element.NONE) {
            if (element.isSeen()) {
                elementDesc = "\n\nElement: " + element.displayName() + "\n";
                elementDesc += element.desc();
            } else {
                elementDesc = "\n\nElement: ???\n";
                elementDesc += "A mysterious element.\n\nIdentify a weapon imbued with this element to learn more about it.";
            }
        }

        // 3. EXPLICIT BLESSING DESCRIPTIONS
        String blessingDesc = "";
        if (blessing != null) {
            if (Catalog.isSeen(blessing.getClass())) {
                blessingDesc = "\n\nBlessing: [#FFD700]" + blessing.name() + "[]\n";
                blessingDesc += blessing.desc();
            } else {
                blessingDesc = "\n\nBlessing: ???\n";
                blessingDesc += "A mysterious blessing.\n\nIdentify a weapon imbued with this blessing to learn more about it.";
            }
        }

        return desc + qualityDesc + elementDesc + blessingDesc;
	}

	@Override
	public Item random() {
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) { n++; }
		}
		level(n);

		int qualityRoll = Random.Int(1, 100);
		if (qualityRoll <= 40) quality = WeaponQuality.BROKEN;
		else if (qualityRoll <= 70) quality = WeaponQuality.RUSTY;
		else if (qualityRoll <= 85) quality = WeaponQuality.USED;
		else if (qualityRoll <= 95) quality = WeaponQuality.MAINTAINED;
		else if (qualityRoll <= 99) quality = WeaponQuality.FLAWLESS;
		else quality = WeaponQuality.MASTERWORK;

		if (Random.Float() < 0.15f) {
			applyBlessing( com.shatteredpixel.shatteredpixeldungeon.items.weapon.blessings.Blessing.random() );
		}

		Random.pushGenerator(Random.Long());
		float effectRoll = Random.Float();
		if (effectRoll < 0.3f * ParchmentScrap.curseChanceMultiplier()) {
			enchant(Enchantment.randomCurse());
			cursed = true;
		} else if (effectRoll >= 1f - (0.1f * ParchmentScrap.enchantChanceMultiplier())){
			enchant();
		}
		Random.popGenerator();

		return this;
	}

	public Weapon enchant( Enchantment ench ) {
		if (ench == null || !ench.curse()) curseInfusionBonus = false;
		enchantment = ench;
		updateQuickslot();
		if (ench != null && isIdentified() && Dungeon.hero != null
				&& Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(this)){
			Catalog.setSeen(ench.getClass());
			Statistics.itemTypesDiscovered.add(ench.getClass());
		}
		return this;
	}

	public Weapon enchant() {
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random( oldEnchantment );
		return enchant( ench );
	}

	public boolean hasEnchant(Class<?extends Enchantment> type, Char owner) {
		if (owner.buff(MagicImmune.class) != null) {
			return false;
		} else if (enchantment != null
				&& !enchantment.curse()
				&& owner instanceof Hero
				&& isEquipped((Hero) owner)
				&& owner.buff(HolyWeapon.HolyWepBuff.class) != null
				&& ((Hero) owner).subClass != HeroSubClass.PALADIN) {
			return false;
		} else if (owner.buff(BodyForm.BodyFormBuff.class) != null
				&& owner.buff(BodyForm.BodyFormBuff.class).enchant() != null
				&& owner.buff(BodyForm.BodyFormBuff.class).enchant().getClass().equals(type)){
			return true;
		} else if (enchantment != null) {
			return enchantment.getClass() == type;
		} else {
			return false;
		}
	}

	public boolean hasGoodEnchant(){ return enchantment != null && !enchantment.curse(); }
	public boolean hasCurseEnchant(){ return enchantment != null && enchantment.curse(); }

	public float getQualityEnchantmentMultiplier() {
		switch (quality) {
			case BROKEN: return 0.1f;
			case RUSTY: return 0.5f;
			case USED: return 1.0f;
			case MAINTAINED: return 1.5f;
			case FLAWLESS: return 2.0f;
			case MASTERWORK: return 10.0f;
			default: return 1.0f;
		}
	}

	private static ItemSprite.Glowing HOLY = new ItemSprite.Glowing( 0xFFFF00 );
	private static ItemSprite.Glowing BLESSED_GLOW = new ItemSprite.Glowing( 0xFFD700 ); // Radiant Gold

	@Override
	public ItemSprite.Glowing glowing() {
		boolean hasElement = (element != null && element != Element.NONE);
		boolean hasBlessing = (blessing != null);

		// 1. THE PERFECT BLEND: Mix the Element and Blessing permanently
		if (hasElement && hasBlessing) {
			ItemSprite.Glowing elemGlow = getElementalGlowing();

			// Extract RGB of the Element
			int elemColor = elemGlow.color;
			int elemR = (elemColor >> 16) & 0xFF;
			int elemG = (elemColor >> 8) & 0xFF;
			int elemB = elemColor & 0xFF;

			// Extract RGB of the Radiant Gold Blessing
			int blessColor = BLESSED_GLOW.color;
			int blessR = (blessColor >> 16) & 0xFF;
			int blessG = (blessColor >> 8) & 0xFF;
			int blessB = blessColor & 0xFF;

			// Mix them exactly 50/50 to create a stable, hybrid color!
			int currentR = (elemR + blessR) / 2;
			int currentG = (elemG + blessG) / 2;
			int currentB = (elemB + blessB) / 2;

			int blendedColor = (currentR << 16) | (currentG << 8) | currentB;

			// The game engine will automatically pulse the brightness of this mixed color
			return new ItemSprite.Glowing(blendedColor);
		}

		// 2. If it ONLY has an element, glow the element color
		if (hasElement) {
			return getElementalGlowing();
		}

		// 3. If it ONLY has a blessing, glow gold
		if (hasBlessing) {
			return BLESSED_GLOW;
		}

		// 4. Vanilla Fallback
		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
				&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
			return HOLY;
		} else {
			return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.glowing() : null;
		}
	}
	private ItemSprite.Glowing getElementalGlowing() {
		switch (element) {
			case ABYSSAL: return new ItemSprite.Glowing(0x000000);
			case LUMINOUS: return new ItemSprite.Glowing(0xFFFFFF);
			case SANGUINE: return new ItemSprite.Glowing(0x8B0000);
			case CHAOTIC: return new ItemSprite.Glowing(0xFF00FF);
			case INFERNAL: return new ItemSprite.Glowing(0xFF4500);
			case GLACIAL: return new ItemSprite.Glowing(0x00BFFF);
			case GILDED: return new ItemSprite.Glowing(0xFFD700);
			case VOLTAIC: return new ItemSprite.Glowing(0x00FFFF);
			case CAUSTIC: return new ItemSprite.Glowing(0x32CD32);
			case ZEPHYR: return new ItemSprite.Glowing(0xF0F8FF);
			case TERRAN: return new ItemSprite.Glowing(0x8B4513);
			default: return null;
		}
	}

	public static abstract class Enchantment implements Bundlable {

		public static final Class<?>[] common = new Class<?>[]{ Blazing.class, Chilling.class, Kinetic.class, Shocking.class};
		public static final Class<?>[] uncommon = new Class<?>[]{ Blocking.class, Blooming.class, Elastic.class, Lucky.class, Projecting.class, Unstable.class};
		public static final Class<?>[] rare = new Class<?>[]{ Corrupting.class, Grim.class, Vampiric.class};
		public static final float[] typeChances = new float[]{ 50, 40, 10 };
		public static final Class<?>[] curses = new Class<?>[]{ Annoying.class, Displacing.class, Dazzling.class, Explosive.class, Sacrificial.class, Wayward.class, Polarized.class, Friendly.class };

		public abstract int proc( Weapon weapon, Char attacker, Char defender, int damage );

		protected float procChanceMultiplier( Char attacker ){ return genericProcChanceMultiplier( attacker ); }

		public static float genericProcChanceMultiplier( Char attacker ){
			float multi = RingOfArcana.enchantPowerMultiplier(attacker);
			Berserk rage = attacker.buff(Berserk.class);
			if (rage != null) multi = rage.enchantFactor(multi);
			if (attacker.buff(RunicBlade.RunicSlashTracker.class) != null){
				multi += attacker.buff(RunicBlade.RunicSlashTracker.class).boost;
				attacker.buff(RunicBlade.RunicSlashTracker.class).detach();
			}
			if (attacker.buff(Smite.SmiteTracker.class) != null) multi += 3f;
			if (attacker.buff(ElementalStrike.DirectedPowerTracker.class) != null){
				multi += attacker.buff(ElementalStrike.DirectedPowerTracker.class).enchBoost;
				attacker.buff(ElementalStrike.DirectedPowerTracker.class).detach();
			}
			if (attacker.buff(Talent.SpiritBladesTracker.class) != null && ((Hero)attacker).pointsInTalent(Talent.SPIRIT_BLADES) == 4) multi += 0.1f;
			if (attacker.buff(Talent.StrikingWaveTracker.class) != null && ((Hero)attacker).pointsInTalent(Talent.STRIKING_WAVE) == 4) multi += 0.2f;
			return multi;
		}

		public String name() {
			if (!curse()) return name( Messages.get(this, "enchant"));
			else return name( Messages.get(Item.class, "curse"));
		}
		public String name( String weaponName ) { return Messages.get(this, "name", weaponName); }
		public String desc() { return Messages.get(this, "desc"); }
		public boolean curse() { return false; }
		@Override public void restoreFromBundle( Bundle bundle ) { }
		@Override public void storeInBundle( Bundle bundle ) { }
		public abstract ItemSprite.Glowing glowing();

		@SuppressWarnings("unchecked")
		public static Enchantment random( Class<? extends Enchantment> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default: return randomCommon( toIgnore );
				case 1: return randomUncommon( toIgnore );
				case 2: return randomRare( toIgnore );
			}
		}
		@SuppressWarnings("unchecked")
		public static Enchantment randomCommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(common));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) return random();
			else return (Enchantment) Reflection.newInstance(Random.element(enchants));
		}
		@SuppressWarnings("unchecked")
		public static Enchantment randomUncommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(uncommon));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) return random();
			else return (Enchantment) Reflection.newInstance(Random.element(enchants));
		}
		@SuppressWarnings("unchecked")
		public static Enchantment randomRare( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(rare));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) return random();
			else return (Enchantment) Reflection.newInstance(Random.element(enchants));
		}
		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse( Class<? extends Enchantment> ... toIgnore ){
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(curses));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) return random();
			else return (Enchantment) Reflection.newInstance(Random.element(enchants));
		}
	}

}