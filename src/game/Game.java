package game;
import unit.Unit;
import item.Item;
import unit.UnitClass;
import item.Weapon;
import board.Board;
import unit.UnitAttacker;

public class Game {

	public static void main(String[] args) {
		Unit fred = new Unit("Fred");
		Unit bob = new Unit("Bob");
		Board b = new Board(1, 2);
		b.setUnit(0, 0, fred);
		b.setUnit(0, 1, bob);
		String[] types = {"freddish"};
		
		fred.setUnitClass(new UnitAttacker("freddy"));
		fred.getUnitClass().setEquippableItemTypes(types);
		fred.setStat("HP", 2);
		fred.setStat("Str", 1);
		bob.setUnitClass(new UnitClass("freddy"));
		bob.setStat("HP", 2);
		bob.setStat("Str", 1);
		
		Weapon fredinator = new Weapon("Fredinator");
		fredinator.setUsable(true);
		fredinator.setItemType(types[0]);
		fredinator.setRemainingUses(1);
		fredinator.setDoesPhysicalDamage(true);
		fredinator.setStat("Mt", 1);
		
		String[][] units = new String[3][4];
		for(int row = 0; row < 1; row++) {
			for(int column = 0; column < 2; column++) {
				if(b.getUnits()[row][column] != null) units[row][column] = b.getUnits()[row][column].getName();
				else units[row][column] = "null";
			}
		}
		System.out.println("units[0][0]: " + units[0][0] + " units[0][1]: " + units[0][1]);
		System.out.println("fred.giveItem(fredinator): " + fred.giveItem(fredinator));
		System.out.println("fred.equip(0): " + fred.equip(0));
		System.out.println("fred.getUnitClass() instanceof UnitAttacker: " + (fred.getUnitClass() instanceof UnitAttacker));
		System.out.println("fred.getUnitClass().actZero(fred, bob, b): " + fred.getUnitClass().actZero(fred, bob, b));
		for(int row = 0; row < 1; row++) {
			for(int column = 0; column < 2; column++) {
				if(b.getUnits()[row][column] != null) units[row][column] = b.getUnits()[row][column].getName();
				else units[row][column] = "null";
			}
		}
		System.out.println("units[0][0]: " + units[0][0] + " units[0][1]: " + units[0][1]);
		System.out.println("Made it this far!");
	}
}
