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
	//will assume that weapon is equipped for now
	//crits to be added
	public boolean actZero(Unit unitA, Unit unitB, Board board) {
		if(unitA.getItem(unitA.getEquipped()) instanceof Weapon) {
			//checks to see if target is in range
			int rowDif = board.getUnitCoordinates(unitA)[0] - board.getUnitCoordinates(unitB)[0];
			int colDif = board.getUnitCoordinates(unitA)[1] - board.getUnitCoordinates(unitB)[1];
			if(rowDif < 0) rowDif *= -1;
			if(colDif < 0) colDif *= -1;
			boolean inRange = false;
			for(int rng : ((Weapon) unitA.getItem(unitA.getEquipped())).getRng()) {
				if(rng == rowDif + colDif) inRange = true;
			}
			if(!inRange) return false;
			
			//deals damage
			int damage = ((Weapon) unitA.getItem(unitA.getEquipped())).getStat("Mt");
			boolean physicalDamage = ((Weapon) unitA.getItem(unitA.getEquipped())).doesPhysicalDamage();
			if(physicalDamage) damage += unitA.getStat("Str");
			else damage += unitA.getStat("Mag");
			System.out.printf("%s attacks %s! \n", unitA.getName(), unitB.getName());
			unitB.takeDamage(physicalDamage, damage, board);
			if(!unitA.getItem(unitA.getEquipped()).wear()) {
				unitA.removeItem(unitA.getEquipped());
				unitA.setEquipped(-1);
			}
			return true;
		}
		return false;
	}
}
