package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;

public class KeyboardControls implements KeyListener {

	private Map<Integer, Boolean> keys = new HashMap();
	public int up = KeyEvent.VK_W;
	public int left = KeyEvent.VK_A;
	public int down = KeyEvent.VK_S;
	public int right = KeyEvent.VK_D;
	
	public void keyPressed(KeyEvent arg0) {
		setKey(arg0, true);
	}

	public void keyReleased(KeyEvent arg0) {
		setKey(arg0, false);
	}

	public void keyTyped(KeyEvent arg0) {
		
	}
	
	private void setKey(KeyEvent arg0, boolean pressed) {
		keys.put(arg0.getKeyCode(), pressed);
	}
	
	public boolean getKey(int keyCode) {
		return keys.getOrDefault(keyCode, false);
	}
}
