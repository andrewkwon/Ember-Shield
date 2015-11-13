package game;
import unit.Unit;
import item.Item;

public class Game {

	public static void main(String[] args) {
		Unit fred = new Unit("Fred");
		System.out.println(fred.giveItem(new Item("Fredinator")));
		System.out.println(fred.useItem(0));
		System.out.println("Made it this far!");
	}
}
