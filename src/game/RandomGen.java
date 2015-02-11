package game;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Stack;

public class RandomGen {
	int seed=0;
	public float Noise1(float x,float y){
		
		Random r = new Random((long) (y*2000+x+10000*(((float)seed)/192837)));
		r.setSeed(r.nextLong());
		float f = r.nextFloat()*2-1;
		
		return f;
	}
	private float SmoothedNoise(float x, float y){
		float corners = (Noise1(x-1,y-1)+Noise1(x+1,y-1)+Noise1(x-1,y+1)+Noise1(x+1,y+1))/16;
		float sides = (Noise1(x-1,y)+Noise1(x+1,y)+Noise1(x,y-1)+Noise1(x,y+1))/8;
		float center = Noise1(x,y)/4;
		
		return corners+sides+center;
	}
	private float Linear_Interpolation(float a, float b, float x){
		return a*(1-x)+b*x;
	}
	private float Cosine_Interpolation(float a, float b, float x){
		float ft = x*3.1415927f;
		float f = (float) ((1-Math.cos(ft)) * 0.5);
		return a*(1-f) + b*f;
	}
	private float InterpolatedNoise(float x, float y){
		int int_x=(int)x;
		float fractional_x = x-(float)int_x;
		int int_y=(int)y;
		float fractional_y = y-(float)int_y;
		
		float v1=SmoothedNoise(int_x,int_y);
		float v2=SmoothedNoise(int_x+1,int_y);
		float v3=SmoothedNoise(int_x,int_y+1);
		float v4=SmoothedNoise(int_x+1,int_y+1);
		
		float i1=Cosine_Interpolation(v1, v2, fractional_x);
		float i2=Cosine_Interpolation(v3, v4, fractional_x);
		return Cosine_Interpolation(i1,i2,fractional_y);
	}
	
	private float PerlinNoise(float x,float y,float persistance,int octaves){
		float total = 0;
		for(int i=0;i<octaves;i++){
			float freq = (float) pow(2,i);
			float amp = (float) pow(persistance,i);
			total+=InterpolatedNoise(x*freq, y*freq)*amp;
		}
		return total/octaves;
	}
	public BufferedImage GenMap(int width,int height,int seed){
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		this.seed=seed;
		int lastr=0;
		Stack<Material> genMats = new Stack<Material>();
		for(Material m:Material.Materials){
			if(m!=null)
				if(m.hasGen)
					genMats.push(m);
			
		}
		for(float y=0;y<height;y++){
			for(float x=0;x<width;x++){
				
				float h=PerlinNoise(x/64,y/64,0.30f,5);
				int nh = (int) (((h+1)/2)*255);
				int nh2 = 128+new Random((long) (Noise1(x,y)*10000)).nextInt(10);
				bi.setRGB((int)x,(int) y, new Color(nh2,nh2,nh2,Material.wGSelect((float)nh/255).matid).getRGB());
				
			}
			int thisr=(int)((y/height)*100);
			if(thisr!=lastr){
				System.out.println("Progress : "+thisr+"%");
				lastr=thisr;
			}
			
		}
		
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++)
				for(int i=0;i<genMats.size();i++){
					Material m = genMats.get(i);
					bi=m.getGen().canGen(x, y, bi,m,this);
				}
			int thisr=(int)((y/height)*100);
			if(thisr!=lastr){
				System.out.println("Progress : "+thisr+"%");
				lastr=thisr;
			}
			
		}
		return bi;
		
	}
}
