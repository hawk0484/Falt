package game;

public class Material {
	protected Material(int id){
		Materials[id]=this;
	}
	public Material setSpeed(double d){
		speed=d;
		return this;
	}
	public double speed = 1;
	public static final Material[] Materials = new Material[256];
	public static final Material grass = new Material(0);
	public static final Material dirt = new Material(1);
	
}
