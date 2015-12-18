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
	
	public void update(int clock) {
		for(int row = 0; row < 1; row++) {
			for(int column = 0; column < 2; column++) {
				if(units[row][column] != null) {
					units[row][column].update(clock);
				}
			}
		}
	}
	
	public void render(Screen screen, int scale) {
		screen.setSheet(landSpriteSheetPath);
		for(int x = 0; x < terrain[0].length; x++) {
			for(int y = 0; y < terrain.length; y++) {
				if(terrain[y][x] != null) terrain[y][x].render(screen, x * SpriteSheet.TILE_WIDTH, y * SpriteSheet.TILE_WIDTH, scale);
			}
		}
		for(int row = 0; row < 1; row++) {
			for(int column = 0; column < 2; column++) {
				if(units[row][column] != null) {
					units[row][column].render(screen, scale);
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
