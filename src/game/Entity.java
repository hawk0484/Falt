package game;

import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.lwjgl.opengl.GL11.*;

public class Entity{
	protected Entity(int id,String textureloc){
		Entities[id]=this;
		try {
			texture=MAIN.texlder.getTexture(textureloc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private Entity(){
		
	}
	public void render(){
		texture.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x, y);
		glTexCoord2f(1,0);
		glVertex2f(x+texture.getImageWidth(), y);
		glTexCoord2f(1,1);
		glVertex2f(x+texture.getImageWidth(), y+texture.getImageHeight());
		glTexCoord2f(0,1);
		glVertex2f(x, y+texture.getImageHeight());
		glEnd();
	}
	public Entity clone(){
		Entity newent = new Entity();
		for(Field f : getClass().getDeclaredFields()){
			try {
				if(!Modifier.isFinal(f.getModifiers())){
					Field newf = newent.getClass().getField(f.getName());
					newf.set(newent, f.get(f));
				}
				
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return newent;
	}
	public Texture texture;
	public int x=0,y=0;
	public static final Entity[] Entities = new Entity[256];
	public static final Entity dirt = new Entity(0,"BasicTest.png");
}
