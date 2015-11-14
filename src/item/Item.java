package item;

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
	
	public boolean use() {
		if(usable) {
			if(remainingUses != -1) remainingUses --;
			effect();
		}
		//should return whether or not the item has run out of uses
		if (remainingUses == -1 || remainingUses > 0) return true;
		else return false;
	}
	
	//effect when used
	public boolean effect() {
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
}
