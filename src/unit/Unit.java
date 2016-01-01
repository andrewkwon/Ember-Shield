package unit;
import java.util.Map;

import item.Item;
import board.Board;

import java.util.HashMap;

import graphics.OnScreenText;
import graphics.Screen;
import graphics.Sprite;
import graphics.SpriteSheet;

public class Unit {
	
	private String name;
	private int level = 0;
	private Map<String, Integer> stats;
	//fire emblem awakening stats are: HP Str Mag Skl Spd Lck Def Res Move
	private Map<String, Integer> growthRates;
	//growth of stats per level up
	private UnitClass unitClass;
	//max stat modifiers to be added?
	private final int inventorySpace = 4;
	private Item[] inventory = new Item[inventorySpace];
	private int equipped = -1;
	private boolean canMove = true;
	private boolean active = true;
	private String faction;
	private String side;
	public static final int PLAYER_OUTLINE = 1;
	public static final int ENEMY_OUTLINE = 64;
	public static final int ALLY_OUTLINE = 8;
	public static final int NEUTRAL_OUTLINE = PLAYER_OUTLINE + ENEMY_OUTLINE;
	private Sprite sprite;
	private int xOffset = 0;
	private int yOffset = 0;
	private int xMov = 0;
	private int yMov = 0;
	private boolean moving = false;
	private int facing = 0;
	private int[] directions;
	private int directionsIndex;
	private OnScreenText damageNumbers;
	private boolean takingDamage = false;
	private boolean dead = false;
	
	public Unit(String name, String spriteSheetPath, String motionsFilePath, int motionsStartLine) {
		this.name = name;
		String[] keys = {"HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res", "Move"};
		stats = new HashMap(keys.length);
		growthRates = new HashMap(keys.length);
		for(String k : keys) stats.put(k, 0);
		growthRates.putAll(stats);
		sprite = new Sprite(spriteSheetPath, motionsFilePath, motionsStartLine);
	}
	
	public boolean equip(int itemId) {
		if(itemId < 0 || itemId >= inventorySpace) return false;
		if(unitClass.getEquippableItemTypes() == null) return false;
		if(inventory[itemId].getEquippable() && unitClass.isEquippable(inventory[itemId])) {
			equipped = itemId;
			inventory[itemId].equipEffect();
			return true;
		}
		return false;
	}
	
	public boolean useItem(int itemId) {
		if(itemId < 0 || itemId >= inventorySpace) return false;
		if(!inventory[itemId].use(this)) removeItem(itemId);
		return true;
	}
	
	public void setStats(Map stats) {
		this.stats = stats;
	}
	
	public void setGrowthRates(Map growthRates) {
		this.growthRates = growthRates;
	}
	
	public void setUnitClass(UnitClass unitClass) {
		this.unitClass = unitClass;
	}
	
