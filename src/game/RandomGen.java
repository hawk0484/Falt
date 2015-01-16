package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomGen {
	public static float[][] GenerateHeightMap(Dimension dim, long seed,int smooth){
		Random Rand = new Random(seed);
		float[][] out = new float[(int) dim.getWidth()][(int) dim.getHeight()];
		for(int y=0;y<dim.height;y++){
			for(int x=0;x<dim.height;x++){
				out[x][y]=Rand.nextFloat();
			}
		}
		for(int i=0;i<smooth;i++){
			out=Smooth(out);
		}
		return out;
	}
	public static BufferedImage convertToBufferedImage(float[][] in){
		BufferedImage map = new BufferedImage(in.length,in[0].length,BufferedImage.TYPE_INT_ARGB);
		
		for(int y=0;y<in[0].length;y++){
			for(int x=0;x<in.length;x++){
				float h=in[x][y];
				Material m = Material.wGSelect(h);
				map.setRGB(x, y, new Color(h,h,h,m.matid+m.height>0.5?128:0).getRGB());
				
			}
		}
		return map;
	}
	private static float[][] Smooth(float[][] in){
		for(int y=0;y<in[0].length;y++){
			for(int x=0;x<in.length;x++){
				in[x][y]=(get(in,x,y)+get(in,x+1,y)+get(in,x-1,y)+get(in,x,y+1)+get(in,x,y-1)+get(in,x+1,y-1)+get(in,x-1,y-1)+get(in,x-1,y+1)+get(in,x+1,y+1))/9;
			}
		}
		return in;
	}
	private static float get(float[][] in, int x, int y){
		try{
			return in[x][y];
		}catch(ArrayIndexOutOfBoundsException e){
			return 0.0f;
		}
	}
}
