package controls;

import graphics.Screen;
import graphics.Sprite;
import graphics.SpriteSheet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cursor implements MouseListener {
	
	private Sprite cursorSprite;
	private int spriteColor = 0;
	public int x;
	public int y;
	
	public Cursor() {
		cursorSprite = new Sprite("/UserInterface.png", "/TestMotions.txt", 8);
		cursorSprite.changeAnimationTo("Blink", 0);
	}
	
	public void update(int clock, Screen screen, int mouseX, int mouseY, int scale) {
		x = (mouseX - screen.xOffset) / scale;
		y = (mouseY - screen.yOffset) / scale;
		if(x < 0) x = 0;
		else if(x >= screen.getWidth()) x = screen.getWidth() - 1;
		if(y < 0) y = 0;
		else if(y >= screen.getHeight()) y = screen.getHeight() - 1;
		cursorSprite.setX(x - x % SpriteSheet.TILE_WIDTH);
		cursorSprite.setY(y - y % SpriteSheet.TILE_WIDTH);
		cursorSprite.update(clock);
	}
	
	public void render(Screen screen, int scale) {
		cursorSprite.render(screen, scale);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void setSpriteColor(int color) {
		spriteColor = color;
		cursorSprite.setColorToSwap(0);
		cursorSprite.setSwapTargetColor(spriteColor);
	}
}
