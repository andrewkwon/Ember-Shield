package board;

import graphics.Screen;

import java.util.Map;

public class Land {

	private String name;
	private int tile;
	private Map<String, Integer> buffs;
	
	public Land(String name, int tile) {
		this.name = name;
		this.tile = tile;
	}
	
	public void render(Screen screen, int x, int y, int scale) {
		screen.render(x, y, tile, scale, Screen.NONCOLOR, Screen.NONCOLOR);
	}
}
