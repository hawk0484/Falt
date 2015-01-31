package game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class World {
	BufferedImage map = null;
	int width = 0;
	int height = 0;
	public World(BufferedImage bi){
		map=bi;
		width=bi.getWidth();
		height=bi.getHeight();
		
	}
	public Texture getTexture(){
		try {
			return MAIN.texlder.getTexture(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static World genWorld(){
		return new World(RandomGen.convertToBufferedImage(RandomGen.GenerateHeightMap(new Dimension(1024, 1024), 10, 3)));
	}
	public static World loadWorld(File f){
		try {
			return new World(ImageIO.read(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
