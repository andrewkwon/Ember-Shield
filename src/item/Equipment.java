package item;

import java.util.HashMap;

//staffs and weapons
public class Equipment extends Item {

	//range
	private int[] Rng;
	
	public Equipment(String name) {
		super(name);
		setEquippable(true);
	}
	
	public int[] getRng() {
		return Rng;
	}
	
	public void setRng(int[] Rng) {
		this.Rng = Rng; 
	}
}
