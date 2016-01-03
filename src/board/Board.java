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
		for(int row = 0; row < boardHeight; row++) {
			for(int column = 0; column < boardWidth; column++) {
				if(unit == units[row][column]) {
					units[row][column] = null;
					return;
				}
			}
		}
	}
	
	public int[] getUnitCoordinates(Unit unit) {
		int[] coordinates = new int[2];
		for(int row = 0; row < boardHeight; row++) {
			for(int col = 0; col < boardWidth; col++) {
				if(units[row][col] == unit) {
					coordinates[0] = row;
					coordinates[1] = col;
					break;
				}
			}
		}
		return coordinates;
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
	
	//moves unit one space, returns change in x and y
	public boolean moveUnit(int row, int column, int direction) {
		if(units[row][column] == null) return false;
		//direction: 0=right, 1=up, 2=left, 3=down
		units[row][column].setFacing(direction);
		int xDir = 0;
		int yDir = 0;
		if(direction % 4 == 0) xDir = 1;
		else if(direction % 4 == 1) yDir = -1;
		else if(direction % 4 == 2) xDir = -1;
		else if(direction % 4 == 3) yDir = 1;
		if(row + yDir < 0 || row + yDir >= boardHeight ||
				column + xDir < 0 || column + xDir >= boardWidth ||
				units[row + yDir][column + xDir] != null) return false;
		else {
			units[row][column].setMoving(true);
			units[row][column].setXMov(xDir * SpriteSheet.TILE_WIDTH);
			units[row][column].setYMov(yDir * SpriteSheet.TILE_WIDTH);
			if(direction % 4 == 0 || direction % 4 == 2) units[row][column].getSprite().changeAnimationTo("WalkingRight");
			if(direction % 4 == 1) units[row][column].getSprite().changeAnimationTo("WalkingUp");
			if(direction % 4 == 3) units[row][column].getSprite().changeAnimationTo("WalkingDown");
			units[row][column].setXOffset(-xDir * SpriteSheet.TILE_WIDTH);
			units[row][column].setYOffset(-yDir * SpriteSheet.TILE_WIDTH);
			units[row + yDir][column + xDir] = units[row][column];
			units[row][column] = null;
			return true;
		}
	}
	
	public void moveUnitAlongPath(int row, int column, int[] directions) {
		if(!units[row][column].getCanMove()) return;
		units[row][column].setDirections(directions);
	}
	
	public void update(int clock) {
		for(int row = 0; row < boardHeight; row++) {
			for(int column = 0; column < boardWidth; column++) {
				if(units[row][column] != null) {
					units[row][column].update(clock, this, row, column);
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
