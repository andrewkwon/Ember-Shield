package item;
import unit.Unit;

public class Item {

	private String name;
	//itemType determines whether or not it can be equipped by a specific class
	private String itemType;
	private int remainingUses;
	private boolean usable;
	private boolean equippable;
	
	public Item(String name) {
		this.name = name;
	}
	
	public boolean use(Unit unitA) {
		if(usable) {
			effect(unitA);
			return wear();
		}
		return true;
	}
	
	//lowers remaining item uses
	public boolean wear() {
		if(remainingUses != -1) remainingUses --;
		//should return whether or not the item still has uses
		if (remainingUses == -1 || remainingUses > 0) return true;
		else return false;
	}
	
	//effect when used
	public boolean effect(Unit unitA) {
		return false;
	}
	
	public boolean getEquippable() {
		return equippable;
	}
	
	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}
	
	public String getItemType() {
		return itemType;
	}
	
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	//effect when equipped
	public boolean equipEffect() {
		return equippable;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRemainingUses(int remainingUses) {
		this.remainingUses = remainingUses;
	}
	
	public int getRemainingUses() {
		return remainingUses;
	}
	
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
}
