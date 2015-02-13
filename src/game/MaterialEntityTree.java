package game;

import java.awt.Color;

import sun.applet.Main;

public class MaterialEntityTree extends MaterialEntity {

	public MaterialEntityTree(int id, Color c,boolean needsUpdates) {
		super(id, c,needsUpdates);
	}
	@Override
	public void onCreate(World w, Main m, int x, int y, Object[] data) {
		super.onCreate(w, m, x, y, data);
		data = new Object[1];
		data[1]=10;
	}
	@Override
	public void onDestroyAttempt(World w, Main m, int x, int y, Object[] data,
			int damage) {
		super.onDestroyAttempt(w, m, x, y, data, damage);
		int hp=(int)((Integer)data[1])-damage;
		if(hp<=0){
			MAIN.setMaterial(x, y, Material.grass.matid);
		}
		
	}
}
