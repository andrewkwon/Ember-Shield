package game;
import unit.Unit;
import graphics.Screen;
import graphics.SpriteSheet;
import item.Item;
import unit.UnitClass;
import item.Weapon;
import board.Board;
import board.Land;
import unit.UnitAttacker;

import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controls.Cursor;
import controls.KeyboardControls;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	private KeyboardControls kc;
	private Board b;
	private Cursor cursor;
	private int[][] tileMap = {
			{00, 01, 02, 19, 19, 19, 19, 19, 03, 19, 19, 19, 30}, 
			{12, 13, 13, 31, 19, 19, 31, 19, 03, 19, 19, 16, 21}, 
			{13, 13, 13, 13, 13, 34, 13, 14, 28, 19, 19, 19, 14}, 
			{07, 07, 07, 8, 13, 13, 13, 18, 15, 17, 19, 19, 32}, 
			{19, 19, 19, 19, 07, 07, 07, 19, 19, 00, 01, 01, 02},
			{01, 03, 05, 9, 00, 13, 15, 14, 16, 17, 18, 25, 24}};
	private Unit fred;
	private Unit bob;
	private String objective = "DefeatAll";
	private boolean currentlyUpdating = false;
	private boolean currentlyRendering = false;
	
	public Game() {
		super();
		frame = new JFrame(name);
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				while(currentlyUpdating == true || currentlyRendering == true) Thread.yield();
				stop();
				frame.setVisible(false);
				frame.dispose();
			}
		});
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setIconImage(new ImageIcon("res/EmberShieldIcon.png").getImage());
		frame.setVisible(true);
		addKeyListener(kc = new KeyboardControls());
		addMouseListener(cursor = new Cursor());
	}
	
	public synchronized void init() {
		running = true;
		clock = 0;
		
		for(int r = 0; r < SpriteSheet.COLOR_DEPTH; r++) {
			for(int g = 0; g < SpriteSheet.COLOR_DEPTH; g++) {
				for(int b = 0; b < SpriteSheet.COLOR_DEPTH; b++) {
					int index = r * (SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH) + g * (SpriteSheet.COLOR_DEPTH) + b;
					int col = ((r * 0xFF / SpriteSheet.COLOR_DEPTH) << 16) + ((g * 0xFF / SpriteSheet.COLOR_DEPTH) << 8) + (b * 0xFF / SpriteSheet.COLOR_DEPTH);
					//System.out.printf("r: %d g: %d b: %d \n", r, g, b);
					//System.out.printf("index: %d, col: %d \n", index, col);
					colors[index] = col;
				}
			}
		}
		screen = new Screen(width, height);
		
		b = new Board(tileMap[0].length, tileMap.length);
		b.setLandSpriteSheetPath("/Untitled.png");
		Land[][] terrain = new Land[tileMap.length][tileMap[0].length];
		for(int x = 0; x < tileMap[0].length; x++) {
			for(int y = 0; y < tileMap.length; y++) {
				b.setLand(y, x, new Land("Test Land", tileMap[y][x]));
			}
		}
		
		fred = new Unit("Fred", "/Untitled.png", "/TestMotions.txt", 5);
		bob = new Unit("Bob", "/Untitled.png", "/TestMotions.txt", 5);
		b.setUnit(0, 0, fred);
		b.setUnit(0, 1, bob);
		String[] types = {"freddish"};
		
		fred.setUnitClass(new UnitAttacker("freddy"));
		fred.getUnitClass().setEquippableItemTypes(types);
		fred.setStat("HP", 2);
		fred.setStat("Str", 1);
		fred.setSide("Ally");
		bob.setUnitClass(new UnitClass("freddy"));
		bob.setStat("HP", 2);
		bob.setStat("Str", 1);
		bob.setSide("Neutral");
		
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
		long lastTime = System.nanoTime();
		double ticksPerNanoSecond = 60D/1000000000D;
		double ticksPassed = 0;
		while(running) {
			//renders and ticks
			long now = System.nanoTime();
			ticksPassed += (now - lastTime) * ticksPerNanoSecond;
			lastTime = now;
			
			while(ticksPassed >= 1) {
				currentlyUpdating = true;
				update();
				currentlyUpdating = false;
				ticksPassed -= 1;
				tickCount++;
			}
			
			currentlyRendering = true;
			render();
			currentlyRendering = false;
			frameCount++;
			try{
				Thread.sleep(2);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
				
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck += 1000;
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
		for(int i = 0; i < scale && kc.getKey(kc.up) && screen.yOffset < 0; i++) screen.yOffset++;
		for(int i = 0; i < scale && kc.getKey(kc.left) && screen.xOffset < 0; i++) screen.xOffset++;
		for(int i = 0; i < scale && kc.getKey(kc.right) && screen.xOffset > width - b.getBoardWidth() * SpriteSheet.TILE_WIDTH * scale; i++) screen.xOffset--;
		for(int i = 0; i < scale && kc.getKey(kc.down) && screen.yOffset > height - b.getBoardHeight() * SpriteSheet.TILE_WIDTH * scale; i++) screen.yOffset--;
		b.update(clock);
		Point point = getMousePosition();
		if(point != null) { 
			cursor.update(clock, screen, point.x, point.y, scale);
			if(cursor.x / SpriteSheet.TILE_WIDTH >= 0 && cursor.x / SpriteSheet.TILE_WIDTH < b.getBoardWidth() &&
				cursor.y / SpriteSheet.TILE_WIDTH >= 0 && cursor.y / SpriteSheet.TILE_WIDTH < b.getBoardHeight()) {
				Unit cursorUnit = b.getUnits()[cursor.y / SpriteSheet.TILE_WIDTH][cursor.x / SpriteSheet.TILE_WIDTH];
				if(cursorUnit == null) cursor.setSpriteColor(0);
				else if(cursorUnit.getSide().equals("Player")) cursor.setSpriteColor((Unit.PLAYER_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Enemy")) cursor.setSpriteColor((Unit.ENEMY_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Ally")) cursor.setSpriteColor((Unit.ALLY_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Neutral")) cursor.setSpriteColor((Unit.NEUTRAL_OUTLINE * 4) & 511);
			}
		}
		int loggedSelectX = cursor.selectX;
		int loggedSelectY = cursor.selectY;
		int loggedSelectType = cursor.selectType;
		if(kc.getKey(kc.move) && loggedSelectX != -1 && loggedSelectY != -1) {
			if(b.getUnits()[loggedSelectY][loggedSelectX] != null && loggedSelectType == MouseEvent.BUTTON1) {
				class SelectionChangeMover extends Thread {
					
					public void run() {
						int newSelectX = cursor.selectX;
						int newSelectY = cursor.selectY;
						while(loggedSelectX == newSelectX && loggedSelectY == newSelectY) {
							Thread.yield();
						}
						if(newSelectX != -1 && newSelectY != -1) {
							int xDif = newSelectX - loggedSelectX;
							int yDif = newSelectY - loggedSelectY;
							int dirLength = 0;
							if(xDif >= 0) dirLength += xDif;
							else dirLength += xDif * -1;
							if(yDif >= 0) dirLength += yDif;
							else dirLength += yDif * -1;
							int[] directions = new int[dirLength];
							int xDir = -1;
							if(xDif > 0) xDir = 0;
							else if(xDif < 0) xDir = 2;
							int yDir = -1;
							if(yDif > 0) yDir = 3;
							else if(yDif < 0) yDir = 1;
							for(int i = 0; i < dirLength; i++) {
								if(xDif > 0 && i < xDif) directions[i] = xDir;
								else if(xDif < 0 && i < xDif * -1) directions[i] = xDir;
								else directions[i] = yDir;
							}
							System.out.println(loggedSelectX + " " + loggedSelectY + " " + directions.toString());
							b.moveUnitAlongPath(loggedSelectY, loggedSelectX, directions);
						}
					}
				};
				SelectionChangeMover scl = new SelectionChangeMover();
				scl.start();
			}
		}
		else if(kc.getKey(kc.actzero) && loggedSelectX != -1 && loggedSelectY != -1) {
			if(b.getUnits()[loggedSelectY][loggedSelectX] != null && loggedSelectType == MouseEvent.BUTTON1) {
				class SelectionChangeActor extends Thread {
					
					public void run() {
						int newSelectX = cursor.selectX;
						int newSelectY = cursor.selectY;
						while(loggedSelectX == newSelectX && loggedSelectY == newSelectY) {
							Thread.yield();
						}
						if(newSelectX != -1 && newSelectY != -1) {
							if(b.getUnits()[newSelectY][newSelectX] != null) {
								b.getUnits()[loggedSelectY][loggedSelectX].getUnitClass().actZero(b.getUnits()[loggedSelectY][loggedSelectX], b.getUnits()[newSelectY][newSelectX], b);
							}
						}
					}
				};
				SelectionChangeActor scl = new SelectionChangeActor();
				scl.start();
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		b.render(screen, scale);
		cursor.render(screen, scale);
		
		for(int i = 0; i < pixels.length; i++) {
			if (screen.getPixels()[i] != -1) {
				pixels[i] = colors[screen.getPixels()[i]];
			}
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
		sc.close();
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
