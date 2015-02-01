package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;

public class RandomGen {
	static Random Rand = null;
	public static float[][] GenerateHeightMap(Dimension dim, long seed,int smooth){
		Rand = new Random(seed);
		float[][] out = new float[(int) dim.getWidth()][(int) dim.getHeight()];
		for(int y=0;y<dim.height;y++){
			for(int x=0;x<dim.height;x++){
				out[x][y]=Rand.nextFloat()>0.5f? 1:0;
			}
		}
		
		
		for(int i=0;i<smooth;i++){
			out=Smooth(out);
		}
		corrupt(out,2000);
		for(int i=0;i<smooth/10;i++){
			out=Smooth(out);
		}
		return out;
	}
	public static float[][] set(float[][] in,int x,int y,float val){
		if(x<in.length&&x>0&&y<in[0].length&&y>0){
			in[x][y]=val;
		}
		
		return in;
	}
	public static float[][] corrupt(float[][] in,int num){
		for(int i=0;i<num;i++){
			in[Rand.nextInt(in.length)][Rand.nextInt(in[0].length)]=Rand.nextFloat()>0.5f? 1:0;
		}
		return in;
	}
	public static BufferedImage convertToBufferedImage(float[][] in){
		BufferedImage map = new BufferedImage(in.length,in[0].length,BufferedImage.TYPE_INT_ARGB);
		
		for(int y=0;y<in[0].length;y++){
			for(int x=0;x<in.length;x++){
				float h=in[x][y];
				Material m = Material.wGSelect(h);
				map.setRGB(x, y, 
						new Color((int)(h*255),(int)(h*255),(int)(h*255),(int)(m.matid+(m.height>0.5f?128:0))).getRGB());
			}
		}
		return map;
	}
	private static float[][] Smooth(float[][] in){
		for(int y=0;y<in[0].length;y++){
			for(int x=0;x<in.length;x++){
				in[x][y]=(get(in,x+1,y)+get(in,x-1,y)+get(in,x,y+1)+get(in,x,y-1))/4;
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
