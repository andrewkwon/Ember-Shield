package graphics;

public class OnScreenText {

	private String text;
	private int x;
	private int y;
	public static final String spriteSheetPath = "/UserInterface.png";
	//index of first character on sheet
	public static final int startIndex = 12;
	public static final String readableCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ";
	private int scale = 1;
	private int textColor;
	private int xMov = 0;
	private int yMov = 0;
	private int counter = -1;
	
	public OnScreenText(int x, int y, String text, int textColor) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.textColor = textColor;
	}
	
	public void update() {
		if(xMov != 0) {
			int xDif = 4;
			if(xMov > 0) {
				x += xDif;
				xMov -= xDif;
			}
			else {
				x -= xDif;
				xMov += xDif;
			}
		}
		if(yMov != 0) {
			int yDif = 4;
			if(yMov > 0) {
				y += yDif;
				yMov -= yDif;
			}
			else {
				y -= yDif;
				yMov += yDif;
			}
		}
		if(counter != -1) counter++;
	}
	
	public void render(Screen screen) {
		screen.setSheet(spriteSheetPath);
		for(int i = 0; i < text.length(); i++) {
			int tile = startIndex + readableCharacters.indexOf(text.charAt(i));
			screen.render(x + i * SpriteSheet.TILE_WIDTH, y, tile, scale, 0, textColor, false, false, 1.0);
		}
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getXMov() {
		return xMov;
	}
	
	public int getYMov() {
		return yMov;
	}
	
	public void setXMov(int xMov) {
		this.xMov = xMov;
	}
	
	public void setYMov(int yMov) {
		this.yMov = yMov;
	}
	
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public void setCounter(int counter) {
		this.counter = counter;
	}
}
