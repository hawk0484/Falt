package game;

import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;

public class Entity {
	protected Entity(int id,String textureloc){
		Entitys[id]=this;
		try {
			texture=MAIN.texlder.getTexture(textureloc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void render(){
		
	}
	public Texture texture;
	public static final Entity[] Entitys = new Entity[256];
}
