package game;

import java.awt.image.BufferedImage;

public interface Gen {
	public BufferedImage canGen(int x,int y,BufferedImage bi,Material genMaterial,RandomGen rg);
}
