package game;

import java.awt.Color;

public class Material {
	protected Material(int id,Color c){
		Materials[id]=this;
		matid = id;
		color=c;
	}
	/**
	 * set walking speed
	 * @param d
	 * @return
	 */
	protected Material setSpeed(double d){
		speed=d;
		return this;
	}
	/**
	 * set height of block to generate at (use negative value for nongen)
	 * @param height
	 * @return
	 */
	protected Material setHeight(float height){
		this.height = height;
		return this;
	}
	protected Material setGen(Gen g){
		generator=g;
		hasGen=true;
		return this;
	}
	public Gen getGen(){
		return generator;
	}
	/**
	 * get closest material
	 * @param f
	 * @return
	 */
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
	private Gen generator = null;
	public boolean hasGen=false;
	public double speed = 1;
	public int matid = 0;
	public float height = 0;
	public Color color = null;
	public static final Material[] Materials = new Material[256];
	public static final Material grass = new Material(0,Color.GREEN.darker()).setHeight(0.5f);
	public static final Material water = new Material(2,Color.BLUE).setSpeed(0.05).setHeight(0.45f);
	public static final Material deepwater = new Material(3,Color.BLUE.darker()).setSpeed(0.01).setHeight(0.01f);
	public static final Material stone = new Material(4,Color.GRAY).setSpeed(1).setHeight(0.54f);
	public static final Material coaldeposit = new Material(5,Color.BLACK).setSpeed(0.9f).setGen(new GenOre(0.02f,30,3));
}
