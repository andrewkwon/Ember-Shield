package game;
import unit.Unit;
import item.Item;
import unit.UnitClass;

public class Game {

	public static void main(String[] args) {
		Unit fred = new Unit("Fred");
		fred.setUnitClass(new UnitClass());
		Item fredinator = new Item("Fredinator");
		fredinator.setEquippable(true);
		String[] types = {"freddish"};
		fred.getUnitClass().setEquippableItemTypes(types);
		fredinator.setItemType(types[0]);
		System.out.println("fred.giveItem(fredinator): " + fred.giveItem(fredinator));
		System.out.println("fred.equip(0): " + fred.equip(0));
		System.out.println("fred.useItem(0): " + fred.useItem(0));
		System.out.println("Made it this far!");
	}
}
