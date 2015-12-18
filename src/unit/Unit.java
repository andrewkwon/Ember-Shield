package unit;
import java.util.Map;

import item.Item;
import board.Board;

import java.util.HashMap;

import graphics.Screen;
import graphics.Sprite;

public class Unit {
	
	private String name;
	private int level = 0;
	private Map<String, Integer> stats;
	//fire emblem awakening stats are: HP Str Mag Skl Spd Lck Def Res Move
	private Map<String, Integer> growthRates;
	//growth of stats per level up
	private UnitClass unitClass;
	//max stat modifiers to be added?
	private final int inventorySpace = 4;
	private Item[] inventory = new Item[inventorySpace];
	private int equipped = -1;
	private boolean canMove = true;
	private boolean active = true;
	private String faction;
	private String side;
	private Sprite sprite;
	
	public Unit(String name) {
		this.name = name;
		String[] keys = {"HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res", "Move"};
		stats = new HashMap(keys.length);
		growthRates = new HashMap(keys.length);
		for(String k : keys) stats.put(k, 0);
		growthRates.putAll(stats);
		//TODO: remove and generalize
		sprite = new Sprite(null, 32, 32);
	}
	
	public boolean equip(int itemId) {
		if(itemId < 0 || itemId >= inventorySpace) return false;
		if(unitClass.getEquippableItemTypes() == null) return false;
		if(inventory[itemId].getEquippable() && unitClass.isEquippable(inventory[itemId])) {
			equipped = itemId;
			inventory[itemId].equipEffect();
			return true;
		}
		return false;
	}
	
	public boolean useItem(int itemId) {
		if(itemId < 0 || itemId >= inventorySpace) return false;
		if(!inventory[itemId].use(this)) removeItem(itemId);
		return true;
	}
	
	public void setStats(Map stats) {
		this.stats = stats;
	}
	
	public void setGrowthRates(Map growthRates) {
		this.growthRates = growthRates;
	}
	
	public void setUnitClass(UnitClass unitClass) {
		this.unitClass = unitClass;
	}
	
	public boolean giveItem(Item item) {
		for(int i = 0; i < inventorySpace; i++) {
			if(inventory[i] == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void removeItem(int itemId) {
		if(itemId == equipped) {
			equipped = -1;
			System.out.printf("%s broke!\n", inventory[itemId].getName());
		}
		inventory[itemId] = null;
	}
	
	public void takeDamage(boolean physicalDamage, int damage, Board board) {
		int defense = 0;
		if(physicalDamage) defense = stats.get("Def");
		else defense = stats.get("Res");
		int damageSustained = damage - defense;
		if(damageSustained < 0) damageSustained = 0;
		int newHP = stats.get("HP") - damageSustained;
		if(newHP < 0) newHP = 0;
		stats.replace("HP", newHP);
		if(stats.get("HP") == 0) die(board);
	}
	
	//if has died, remove from board;
	public void die(Board board) {
		System.out.printf("%s died! \n", name);
		board.removeUnit(this);
	}
	
	public void update(int clock) {
		//updates sprite animation or other things which need continuous updating
		sprite.update(clock);
	}
	
	public void render(Screen screen, int scale) {
		sprite.render(screen, scale);
	}
	
	public UnitClass getUnitClass() {
		return unitClass;
	}
	
	public int getEquipped() {
		return equipped;
	}
	
	public Item getItem(int itemId) {
		return inventory[itemId];
	}
	
	public Integer getStat(String key) {
		return stats.get(key);
	}
	
	public void setStat(String key, Integer value) {
		stats.replace(key, value);
	}
	
	public String getName() {
		return name;
	}
	
	public String getFaction() {
		return faction;
	}
	
	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	public String getSide() {
		return side;
	}
	
	public void setSide(String side) {
		this.side = side;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
}
