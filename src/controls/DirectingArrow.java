package controls;

import graphics.Screen;
import graphics.Sprite;
import graphics.SpriteSheet;

import java.util.ArrayList;

public class DirectingArrow {

	private int headX = -1;
	private int headY = -1;
	private ArrayList<Integer> directions = new ArrayList<Integer>();
	private boolean settingDirections;
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	
	public DirectingArrow() {
		
	}
	
	public void update(int clock, int cursorX, int cursorY) {
		if(settingDirections) {
			int xDif = cursorX - headX;
			int yDif = cursorY - headY;
			if(!(xDif == 0 && yDif == 0)) {
				int dir = -1;
				while(xDif != 0 || yDif != 0) {
					if(xDif > 0) {
						dir = 0;
						xDif--;
					}
					else if(yDif < 0) {
						dir = 1;
						yDif++;
					}
					else if(xDif < 0) {
						dir = 2;
						xDif++;
					}
					else if(yDif > 0) {
						dir = 3;
						yDif--;
					}
					if(!directions.isEmpty() && directions.get(directions.size() - 1) == (dir + 2) % 4) {
						directions.remove(directions.size() - 1);
					}
					else {
						directions.add(dir);
					}
					
				}
				headX = cursorX;
				headY = cursorY;
				
				while(sprites.size() < directions.size()) {
					sprites.add(new Sprite("/UserInterface.png", "/TestMotions.txt", 17));
				}
				while(sprites.size() > directions.size()) {
					sprites.remove(sprites.size() - 1);
				}
				int spriteX = 0;
				int spriteY = 0;
				for(int i = directions.size() - 1; i >= 0; i--) {
					int d = directions.get(i);
					if(i == directions.size() - 1) {
						spriteX = headX;
						spriteY = headY;
						if(d % 4 == 0 || d % 4 == 2) sprites.get(i).changeAnimationTo("RightArrow");
						else sprites.get(i).changeAnimationTo("UpArrow");
					}
					else sprites.get(i).changeAnimationTo("Dot");
					System.out.println(i + ": " + sprites.get(i).getCurrentAnimation());
					sprites.get(i).setX(spriteX * SpriteSheet.TILE_WIDTH);
					sprites.get(i).setY(spriteY * SpriteSheet.TILE_WIDTH);
					sprites.get(i).update(clock);
					int spriteXDif = 0;
					int spriteYDif = 0;
					if(d % 4 == 0) spriteXDif = 1;
					else if(d % 4 == 1) spriteYDif = -1;
					else if(d % 4 == 2) spriteXDif = -1;
					else if(d % 4 == 3) spriteYDif = 1;
					spriteX -= spriteXDif;
					spriteY -= spriteYDif;
				}
			}
		}
	}
	
	public void render(Screen screen, int scale) {
		if(settingDirections) {
			for(int i = sprites.size() - 1; i >= 0; i--) {
				boolean mirrorX = false;
				boolean mirrorY = false;
				if(!directions.isEmpty() && i == sprites.size() - 1) {
					int d = directions.get(i);
					if(d % 4 == 2) mirrorX = true;
					else if(d % 4 == 3) mirrorY = true;
				}
				sprites.get(i).render(screen, scale, mirrorX, mirrorY);
			}
		}
	}
	
	public int getHeadX() {
		return headX;
	}
	
	public int getHeadY() {
		return headY;
	}
	
	public void setHeadX(int headX) {
		this.headX = headX;
	}
	
	public void setHeadY(int headY) {
		this.headY = headY;
	}
	
	public int[] readDirections() {
		settingDirections = false;
		int[] reading = new int[directions.size()];
		for(int i = 0; i < reading.length; i++) reading[i] = directions.get(i);
		directions.clear();
		sprites.clear();
		headX = -1;
		headY = -1;
		return reading;
	}
	
	public boolean getSettingDirections() {
		return settingDirections;
	}
	
	public void setSettingDirections(boolean settingDirections) {
		this.settingDirections = settingDirections;
	}
}
