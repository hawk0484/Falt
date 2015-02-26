package game;

import java.awt.Color;

public class MaterialEntity extends Material{
	boolean needsUpdates=false;
	public MaterialEntity(int id,Color c, boolean needsUpdates){
		super(id, c);
		
	}
	public void onCreate(World w, MAIN m,int x,int y,Object[] data){
		
	}
	public void update(World w,MAIN m,int x,int y,Object[] data){
		
	}
	public void onDestroyAttempt(World w,MAIN m,int x,int y,Object[] data,int damage){
		
	}
}
