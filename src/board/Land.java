package board;

import graphics.Screen;
import graphics.Tile;

import java.util.Map;

public class Land {

	private String name;
	private Tile tile;
	private Map<String, Integer> buffs;
	
	public Land(String name, String spriteSheetPath, int tileIndex) {
		this.name = name;
		tile = new Tile(spriteSheetPath, tileIndex);
	}
	
	public void render(Screen screen, int x, int y, int scale) {
		tile.render(screen, x, y, scale);
	}
}
