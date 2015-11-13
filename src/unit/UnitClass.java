package unit;
import java.util.Dictionary;
import item.Item;

public class UnitClass {
	
	private String name;
	private String[] equippableItemTypes;
	//stat caps to be added?
	private Dictionary<String, Integer> actions;
	
	public UnitClass() {
		
	}
	
	//possible solution to having different sets of actions for each class
	public boolean act(String actionName, Unit unitA, Unit unitB) {
		int actionId = -1;
		if(actions.get(actionName) != null) {
			actionId = actions.get(actionName);
			if(actionId == 0) actZero(unitA, unitB);
			else if(actionId == 1) actOne(unitA, unitB);
			else if(actionId == 2) actTwo(unitA, unitB);
			else if(actionId == 3) actThree(unitA, unitB);
			return true;
		}
		return false;
	}
	
	//each action method to be filled in specific classes
	//e.g. dancer class actOne could be changed to a dance method
	public boolean actZero(Unit unitA, Unit unitB) {
		return false;
	}
	
	public boolean actOne(Unit unitA, Unit unitB) {
		return false;
	}
	
	public boolean actTwo(Unit unitA, Unit unitB) {
		return false;
	}
	
	public boolean actThree(Unit unitA, Unit unitB) {
		return false;
	}
	
	public boolean wait(Unit unitA) {
		unitA.setActive(false);
		return true;
	}
	
	public String[] getEquippableItemTypes() {
		return equippableItemTypes;
	}
	
	public boolean isEquippable(Item item) {
		boolean equippable = false;
		if(item.getEquippable()) {
			for(String itemType : equippableItemTypes) {
				if (item.getItemType() == itemType) {
					equippable = true;
					break;
				}
			}
		}
		return equippable;
	}
}
