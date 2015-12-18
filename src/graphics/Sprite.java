package graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Sprite {

	private String spriteSheetPath;
	//animations maps animation name to a mapping of times to switch frames(not durations) to frame
	//animations.get(currentAnimation).get(-1) should return length of animation
	private Map<String, Map<Integer, Integer>> animations;
	private int animationLength;
	private String currentAnimation;
	private int currentFrame = 0;
	private int animationStartTime = -1;
	private int x;
	private int y;
	
	public Sprite(String spriteSheetPath, int x, int y) {
		this.spriteSheetPath = spriteSheetPath;
		this.x = x;
		this.y = y;
		animations = new HashMap<String, Map<Integer, Integer>>();
		load();
	}
	
	//TODO: fill with actual loading code
	public void load() {
		spriteSheetPath = "/Untitled.png";
		Map<Integer, Integer> f = new HashMap<Integer, Integer>();
		f.put(-1, 40);
		f.put(0, 27);
		f.put(5, 33);
		f.put(10, 27);
		f.put(15, 33);
		f.put(20, 27);
		f.put(25, 33);
		f.put(30, 27);
		f.put(35, 33);
		animations.put("Idle", f);
	}
	
	public void update(int clock) {
		if(animationStartTime == -1) changeAnimationTo("Idle", clock);
		int animationTime = (clock - animationStartTime) % animationLength;
		Set<Integer> times = animations.get(currentAnimation).keySet();
		//time for the frame we are on
		int frameKey = 0;
		for(int key : times) {
			if(key == -1);
			else if(animationTime >= key) {
				frameKey = key;
			}
			else break;
		}
		currentFrame = animations.get(currentAnimation).get(frameKey);
	}
	
	public void render(Screen screen, int scale) {
		screen.setSheet(spriteSheetPath);
		screen.render(x, y, currentFrame, scale);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void changeAnimationTo(String newAnimation, int clock) {
		currentAnimation = newAnimation;
		animationStartTime = clock;
		animationLength = animations.get(currentAnimation).get(-1);
	}
}
