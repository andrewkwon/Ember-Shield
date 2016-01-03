package unit;
import java.util.Map;
import item.Item;
import board.Board;

public class UnitClass {
	
	private String name;
	private String[] equippableItemTypes;
	//stat caps to be added?
	private Map<String, Integer> actions;
	protected final int numberOfActions = 4;
	
	public UnitClass(String name) {
		this.name = name;
	}
	
	//possible solution to having different sets of actions for each class
	public boolean act(String actionName, Unit unitA, Unit unitB, Board board) {
		int actionId = -1;
		if(actions.get(actionName) != null) {
			actionId = actions.get(actionName);
			if(actionId == 0) actZero(unitA, unitB, board);
			else if(actionId == 1) actOne(unitA, unitB, board);
			else if(actionId == 2) actTwo(unitA, unitB, board);
			else if(actionId == 3) actThree(unitA, unitB, board);
			unitA.setActive(false);
			return true;
		}
		return false;
	}
	
	public boolean act(int actionId, Unit unitA, Unit unitB, Board board) {
		if(actions.containsValue(actionId)) {
			//if the action can't be carried out, the unit is not set to inactive
			boolean succeededInExecuting = false;
			if(actionId == 0) succeededInExecuting = actZero(unitA, unitB, board);
			else if(actionId == 1) succeededInExecuting = actOne(unitA, unitB, board);
			else if(actionId == 2) succeededInExecuting = actTwo(unitA, unitB, board);
			else if(actionId == 3) succeededInExecuting = actThree(unitA, unitB, board);
			unitA.setActive(!succeededInExecuting);
			return true;
		}
		return false;
	}
	
	//each action method to be filled in specific classes
	//e.g. dancer class actOne could be changed to a dance method
	public boolean actZero(Unit unitA, Unit unitB, Board board) {
		return false;
	}
	
	public boolean actOne(Unit unitA, Unit unitB, Board board) {
		return false;
	}
	
	public boolean actTwo(Unit unitA, Unit unitB, Board board) {
		return false;
	}
	
	public boolean actThree(Unit unitA, Unit unitB, Board board) {
		return false;
	}
	
	public boolean wait(Unit unitA) {
		unitA.setActive(false);
		return true;
	}
	
	public String[] getEquippableItemTypes() {
		return equippableItemTypes;
	}
	
	public void setEquippableItemTypes(String[] equippableItemTypes) {
		this.equippableItemTypes = equippableItemTypes;
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
	
	public void setActions(Map<String, Integer> actions) {
		this.actions = actions;
	}
}
