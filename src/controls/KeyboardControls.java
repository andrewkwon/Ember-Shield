package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

public class KeyboardControls implements KeyListener {

	private Map<Integer, Boolean> keys = new HashMap();
	//Tells whether or not the game has registered and handled a key press, use it
	//when you want the game to know it has already acted on a key's being pressed
	private Map<Integer, Boolean> registeredPresses = new HashMap();
	public int up = KeyEvent.VK_W;
	public int left = KeyEvent.VK_A;
	public int down = KeyEvent.VK_S;
	public int right = KeyEvent.VK_D;
	public int move = KeyEvent.VK_SHIFT;
	public int actzero = KeyEvent.VK_0;
	public int turnend = KeyEvent.VK_ENTER;
	
	public void keyPressed(KeyEvent arg0) {
		setKey(arg0, true);
	}

	public void keyReleased(KeyEvent arg0) {
		setKey(arg0, false);
		registeredPresses.put(arg0.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent arg0) {
		
	}
	
	private void setKey(KeyEvent arg0, boolean pressed) {
		keys.put(arg0.getKeyCode(), pressed);
	}
	
	public boolean getKey(int keyCode) {
		return keys.getOrDefault(keyCode, false);
	}
	
	public void registerKeyPress(int keyCode) {
		registeredPresses.put(keyCode, true);
	}
	
	public boolean keyHasRegisteredPress(int keyCode) {
		return registeredPresses.getOrDefault(keyCode, false);
	}
}
