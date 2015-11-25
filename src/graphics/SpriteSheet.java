package graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;

public class SpriteSheet {

	private String path;
	private int[] pixels;
	private int width;
	private int height;
	public static final int TILE_WIDTH = 32;
	public static final int COLOR_DEPTH = 8;
	
	public SpriteSheet(String path) {
		BufferedImage image = null;
		try{
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		}
		catch(Exception ex){ 
			ex.printStackTrace();
		}
		if(image == null) return;
		this.path = path;
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		for(int i = 0; i < pixels.length; i++) {
			int r = (int) (((pixels[i] >> 16) & 0xFF) / (256 / COLOR_DEPTH));
			int g = (int) (((pixels[i] >> 8) & 0xFF) / (256 / COLOR_DEPTH));
			int b = (int) (((pixels[i]) & 0xFF) / (256 / COLOR_DEPTH));
			pixels[i] = (int) r * COLOR_DEPTH * COLOR_DEPTH + g * COLOR_DEPTH + b;
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
