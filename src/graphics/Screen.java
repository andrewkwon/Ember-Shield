package graphics;

public class Screen {

	private int width;
	private int height;
	private SpriteSheet sheet;
	private int[] pixels;
	public int xOffset = 0;
	public int yOffset = 0;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void render(int x, int y, int tile, int scale) {
		int tileX = tile % (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
		int tileY = (int) tile / (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
		for(int pixelX = 0; pixelX < SpriteSheet.TILE_WIDTH * scale; pixelX++) {
			int actualX = pixelX + x * scale  + xOffset;
			if(0 <= actualX && actualX < width) {
				for(int pixelY = 0; pixelY < SpriteSheet.TILE_WIDTH * scale; pixelY++) {
					int actualY = pixelY + y * scale + yOffset;
					if(0 <= actualY && actualY < height) {
						pixels[actualX + actualY * width] = 
							sheet.getPixels()[tileX * SpriteSheet.TILE_WIDTH + (pixelX / scale) + (tileY * SpriteSheet.TILE_WIDTH + (pixelY / scale)) * sheet.getWidth()];
					}
				}
			}
		}
	}
	
	public SpriteSheet getSheet() {
		return sheet;
	}
	
	public void setSheet(String spriteSheetPath) {
		if(sheet == null || !sheet.getPath().equals(spriteSheetPath)) sheet = new SpriteSheet(spriteSheetPath);
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}
	
	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}
}
