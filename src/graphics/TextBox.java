package graphics;

public class TextBox extends DisplayBox {
	
	private String text;
	private OnScreenText[] textRows;

	public TextBox(int x, int y, int charactersPerRow, String text, int boxColor, int textColor) {
		super(x, y, charactersPerRow * SpriteSheet.TILE_WIDTH + 2, (text.length() / charactersPerRow) * SpriteSheet.TILE_WIDTH + SpriteSheet.TILE_WIDTH + 2, boxColor);
		this.text = text;
		if((text.length() / charactersPerRow) * SpriteSheet.TILE_WIDTH + 2 < 34) setHeight(34);
		//makes a different text object for each row of text
		textRows = new OnScreenText[text.length() / charactersPerRow + 1];
		for(int row = 0; row < text.length() / charactersPerRow + 1; row++) {
			//tell where we are in the text
			System.out.printf("height: %d \n", (text.length() / charactersPerRow) * SpriteSheet.TILE_WIDTH + 2);
			int startIndex = row * charactersPerRow;
			int endIndex = (row + 1) * charactersPerRow;
			if(endIndex >= text.length()) endIndex = text.length();
			textRows[row] = new OnScreenText(x + 1, y + 1 + row * SpriteSheet.TILE_WIDTH, text.substring(startIndex, endIndex), textColor);
		}
	}
	
	public void render(Screen screen, int scale) {
		super.render(screen, scale);
		for(OnScreenText row : textRows) row.render(screen, scale);
	}
}
