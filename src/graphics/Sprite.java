package graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class Sprite {

	private String spriteSheetPath;
	//motions txt file hold frame change times, frame indexes, animation duration
	private String motionsFilePath;
	private int motionsStartLine;
	//animations maps animation name to a mapping of times to switch frames(not durations) to frame
	//animations.get(currentAnimation).get(-1) should return length of animation
	private Map<String, Map<Integer, Integer>> animations;
	private int animationLength;
	private String currentAnimation;
	private int currentFrame = 0;
	private int x;
	private int y;
	private int colorToSwap = Screen.NONCOLOR;
	private int swapTargetColor = Screen.NONCOLOR;
	private double shadeFactor = 1.0;
	private int counter = -1;
	
	public Sprite(String spriteSheetPath, String motionsFilePath, int motionsStartLine) {
		this.spriteSheetPath = spriteSheetPath;
		this.motionsFilePath = motionsFilePath;
		this.motionsStartLine = motionsStartLine;
		animations = new HashMap<String, Map<Integer, Integer>>();
		load();
	}
	
	public void load() {
		Scanner scanner = null;
		try{
			scanner = new Scanner(Sprite.class.getResourceAsStream(motionsFilePath));
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		//sets scanner to correct line
		for(int i = 1; i < motionsStartLine; i++) scanner.nextLine();
		String lineStart = scanner.next();
		while(!lineStart.equals("End")) {
			Map<Integer, Integer> anim = new HashMap<Integer, Integer>();
			String nextDataPiece = scanner.next();
			while(!nextDataPiece.equals("End")) {
				int timeChange = Integer.parseInt(nextDataPiece);
				int frame = Integer.parseInt(scanner.next());
				anim.put(timeChange, frame);
				nextDataPiece = scanner.next();
			}
			animations.put(lineStart, anim);
			lineStart = scanner.next();
		}
		scanner.close();
	}
	
	public void update(int clock) {
		int animationTime = clock % animationLength;
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
		if(counter != -1) counter++;
	}
	
	public void render(Screen screen, int scale, boolean mirrorX, boolean mirrorY) {
		screen.setSheet(spriteSheetPath);
		screen.render(x, y, currentFrame, scale, colorToSwap, swapTargetColor, mirrorX, mirrorY, shadeFactor);
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
	
	public void changeAnimationTo(String newAnimation) {
		currentAnimation = newAnimation;
		animationLength = animations.get(currentAnimation).get(-1);
	}
	
	public String getCurrentAnimation() {
		return currentAnimation;
	}

	public void setColorToSwap(int colorToSwap) {
		this.colorToSwap = colorToSwap;
	}
	
	public void setSwapTargetColor(int swapTargetColor) {
		this.swapTargetColor = swapTargetColor;
	}
	
	public void setShadeFactor(double shadeFactor) {
		this.shadeFactor = shadeFactor;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public void setCounter(int counter) {
		this.counter = counter;
	}
}
