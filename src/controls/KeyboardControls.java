package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardControls implements KeyListener{

	public boolean upPressed = false;
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean downPressed = false;
	
	public void keyPressed(KeyEvent arg0) {
		setKey(arg0, true);
	}

	public void keyReleased(KeyEvent arg0) {
		setKey(arg0, false);
	}

	public void keyTyped(KeyEvent arg0) {
		
	}
	
	public void setKey(KeyEvent arg0, boolean pressed) {
		if(arg0.getKeyCode() == KeyEvent.VK_UP) upPressed = pressed;
		else if(arg0.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = pressed;
		else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = pressed;
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) downPressed = pressed;
	}
}
