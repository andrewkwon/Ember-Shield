package unit;

import game.Game;
import graphics.DisplayBox;
import graphics.OnScreenText;
import graphics.Screen;
import graphics.SpriteSheet;
import graphics.Tile;

public class UnitInfoDisplay extends DisplayBox {
	
	private Unit unit;
	private OnScreenText name;
	private OnScreenText[] items;
	private OnScreenText[] stats;
	private Tile unitImage;
	
	public UnitInfoDisplay(Screen screen, Unit unit) {
		super(-screen.getXOffset() + SpriteSheet.TILE_WIDTH * Game.scale / 2, -screen.getYOffset() + SpriteSheet.TILE_WIDTH * Game.scale / 2, 32 * 11 * 3, 32 * 4 * 3, 506);
		this.unit = unit;
		unitImage = unit.getSprite().getFrame();
		name = new OnScreenText((getX() + 3 + 1) + SpriteSheet.TILE_WIDTH * Game.scale, (getY() + 3), unit.getName() + "   HP:" + unit.getStat("HP"), 0);
		items = new OnScreenText[Unit.inventorySpace];
		for(int i = 0; i < items.length; i++) {
			if(unit.getItem(i) != null) {
				items[i] = new OnScreenText((getX() + 3), (getY() + 3) + (i + 1) * SpriteSheet.TILE_WIDTH * Game.scale, 
						unit.getItem(i).getName() + "   " + unit.getItem(i).getRemainingUses(), 0);
			}
		}
		stats = new OnScreenText[unit.getStats().size()];
		for(int i = 0; i < stats.length; i++) {
			String key = (String) unit.getStats().keySet().toArray()[i];
			if(!key.equals("HP")) {
				stats[i] = new OnScreenText((getX() + 3) + 8 * SpriteSheet.TILE_WIDTH * Game.scale, (getY() + 3) + (i) * SpriteSheet.TILE_WIDTH,
						key + ": " + unit.getStat(key), 0);
			}
		}
		screen.setLocked(true);
	}
	
	public void render(Screen screen, int scale) {
		super.render(screen, scale);
		unitImage = unit.getSprite().getFrame();
		unitImage.render(screen, (getX() + 3) / Game.scale, (getY() + 3) / Game.scale, Game.scale * scale);
		name.render(screen, scale);
		for(OnScreenText line : items) if(line != null) line.render(screen, scale);
		for(OnScreenText line : stats) if(line != null) line.render(screen, scale);
	}
}
