package item;
import java.util.HashMap;
import java.util.Map;

public class Weapon extends Item {

	//weapon-specific attributes in Awakening are:
	//Mt, Hit, Crt, Rng
	private Map<String, Integer> stats;
	private boolean doesPhysicalDamage;
	
	public Weapon(String name) {
		super(name);
		setEquippable(true);
		String[] keys = {"Mt", "Hit", "Crt", "Rng"};
		stats = new HashMap(keys.length);
		for(String k : keys) stats.put(k, 0);
	}
	
	public boolean doesPhysicalDamage() {
		return doesPhysicalDamage;
	}
	
	public Integer getStat(String key) {
		return stats.get(key);
	}
	
	public void setStat(String key, Integer value) {
		stats.replace(key, value);
	}
	
	public void setDoesPhysicalDamage(boolean doesPhysicalDamage) {
		this.doesPhysicalDamage = doesPhysicalDamage;
	}
}