	public boolean giveItem(Item item) {
		for(int i = 0; i < inventorySpace; i++) {
			if(inventory[i] == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void removeItem(int itemId) {
		if(itemId == equipped) {
			equipped = -1;
			System.out.printf("%s broke!\n", inventory[itemId].getName());
		}
		inventory[itemId] = null;
	}
	
	public void takeDamage(boolean physicalDamage, int damage, Board board) {
		int defense = 0;
		if(physicalDamage) defense = stats.get("Def");
		else defense = stats.get("Res");
		int damageSustained = damage - defense;
		takingDamage = true;
		damageNumbers = new OnScreenText(-1, -1, Integer.toString(damageSustained), 448);
		damageNumbers.setYMov((int) (-1.75) * SpriteSheet.TILE_WIDTH);
		if(damageSustained < 0) damageSustained = 0;
		int newHP = stats.get("HP") - damageSustained;
		if(newHP < 0) newHP = 0;
		stats.replace("HP", newHP);
		if(stats.get("HP") == 0) die(board);
	}
	
	//if has died, remove from board;
	public void die(Board board) {
		System.out.printf("%s died! \n", name);
		dead = true;
		sprite.setCounter(0);
	}
	
	public void refresh() {
		canMove = true;
		active = true;
	}
	
	public void update(int clock, Board board, int row, int column) {
		//updates sprite animation or other things which need continuous updating
		if(xMov != 0) {
			int xDif = 2;
			if(xMov > 0) {
				xOffset += xDif;
				xMov -= xDif;
			}
			else {
				xOffset -= xDif;
				xMov += xDif;
			}
		}
		if(yMov != 0) {
			int yDif = 2;
			if(yMov > 0) {
				yOffset += yDif;
				yMov -= yDif;
			}
			else {
				yOffset -= yDif;
				yMov += yDif;
			}
		}
		if(sprite.getCurrentAnimation() == null) sprite.changeAnimationTo("Idle");
		if(xMov == 0 && yMov == 0) {
			if(!moving && sprite.getCurrentAnimation().contains("Walking")) sprite.changeAnimationTo("Idle");
			if(directions == null && moving) canMove = false;
			moving = false;
		}
		if(directions != null && moving == false) {
			if(board.moveUnit(row, column, directions[directionsIndex])) {
				directionsIndex++;
				if(directionsIndex >= directions.length) {
					directionsIndex = -1;
					directions = null;
				}
			}
			else {
				directionsIndex = -1;
				directions = null;
			}
		}
		sprite.update(clock);
		if(damageNumbers != null) {
			damageNumbers.update();
			if(damageNumbers.getXMov() == 0 && damageNumbers.getYMov() == 0) {
				if(damageNumbers.getCounter() >= 30) {
					damageNumbers = null;
					takingDamage = false;
					sprite.changeAnimationTo("Idle");
				}
				else if(damageNumbers.getCounter() % 10 < 5){
					damageNumbers.setTextColor(511);
				}
				else damageNumbers.setTextColor(448);
			}
		}
		if(dead) {
			if(sprite.getCounter() >= 15) {
				sprite.setShadeFactor(7.0);
				int transition = (sprite.getCounter() * 511) / 30;
				int newColor = transition * SpriteSheet.COLOR_DEPTH * SpriteSheet.COLOR_DEPTH + transition * SpriteSheet.COLOR_DEPTH + transition;
				sprite.setSwapTargetColor(newColor);
			}
			if(sprite.getCounter() >= 30) board.removeUnit(this);
		}
	}
	
	public void render(Screen screen, int scale, int x, int y) {
		sprite.setX(x + xOffset);
		sprite.setY(y + yOffset);
		boolean xMirror = false;
		if(facing % 4 == 0) xMirror = false;
		else if(facing % 4 == 1) sprite.changeAnimationTo("WalkingUp");
		else if(facing % 4 == 2) xMirror = true;
		else if(facing % 4 == 3) sprite.changeAnimationTo("WalkingDown");
		if(!active) sprite.setShadeFactor(0.5);
		if(takingDamage) sprite.changeAnimationTo("TookDamage");
		sprite.render(screen, scale, xMirror, false);
		if(damageNumbers != null) {
			if(takingDamage && damageNumbers.getCounter() == -1) {
				damageNumbers.setX((x + xOffset) * scale);
				damageNumbers.setY((y + yOffset) * scale);
				damageNumbers.setCounter(0);
			}
			damageNumbers.render(screen);
		}
	}
	
	public UnitClass getUnitClass() {
		return unitClass;
	}
	
	public int getEquipped() {
		return equipped;
	}
	
	public Item getItem(int itemId) {
		return inventory[itemId];
	}
	
	public Integer getStat(String key) {
		return stats.get(key);
	}
	
	public void setStat(String key, Integer value) {
		stats.replace(key, value);
	}
	
	public String getName() {
		return name;
	}
	
	public String getFaction() {
		return faction;
	}
	
	public void setFaction(String faction) {
		this.faction = faction;
	}
	
	public String getSide() {
		return side;
	}
	
	public void setSide(String side) {
		this.side = side;
		sprite.setColorToSwap(0);
		if(side.equals("Player")) sprite.setSwapTargetColor(PLAYER_OUTLINE);
		else if(side.equals("Enemy")) sprite.setSwapTargetColor(ENEMY_OUTLINE);
		else if(side.equals("Ally")) sprite.setSwapTargetColor(ALLY_OUTLINE);
		else if(side.equals("Neutral")) sprite.setSwapTargetColor(NEUTRAL_OUTLINE);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}
	
	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	
	public void setXMov(int xMov) {
		this.xMov = xMov;
	}
	
	public void setYMov(int yMov) {
		this.yMov = yMov;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void setFacing(int facing) {
		this.facing = facing;
	}
	
	public void setDirections(int[] directions) {
		this.directions = directions;
		if(directions != null) directionsIndex = 0;
	}
	
	public void setEquipped(int equipped) {
		this.equipped = equipped;
	}
	
	public boolean getCanMove() {
		return canMove;
	}
	
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
}
