package board;
import unit.Unit;

public class Board {

	private int boardHeight;
	private int boardWidth;
	private Land[][] terrain;
	private Unit[][] units;
	private Form[][] forms;
	
	public Board(int boardHeight, int boardWidth) {
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
}
