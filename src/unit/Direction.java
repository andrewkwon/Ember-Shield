package unit;

//contains methods which handle directions
//direction: 0=right, 1=up, 2=left, 3=down
public class Direction {

	public static int getXDir(int direction) {
		int xDir = 0;
		if(direction % 4 == 0) xDir = 1;
		else if(direction % 4 == 2) xDir = -1;
		return xDir;
	}
	
	public static int getYDir(int direction) {
		int yDir = 0;
		if(direction % 4 == 1) yDir = -1;
		else if(direction % 4 == 3) yDir = 1;
		return yDir;
	}
	
	public static int getDirection(int x, int y) {
		int direction = -1;
		if(x == 1 && y == 0) direction = 0;
		else if(x == 0 && y == -1) direction = 1;
		else if(x == -1 && y == 0) direction = 2;
		else if(x == 0 && y == 1) direction = 3;
		return direction;
	}
}
