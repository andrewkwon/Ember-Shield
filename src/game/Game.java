package game;
import unit.Unit;
import graphics.Screen;
import graphics.SpriteSheet;
import item.Item;
import unit.UnitClass;
import item.Weapon;
import board.Board;
import unit.UnitAttacker;

import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
	
	private boolean running;
	private JFrame frame;
	private int width = 32 * 36;
	private int height = 32 * 15;
	private final String name = "Ember Shield";
	private int clock;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH];
	private Screen screen;
	private final int scale = 3;
	private Board b;
	private int[][] tileMap = {
			{00, 01, 02, 19, 19, 19, 19, 19, 03, 19, 19, 19}, 
			{12, 13, 13, 31, 19, 19, 31, 19, 03, 19, 19, 16}, 
			{13, 13, 13, 13, 13, 34, 13, 14, 28, 19, 19, 19}, 
			{07, 07, 07, 8, 13, 13, 13, 18, 15, 17, 19, 19}, 
			{19, 19, 19, 19, 07, 07, 07, 19, 19, 00, 01, 01}};
	private Unit fred;
	private Unit bob;
	private String objective = "DefeatAll";
	
	public Game() {
		super();
		frame = new JFrame(name);
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public synchronized void init() {
		running = true;
		clock = 0;
		
		for(int r = 0; r < SpriteSheet.COLOR_DEPTH; r++) {
			for(int g = 0; g < SpriteSheet.COLOR_DEPTH; g++) {
				for(int b = 0; b < SpriteSheet.COLOR_DEPTH; b++) {
					int index = r * (SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH) + g * (SpriteSheet.COLOR_DEPTH) + b;
					int col = ((r * 0xFF / SpriteSheet.COLOR_DEPTH) << 16) + ((g * 0xFF / SpriteSheet.COLOR_DEPTH) << 8) + (b * 0xFF / SpriteSheet.COLOR_DEPTH);
					System.out.printf("r: %d g: %d b: %d \n", r, g, b);
					System.out.printf("index: %d, col: %d \n", index, col);
					colors[index] = col;
				}
			}
		}
		screen = new Screen(width, height);
		screen.setSheet(new SpriteSheet("/Untitled.png"));
		
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
		int tickCount = 0;
		int frameCount = 0;
		long lastCheck = System.currentTimeMillis();
		long lastUpdate = System.nanoTime();
		double nanoSecondsBetweenTicks = 1000000000D/60D;
		long ticksPassed = 0;
		while(running) {
			//renders and ticks
			long now = System.nanoTime();
			ticksPassed += (now - lastUpdate) / nanoSecondsBetweenTicks;
			
			if(ticksPassed >= 1) {
				update();
				ticksPassed --;
				tickCount++;
				lastUpdate  = now;
			}
			
			render();
			frameCount++;
			try{
				Thread.sleep(2);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
				
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.printf("tps: %d, fps: %d\n", tickCount, frameCount);
				tickCount = 0;
				frameCount = 0;
			}
			
			/*display(b);
			takeCommand(b);
			if(hasWon(objective, b)) {
				System.out.println("YOU WIN");
				stop();
			}
			else if(hasLost(b)) {
				System.out.println("GAME OVER");
				stop();
			}*/
		}
	}
	
	public void update() {
		clock++;
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		for(int x = 0; x < 12; x++) {
			for(int y = 0; y < 5; y++) {
				screen.render(x, y, tileMap[y][x], scale);
			}
		}
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = colors[screen.getPixels()[i]];
		}
		
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		bs.show();
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
