package board;
import graphics.Screen;
import graphics.SpriteSheet;
import unit.Unit;

public class Board {

	private int boardHeight;
	private int boardWidth;
	private Land[][] terrain;
	private Unit[][] units;
	private Form[][] forms;
	private String landSpriteSheetPath;
	private int selectedRow;
	private int selectedColumn;
	
	public Board(int boardWidth, int boardHeight) {
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		terrain = new Land[boardHeight][boardWidth];
		units = new Unit[boardHeight][boardWidth];
		forms = new Form[boardHeight][boardWidth];
	}
	
	public Unit[][] getUnits() {
		return units;
	}
	
	public void removeUnit(Unit unit) {
		for(int row = 0; row < 1; row++) {
			for(int column = 0; column < 2; column++) {
				if(unit == units[row][column]) {
					units[row][column] = null;
					return;
				}
			}
		}
	}
	
	public void setUnit(int row, int column, Unit unit) {
		units[row][column] = unit;
	}
	
	public int getBoardWidth() {
		return boardWidth;
	}
	
	public int getBoardHeight() {
		return boardHeight;
	}
	
	//moves unit one space
	public int[] moveUnit(int row, int column, int direction) {
		if(units[row][column] == null) return null;
		//direction: 0=right, 1=up, 2=left, 3=down
		int xDir = 0;
		int yDir = 0;
		if(direction % 4 == 0) xDir = 1;
		else if(direction % 4 == 1) yDir = -1;
		else if(direction % 4 == 2) xDir = -1;
		else if(direction % 4 == 3) yDir = 1;
		int[] dir = {xDir, yDir};
		if(row + yDir < 0 || row + yDir >= boardHeight ||
				column + xDir < 0 || column + xDir >= boardWidth ||
				units[row + yDir][column + xDir] != null) return null;
		else {
			units[row][column].setMoving(true);
			units[row][column].setXMov(xDir * SpriteSheet.TILE_WIDTH);
			units[row][column].setYMov(yDir * SpriteSheet.TILE_WIDTH);
			units[row][column].getSprite().changeAnimationTo("Walking");
			units[row][column].setXOffset(-xDir * SpriteSheet.TILE_WIDTH);
			units[row][column].setYOffset(-yDir * SpriteSheet.TILE_WIDTH);
			units[row + yDir][column + xDir] = units[row][column];
			units[row][column] = null;
			return dir;
		}
	}
	
	public void moveUnitAlongPath(int row, int column, int[] directions) {
		
		class moverThread extends Thread{
			
			public void run() {
				move();
			}
			
			public boolean move() {
				if(units[row][column] == null) return false;
				int currentRow = row;
				int currentColumn = column;
				for(int i = 0; i < directions.length; i++) {
					while(units[currentRow][currentColumn].isMoving()) Thread.yield();
					int[] dir = moveUnit(currentRow, currentColumn, directions[i]);
					if(dir == null) {
						return false;
					}
					else {
						currentRow += dir[1];
						currentColumn += dir[0];
					}
				}
				return true;
			}
		}
		Thread mover = new moverThread();
		mover.start();
	}
	
	public void update(int clock) {
		for(int row = 0; row < boardHeight; row++) {
			for(int column = 0; column < boardWidth; column++) {
				if(units[row][column] != null) {
					units[row][column].update(clock);
				}
			}
		}
	}
	
	public void render(Screen screen, int scale) {
		screen.setSheet(landSpriteSheetPath);
		for(int x = 0; x < boardWidth; x++) {
			for(int y = 0; y < boardHeight; y++) {
				if(terrain[y][x] != null) terrain[y][x].render(screen, x * SpriteSheet.TILE_WIDTH, y * SpriteSheet.TILE_WIDTH, scale);
			}
		}
		for(int row = 0; row < boardHeight; row++) {
			for(int column = 0; column < boardWidth; column++) {
				if(units[row][column] != null) {
					units[row][column].render(screen, scale, column * SpriteSheet.TILE_WIDTH, row * SpriteSheet.TILE_WIDTH);
				}
			}
		}
	}
	
	public void setLandSpriteSheetPath(String landSpriteSheetPath) {
		this.landSpriteSheetPath = landSpriteSheetPath;
	}
	
	public void setLand(int row, int column, Land land) {
		terrain[row][column] = land;
	}
}
