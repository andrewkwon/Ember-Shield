package board;
import unit.Unit;

public class Board {

	private int boardHeight;
	private int boardWidth;
	private Land[][] terrain = new Land[boardHeight][boardWidth];
	private Unit[][] units = new Unit[boardHeight][boardWidth];
	private Form[][] forms = new Form[boardHeight][boardWidth];
}
