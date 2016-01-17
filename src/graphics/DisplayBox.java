package graphics;

//a box in which to contain displays
public class DisplayBox {
	
	//x and y of top left corner
	private int x;
	private int y;
	private int width;
	private int height;
	private Tile[][] boxTiles;
	private int tilesPerRow;
	private int tilesPerColumn;
	private int color;
	public static final int CORNER = 9;
	public static final int HORIZONTAL_EDGE = 10;
	public static final int VERTICAL_EDGE = 11;
	public static final int INSIDE = 71;
	
	public DisplayBox(int x, int y, int width, int height, int color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		init();
	}
	
	private void init() {

		//number of tiles per row and column
		tilesPerRow = width / SpriteSheet.TILE_WIDTH;
		if(width % SpriteSheet.TILE_WIDTH != 0 || tilesPerRow <= 1) tilesPerRow++;
		tilesPerColumn = height / SpriteSheet.TILE_WIDTH;
		if(height % SpriteSheet.TILE_WIDTH != 0 || tilesPerColumn <= 1) tilesPerColumn++;
		
		boxTiles = new Tile[tilesPerColumn][tilesPerRow];
		
		int tileType = -1;
		for(int row = 0; row < tilesPerColumn; row++) {
			for(int column = 0; column < tilesPerRow; column++) {
				if(row > 0 && row < tilesPerColumn - 1 &&
						column > 0 && column < tilesPerRow - 1) tileType = INSIDE;
				else if((row == 0 || row == tilesPerColumn - 1) && 
						(column == 0 || column == tilesPerRow - 1)) tileType = CORNER;
				else if(row == 0 || row == tilesPerColumn - 1) tileType = HORIZONTAL_EDGE;
				else if(column == 0 || column == tilesPerRow - 1) tileType = VERTICAL_EDGE;
				
				boxTiles[row][column] = new Tile(tileType);
				if(row + 1 > tilesPerColumn / 2) boxTiles[row][column].setMirrorY(true);
				if(column + 1 > tilesPerRow / 2) boxTiles[row][column].setMirrorX(true);
				boxTiles[row][column].setColorToSwap(511);
				boxTiles[row][column].setSwapTargetColor(color);
			}
		}
	}
	
	public void render(Screen screen, int scale) {		
		for(int row = 0; row < tilesPerColumn; row++) {
			for(int column = 0; column < tilesPerRow; column++) {
				int tileX = x + column * SpriteSheet.TILE_WIDTH;
				int tileY = y + row * SpriteSheet.TILE_WIDTH;
				if(row == tilesPerColumn - 1) tileY = y + height - SpriteSheet.TILE_WIDTH;
				if(column == tilesPerRow - 1) tileX = x + width - SpriteSheet.TILE_WIDTH;
				
				boxTiles[row][column].render(screen, tileX, tileY, scale);
			}
		}
	}
	
	public void setWidth(int width) {
		this.width = width;
		init();
	}
	
	public void setHeight(int height) {
		this.height = height;
		init();
	}
}
