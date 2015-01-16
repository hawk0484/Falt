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
		glPushMatrix();
		texture.bind();
		glTranslatef(x, y, 0);
		glRotatef(rotation, 0, 0, 0);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(MAIN.perc22(texture.getImageWidth()), 0);
		glVertex2f(MAIN.perc22(texture.getImageWidth()), 0);
		glTexCoord2f(1, 1);
		glVertex2f(MAIN.perc22(texture.getImageWidth()), MAIN.perc22(texture.getImageHeight()));
		glTexCoord2f(0, 1);
		glVertex2f(0, MAIN.perc22(texture.getImageHeight()));
		glEnd();
		glPopMatrix();
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
	public float rotation=0;
	public static final Entity[] Entities = new Entity[256];
	public static final Entity dirt = new Entity(0,"BasicTest.png");
}
