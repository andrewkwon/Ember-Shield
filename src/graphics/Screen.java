package graphics;

public class Screen {

	private int width;
	private int height;
	private SpriteSheet sheet;
	private int[] pixels;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void render(int x, int y, int tile, int scale) {
		int tileX = tile % (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
		int tileY = (int) tile / (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
		for(int pixelX = 0; pixelX < SpriteSheet.TILE_WIDTH * scale; pixelX++) {
			for(int pixelY = 0; pixelY < SpriteSheet.TILE_WIDTH * scale; pixelY++) {
				pixels[pixelX + x * SpriteSheet.TILE_WIDTH * scale + width * (pixelY + y *  SpriteSheet.TILE_WIDTH * scale)] = 
						sheet.getPixels()[tileX * SpriteSheet.TILE_WIDTH + (pixelX / scale) + (tileY * SpriteSheet.TILE_WIDTH + (pixelY / scale)) * sheet.getWidth()];
			}
		}
	}
	
	public SpriteSheet getSheet() {
		return sheet;
	}
	
	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}
	
	public int[] getPixels() {
		return pixels;
	}
}
