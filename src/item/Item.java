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
		if (usable && (remainingUses == -1 || remainingUses > 0)) {
			if(remainingUses != -1) remainingUses --;
			effect();
			return true;
		}
		return false;
	}
	
	//effect when used
	public boolean effect() {
		return false;
	}
	
	public boolean getEquippable() {
		return equippable;
	}
	
	public String getItemType() {
		return itemType;
	}
	
	//effect when equipped
	public boolean equipEffect() {
		return equippable;
	}
}
