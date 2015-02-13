package game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GenOre implements Gen{

	public BufferedImage canGen(int x, int y,BufferedImage map,Material genMaterial,RandomGen rg) {
		Random r = new Random((int)(rg.Noise1(x, y)*1000000));
		float f = r.nextFloat();
		if(f<chance){
			for(int i=0;i<amount;i++){
				double dir = Math.toRadians(r.nextFloat()*360);
				int xdir=(int) (Math.sin(dir)*diverse);
				int ydir=(int) (Math.cos(dir)*diverse);
				xdir=xdir==0?1:xdir;
				ydir=ydir==0?1:ydir;
				int xx = x+(r.nextInt(xdir>0?xdir:-xdir)*(xdir>0?1:-1));
				int yy = y+(r.nextInt(ydir>0?ydir:-ydir)*(ydir>0?1:-1));
				System.out.println(xdir);
				System.out.println(ydir);
				try{
					int id=new Color(map.getRGB(xx, yy),true).getAlpha();
					boolean up = id>=128;
					
					Material m = Material.Materials[id-(up?128:0)];
					
					if(m.height>=0.5f){
						map.setRGB(xx,yy,new Color(128,128,128,genMaterial.matid+(up?128:0)).getRGB());
					}
				}catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
			
		}
		return map;
	}
	float chance=0;
	int amount=0;
	int diverse=0;
	public GenOre(float chance,int amount,int diversity){
		this.chance=chance/1024;
		this.amount=amount;
		diverse=diversity*2;
	}
}
