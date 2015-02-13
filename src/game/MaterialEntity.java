package game;

import java.awt.Color;

import javax.xml.crypto.Data;

import sun.applet.Main;

public class MaterialEntity extends Material{
	boolean needsUpdates=false;
	public MaterialEntity(int id,Color c, boolean needsUpdates){
		super(id, c);
		
	}
	public void onCreate(World w,Main m,int x,int y,Object[] data){
		
	}
	public void update(World w,Main m,int x,int y,Object[] data){
		
	}
	public void onDestroyAttempt(World w,Main m,int x,int y,Object[] data,int damage){
		
	}
}
