package graphics;

//this object holds data about when the screen's render function is called
public class RenderData {
	
	private String spriteSheetPath;
	private int x;
	private int y;
	private int tile;
	private int scale;
	private int color;
	private int targetColor;
	private boolean mirrorX;
	private boolean mirrorY;
	private double shadeFactor;

	public RenderData(String spriteSheetPath, int x, int y, int tile, int scale, int color, int targetColor, boolean mirrorX, boolean mirrorY, double shadeFactor) {
		this.spriteSheetPath = spriteSheetPath;
		this.x = x;
		this.y = y;
		this.tile = tile;
		this.scale = scale;
		this.color = color;
		this.targetColor = targetColor;
		this.mirrorX = mirrorX;
		this.mirrorY = mirrorY;
		this.shadeFactor = shadeFactor;
	}
	
	public String getSpriteSheetPath() {
		return spriteSheetPath;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getTile() {
		return tile;
	}
	
	public int getScale() {
		return scale;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getTargetColor() {
		return targetColor;
	}
	
	public boolean getMirrorX() {
		return mirrorX;
	}
	
	public boolean getMirrorY() {
		return mirrorY;
	}
	
	public double getShadeFactor() {
		return shadeFactor;
	}
}
