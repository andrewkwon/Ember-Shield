package controls;

import graphics.Screen;
import graphics.Sprite;
import graphics.SpriteSheet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import unit.Unit;

public class Cursor implements MouseListener {
	
	private Sprite cursorSprite;
	private int spriteColor = 0;
	public int x = 0;
	public int y = 0;
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public int selectX = -1;
	public int selectY = -1;
	public int selectType = -1;
	private Sprite selectSprite;
	private Unit selectedUnit;
	
	public Cursor() {
		cursorSprite = new Sprite("/UserInterface.png", "/TestMotions.txt", 12);
		cursorSprite.changeAnimationTo("Blink");
		selectSprite = new Sprite("/UserInterface.png", "/TestMotions.txt", 15);
		selectSprite.changeAnimationTo("Spin");
	}
	
	public void update(int clock, Screen screen, int mouseX, int mouseY, int scale) {
		x = (mouseX - screen.getXOffset()) / scale;
		y = (mouseY - screen.getYOffset()) / scale;
		if(x < 0) x = 0;
		else if(x >= screen.getWidth()) x = screen.getWidth() - 1;
		if(y < 0) y = 0;
		else if(y >= screen.getHeight()) y = screen.getHeight() - 1;
		cursorSprite.setX(x - x % SpriteSheet.TILE_WIDTH);
		cursorSprite.setY(y - y % SpriteSheet.TILE_WIDTH);
		cursorSprite.update(clock);
		
		selectSprite.setX(selectX * SpriteSheet.TILE_WIDTH);
		selectSprite.setY(selectY * SpriteSheet.TILE_WIDTH);
		selectSprite.update(clock);
	}
	
	public void render(Screen screen, int scale) {
		cursorSprite.render(screen, scale, false, false);
		selectSprite.render(screen, scale, false, false);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1) leftPressed = true;
		if(arg0.getButton() == MouseEvent.BUTTON3) rightPressed = true;
		if(leftPressed || rightPressed) {
			int newSelectX = x / SpriteSheet.TILE_WIDTH;
			int newSelectY = y / SpriteSheet.TILE_WIDTH;
			if(selectX == newSelectX && selectY == newSelectY && selectType == arg0.getButton()) {
				selectX = -1;
				selectY = -1;
			}
			else {
				selectX = newSelectX;
				selectY = newSelectY;
				selectType = arg0.getButton();
			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1) leftPressed = false;
		if(arg0.getButton() == MouseEvent.BUTTON3) rightPressed = false;
	}
	
	public void setSpriteColor(int color) {
		spriteColor = color;
		cursorSprite.setColorToSwap(0);
		cursorSprite.setSwapTargetColor(spriteColor);
	}
	
	public void setSelectedUnit(Unit selectedUnit) {
		this.selectedUnit = selectedUnit;
	}
	
	public Unit getSelectedUnit() {
		return selectedUnit;
	}
}
