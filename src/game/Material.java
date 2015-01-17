package game;

import java.awt.Color;

public class Material {
	protected Material(int id,Color c){
		Materials[id]=this;
		matid = id;
		color=c;
	}
	public Material setSpeed(double d){
		speed=d;
		return this;
	}
	public Material setHeight(float height){
		this.height = height;
		return this;
	}
	public static Material wGSelect(float f){
		if(f < 0.01) f=0.01f;
		float bestdist=1f;
		int bestid=0;
		for(int i=0;i<Materials.length;i++){
			if(Materials[i]!=null){
			Material mat = Materials[i];
				float dist = (f-mat.height)>0 ? (f-mat.height) : (mat.height-f);
				if(dist<bestdist){
					bestdist=dist;
					bestid=i;
				}
			}
		}
		return Materials[bestid];
	}
	public double speed = 1;
	public int matid = 0;
	public float height = 1;
	public Color color = null;
	public static final Material[] Materials = new Material[256];
	public static final Material grass = new Material(0,Color.GREEN.darker()).setHeight(0.5f);
	public static final Material dirt = new Material(1,new Color(131, 101, 57)).setHeight(0.25f);
	public static final Material water = new Material(2,new Color(235, 244, 250)).setSpeed(0.05).setHeight(0.01f);
}
