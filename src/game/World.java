package game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class World {
	BufferedImage map = null;
	RandomGen rg = null;
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
			e.printStackTrace();
		}
		return null;
		
	}
	public static World genWorld(){
		RandomGen g = new RandomGen();
		World w = new World(g.GenMap(1024, 1024, 1337));
		w.rg=g;
		return w;
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
