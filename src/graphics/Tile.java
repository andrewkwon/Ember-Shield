package graphics;

public class Tile {

	private String spriteSheetPath;
	private int tileIndex;
	private int colorToSwap = Screen.NONCOLOR;
	private int swapTargetColor = Screen.NONCOLOR;
	private boolean mirrorX;
	private boolean mirrorY;

	public Tile(String spriteSheetPath, int tileIndex) {
		this.spriteSheetPath = spriteSheetPath;
		this.tileIndex = tileIndex;
	}

	public void render(Screen screen, int x, int y, int scale) {
		screen.setSheet(spriteSheetPath);
		screen.render(x, y, tileIndex, scale, colorToSwap, swapTargetColor, mirrorX, mirrorY, 1.0);
	}
	
	public void setColorToSwap(int colorToSwap) {
		this.colorToSwap = colorToSwap;
	}
	
	public void setSwapTargetColor(int swapTargetColor) {
		this.swapTargetColor = swapTargetColor;
	}
	
	public void setMirrorX(boolean mirrorX) {
		this.mirrorX = mirrorX;
	}
	
	public void setMirrorY(boolean mirrorY) {
		this.mirrorY = mirrorY;
	}
}
