package unit;
import java.util.Dictionary;
import item.Item;

public class Unit {
	
	private String name;
	private int level = 0;
	private Dictionary<String, Integer> stats;
	//fire emblem awakening stats are: HP Str Mag Skl Spd Lck Def Res Move
	private Dictionary<String, Integer> growthRates;
	//growth of stats per level up
	private UnitClass unitClass;
	//max stat modifiers to be added?
	private final int inventorySpace = 4;
	private Item[] inventory = new Item[inventorySpace];
	private int equipped = -1;
	private boolean canMove = true;
	private boolean active = true;
	
	public Unit(String name) {
		this.name = name;
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
		if(inventory[itemId].use() == false) removeItem(itemId);
		return true;
	}
	
	public void setStats(Dictionary stats) {
		this.stats = stats;
	}
	
	public void setGrowthRates(Dictionary growthRates) {
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
			System.out.printf("%s has broken!\n", inventory[itemId].getName());
		}
		inventory[itemId] = null;
	}
	
	public UnitClass getUnitClass() {
		return unitClass;
	}
}
