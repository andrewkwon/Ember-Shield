package unit;
import java.util.Map;
import java.util.HashMap;

import board.Board;
import item.Weapon;

public class UnitAttacker extends UnitClass {
	
	public UnitAttacker(String name) {
		super(name);
		Map<String, Integer> a = new HashMap<String, Integer>(numberOfActions);
		a.put("Attack", 0);
		setActions(a);
	}
	
	//overwritten to be an attack action
	//will assume that target is in range and weapon is equipped for now
	//crits to be added
	public boolean actZero(Unit unitA, Unit unitB, Board board) {
		if(unitA.getItem(unitA.getEquipped()) instanceof Weapon) {
			int damage = ((Weapon) unitA.getItem(unitA.getEquipped())).getStat("Mt");
			boolean physicalDamage = ((Weapon) unitA.getItem(unitA.getEquipped())).doesPhysicalDamage();
			if(physicalDamage) damage += unitA.getStat("Str");
			else damage += unitA.getStat("Mag");
			System.out.printf("%s attacks %s! \n", unitA.getName(), unitB.getName());
			unitB.takeDamage(physicalDamage, damage, board);
			if(!unitA.getItem(unitA.getEquipped()).wear()) unitA.removeItem(unitA.getEquipped());
			return true;
		}
		return false;
	}
}
