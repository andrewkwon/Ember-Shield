package graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Screen {

	private int width;
	private int height;
	private Map<String, SpriteSheet> loadedSheets = new HashMap<String, SpriteSheet>(5);
	private String currentSpriteSheetPath;
	private SpriteSheet sheet;
	private int[] pixels;
	//pixelHasBeenRendered tells which pixels have already been rendered
	//used for attempt at optimization, telling the screen it doesn't need to render veiled pixels
	private boolean[] pixelHasBeenRendered;
	//the toRender LIFO tells the screen what to render at updateScreen's call
	private Stack<RenderData> toRender;
	private int xOffset = 0;
	private int yOffset = 0;
	//determines whether offset can be adjusted
	private boolean locked = false;
	//noncolor: not used as a color value, pixels can't have this as a color value
	public static int NONCOLOR = -2;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		toRender = new Stack<RenderData>();
	}
	
	public void render(int x, int y, int tile, int scale, int color, int targetColor, boolean mirrorX, boolean mirrorY, double shadeFactor) {
		toRender.push(new RenderData(currentSpriteSheetPath, x, y, tile, scale, color, targetColor, mirrorX, mirrorY, shadeFactor));
	}
	
	public void updateScreen() {
		pixelHasBeenRendered = new boolean[pixels.length];
		//render objects in order from foreground to background
		while(!toRender.empty()) {
			RenderData toDrawToScreen = toRender.pop();
			setSheet(toDrawToScreen.getSpriteSheetPath());
			loadSheet();
			int x = toDrawToScreen.getX();
			int y = toDrawToScreen.getY();
			int tile = toDrawToScreen.getTile();
			int scale = toDrawToScreen.getScale();
			int color = toDrawToScreen.getColor();
			int targetColor = toDrawToScreen.getTargetColor();
			boolean mirrorX = toDrawToScreen.getMirrorX();
			boolean mirrorY = toDrawToScreen.getMirrorY();
			double shadeFactor = toDrawToScreen.getShadeFactor();
			int tileX = tile % (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
			int tileY = (int) tile / (sheet.getWidth() / SpriteSheet.TILE_WIDTH);
			for(int pixelX = 0; pixelX < SpriteSheet.TILE_WIDTH * scale; pixelX++) {
				int actualX = pixelX + x * scale  + xOffset;
				if(mirrorX) actualX += (SpriteSheet.TILE_WIDTH * scale - 2 * pixelX - 1);
				if(0 <= actualX && actualX < width) {
					for(int pixelY = 0; pixelY < SpriteSheet.TILE_WIDTH * scale; pixelY++) {
						int actualY = pixelY + y * scale + yOffset;
						if(mirrorY) actualY += (SpriteSheet.TILE_WIDTH * scale - 2 * pixelY - 1);
						if(0 <= actualY && actualY < height) {
							//if the pixel is veiled, don't render it
							if(pixelHasBeenRendered[actualX + actualY * width]) continue;
							int pixelData = sheet.getPixels()[tileX * SpriteSheet.TILE_WIDTH + (pixelX / scale) + (tileY * SpriteSheet.TILE_WIDTH + (pixelY / scale)) * sheet.getWidth()];
							if(pixelData != SpriteSheet.TRANSPARENT_COLOR){
								if(shadeFactor != 1) {
									int r = (int) (((pixelData % (SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH)) & 7) * shadeFactor);
									int g = (int) (((pixelData % (SpriteSheet.COLOR_DEPTH)) & 7) * shadeFactor);
									int b = (int) (((pixelData) & 7) * shadeFactor);
									pixelData = (r << 6) + (g << 3) + (b);
								}
								if(pixelData == color) pixelData = targetColor;
								pixels[actualX + actualY * width] = 
										pixelData & 511;
								pixelHasBeenRendered[actualX + actualY * width] = true;
							}
						}
					}
				}
			}
		}
	}
	
	public SpriteSheet getSheet() {
		return sheet;
	}
	
	public void setSheet(String spriteSheetPath) {
		currentSpriteSheetPath = spriteSheetPath;
	}
	
	//loads the currentSpriteSheetPath
	public void loadSheet() {
		if(sheet == null || !sheet.getPath().equals(currentSpriteSheetPath)) {
			if(loadedSheets.containsKey(currentSpriteSheetPath)) sheet = loadedSheets.get(currentSpriteSheetPath); 
			else {
				sheet = new SpriteSheet(currentSpriteSheetPath);
				loadedSheets.put(currentSpriteSheetPath, sheet);
			}
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public void setXOffset(int xOffset) {
		if(!locked) this.xOffset = xOffset;
	}
	
	public void setYOffset(int yOffset) {
		if(!locked) this.yOffset = yOffset;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void clearLoadedSheets() {
		loadedSheets.clear();
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
