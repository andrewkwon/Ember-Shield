package game;
import unit.Unit;
import item.Item;
import unit.UnitClass;
import item.Weapon;
import board.Board;
import unit.UnitAttacker;
import java.util.Scanner;

public class Game implements Runnable {
	
	private boolean running;
	private Board b;
	private Unit fred;
	private Unit bob;
	private String objective = "DefeatAll";
	
	public Game() {
		
	}
	
	public synchronized void init() {
		running = true;
		fred = new Unit("Fred");
		bob = new Unit("Bob");
		b = new Board(1, 2);
		b.setUnit(0, 0, fred);
		b.setUnit(0, 1, bob);
		String[] types = {"freddish"};
		
		fred.setUnitClass(new UnitAttacker("freddy"));
		fred.getUnitClass().setEquippableItemTypes(types);
		fred.setStat("HP", 2);
		fred.setStat("Str", 1);
		fred.setSide("Player");
		bob.setUnitClass(new UnitClass("freddy"));
		bob.setStat("HP", 2);
		bob.setStat("Str", 1);
		bob.setSide("Enemy");
		
		Weapon fredinator = new Weapon("Fredinator");
		fredinator.setUsable(true);
		fredinator.setItemType(types[0]);
		fredinator.setRemainingUses(1);
		fredinator.setDoesPhysicalDamage(true);
		fredinator.setStat("Mt", 1);
		System.out.println("fred.giveItem(fredinator): " + fred.giveItem(fredinator));
		System.out.println("fred.equip(0): " + fred.equip(0));
		System.out.println("fred.getUnitClass() instanceof UnitAttacker: " + (fred.getUnitClass() instanceof UnitAttacker));
	}
	
	public void run() {
		init();
		while(running) {
			display(b);
			takeCommand(b);
			if(hasWon(objective, b)) {
				System.out.println("YOU WIN");
				stop();
			}
			else if(hasLost(b)) {
				System.out.println("GAME OVER");
				stop();
			}
			try {
				Thread.sleep(20);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void display(Board board) {
		String toPrint = "";
		for(int row = 0; row < board.getBoardHeight(); row++) {
			for(int column = 0; column < board.getBoardWidth(); column++) {
				Unit unit = board.getUnits()[row][column];
				if(unit == null) {
					toPrint += " 0";
				}
				else toPrint += (" " + unit.getName().substring(0, 1).toUpperCase());
			}
			System.out.println(toPrint);
			toPrint = "";
		}
	}
	
	public void takeCommand(Board board) {
		Scanner sc = new Scanner(System.in);
		boolean stillTakingCommands = true;
		String[] coms = {"help", "sel \\d+ \\d+", "actzero \\d+ \\d+"};
		while(stillTakingCommands) {
			String command = sc.nextLine();
			if(command.equals("help")) {
				for(String com : coms) System.out.println(com);
			}
			if(command.matches("sel \\d+ \\d+")) {
				int row = Integer.parseInt(command.split(" ")[1]);
				int column = Integer.parseInt(command.split(" ")[2]);
				Unit selectedA = board.getUnits()[row][column];
				System.out.println("Selected " + selectedA.getName());
				command = sc.nextLine();
				if(command.matches("actzero \\d+ \\d+")) {
					row = Integer.parseInt(command.split(" ")[1]);
					column = Integer.parseInt(command.split(" ")[2]);
					Unit selectedB = board.getUnits()[row][column];
					selectedA.getUnitClass().actZero(selectedA, selectedB, board);
					stillTakingCommands = false;
				}
			}
		}
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public boolean hasWon(String objective, Board board) {
		if(objective == "DefeatAll") {
			for(Unit[] column : board.getUnits()) {
				for(Unit unit : column) {
					if(unit != null && unit.getSide().equals("Enemy")) {
						return false;
					}
				}
			}
			return true;
		}
		return true;
	}
	
	public boolean hasLost(Board board) {
		for(Unit[] column : board.getUnits()) {
			for(Unit unit : column) {
				if(unit != null && unit.getSide().equals("Player")) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}
