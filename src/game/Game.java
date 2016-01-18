package game;
import unit.Unit;
import unit.UnitInfoDisplay;
import graphics.DisplayBox;
import graphics.OnScreenText;
import graphics.Screen;
import graphics.SpriteSheet;
import graphics.TextBox;
import item.Item;
import unit.UnitClass;
import item.Weapon;
import board.Board;
import board.Land;
import unit.UnitAttacker;

import java.util.ArrayList;
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
import controls.DirectingArrow;
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
	public static final int scale = 3;
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
	private int loggedSelectX = -1;
	private int loggedSelectY = -1;
	private int loggedSelectType = -1;
	private DirectingArrow directingArrow;
	public int turnNumber = 0;
	public String[] turnOrder;
	private DisplayBox displayBox;
	
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
		directingArrow = new DirectingArrow();
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
				b.setLand(y, x, new Land("Test Land", "/Untitled.png", tileMap[y][x]));
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
		fred.setStat("Move", 4);
		fred.setSide("Player");
		bob.setUnitClass(new UnitClass("freddy"));
		bob.setStat("HP", 1);
		bob.setStat("Str", 1);
		bob.setStat("Move", 2);
		bob.setSide("Enemy");
		
		Weapon fredinator = new Weapon("Fredinator");
		fredinator.setUsable(true);
		fredinator.setItemType(types[0]);
		fredinator.setRemainingUses(1);
		fredinator.setDoesPhysicalDamage(true);
		fredinator.setStat("Mt", 1);
		int[] rng = {1, 2};
		fredinator.setRng(rng);
		System.out.println("fred.giveItem(fredinator): " + fred.giveItem(fredinator));
		System.out.println("fred.equip(0): " + fred.equip(0));
		System.out.println("fred.getUnitClass() instanceof UnitAttacker: " + (fred.getUnitClass() instanceof UnitAttacker));
		
		ArrayList<String> sides = new ArrayList<String>();
		for(Unit[] row : b.getUnits()) {
			for(Unit u : row) {
				if(u != null) {
					String s = u.getSide();
					if(!sides.contains(s)) {
						sides.add(s);
					}
				}
			}
		}
		turnOrder = new String[sides.size()];
		for(int i = 0; i < sides.size(); i++) turnOrder[i] = sides.get(i);
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
		for(int i = 0; i < scale && kc.getKey(kc.up) && screen.getYOffset() < 0; i++) screen.setYOffset(screen.getYOffset() + 1);
		for(int i = 0; i < scale && kc.getKey(kc.left) && screen.getXOffset() < 0; i++) screen.setXOffset(screen.getXOffset() + 1);
		for(int i = 0; i < scale && kc.getKey(kc.right) && screen.getXOffset() > width - b.getBoardWidth() * SpriteSheet.TILE_WIDTH * scale; i++) screen.setXOffset(screen.getXOffset() - 1);
		for(int i = 0; i < scale && kc.getKey(kc.down) && screen.getYOffset() > height - b.getBoardHeight() * SpriteSheet.TILE_WIDTH * scale; i++) screen.setYOffset(screen.getYOffset() - 1);
		b.update(clock);
		Point point = getMousePosition();
		int cursorX = -1;
		int cursorY = -1;
		if(point != null) { 
			cursor.update(clock, screen, point.x, point.y, scale);
			cursorX = cursor.x;
			cursorY = cursor.y;
			if(cursorX / SpriteSheet.TILE_WIDTH >= 0 && cursorX / SpriteSheet.TILE_WIDTH < b.getBoardWidth() &&
				cursorY / SpriteSheet.TILE_WIDTH >= 0 && cursorY / SpriteSheet.TILE_WIDTH < b.getBoardHeight()) {
				Unit cursorUnit = b.getUnits()[cursorY / SpriteSheet.TILE_WIDTH][cursorX / SpriteSheet.TILE_WIDTH];
				if(cursorUnit == null) cursor.setSpriteColor(0);
				else if(cursorUnit.getSide().equals("Player")) cursor.setSpriteColor((Unit.PLAYER_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Enemy")) cursor.setSpriteColor((Unit.ENEMY_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Ally")) cursor.setSpriteColor((Unit.ALLY_OUTLINE * 4) & 511);
				else if(cursorUnit.getSide().equals("Neutral")) cursor.setSpriteColor((Unit.NEUTRAL_OUTLINE * 4) & 511);
			}
		}
		
		if(loggedSelectType == MouseEvent.BUTTON1 && loggedSelectX != -1 && loggedSelectY != -1 && b.getUnits()[loggedSelectY][loggedSelectX] != null) {
			if(kc.getKey(kc.move) && cursor.getSelectedUnit() != null && cursor.getSelectedUnit().getSide() == turnOrder[turnNumber % turnOrder.length]) {
				int newSelectX = cursor.selectX;
				int newSelectY = cursor.selectY;
				if(directingArrow.getHeadX() == -1 || directingArrow.getHeadY() == -1) {
					directingArrow.setSelectedUnit(b.getUnits()[loggedSelectY][loggedSelectX]);
					directingArrow.setHeadX(loggedSelectX);
					directingArrow.setHeadY(loggedSelectY);
					if(!directingArrow.getSettingDirections()) directingArrow.setSettingDirections(true);
				}
				directingArrow.update(clock, cursorX / SpriteSheet.TILE_WIDTH, cursorY / SpriteSheet.TILE_WIDTH);
				if(newSelectX != -1 && newSelectY != -1 && !(newSelectX == loggedSelectX && newSelectY == loggedSelectY)) {
					b.moveUnitAlongPath(loggedSelectY, loggedSelectX, directingArrow.readDirections());
				}
			}
			else {
				directingArrow.clear();
				if(kc.getKey(kc.actzero) && cursor.getSelectedUnit() != null && cursor.getSelectedUnit().getSide() == turnOrder[turnNumber % turnOrder.length]) {
					int newSelectX = cursor.selectX;
					int newSelectY = cursor.selectY;
					if(newSelectX != -1 && newSelectY != -1 && !(newSelectX == loggedSelectX && newSelectY == loggedSelectY)) {
						if(b.getUnits()[newSelectY][newSelectX] != null) {
							b.getUnits()[loggedSelectY][loggedSelectX].getUnitClass().act(0, b.getUnits()[loggedSelectY][loggedSelectX], b.getUnits()[newSelectY][newSelectX], b);
						}
					}
				}
				
				else if(kc.getKey(kc.turnend)) {
					turnNumber++;
					for(Unit[] row : b.getUnits()) {
						for(Unit u : row) {
							if(u != null) u.refresh();
						}
					}
					System.out.println(turnOrder[turnNumber % turnOrder.length]);
				}
			}
		}
		else if(loggedSelectType == MouseEvent.BUTTON3) {
			if(loggedSelectX != -1 && loggedSelectY != -1 && b.getUnits()[loggedSelectY][loggedSelectX] != null) {
				displayBox = new UnitInfoDisplay(screen, b.getUnits()[loggedSelectY][loggedSelectX]);
			}
			else {
				screen.setLocked(false);
				displayBox = null;
			}
		}

		loggedSelectX = cursor.selectX;
		loggedSelectY = cursor.selectY;
		loggedSelectType = cursor.selectType;
		if(loggedSelectX != -1 && loggedSelectY != -1)cursor.setSelectedUnit(b.getUnits()[loggedSelectY][loggedSelectX]);
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
		directingArrow.render(screen, scale);
		if(displayBox != null) displayBox.render(screen, 1);
		
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
		if(getBufferStrategy() != null) getBufferStrategy().dispose();
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
