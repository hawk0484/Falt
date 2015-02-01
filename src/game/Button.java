package game;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Stack;
import static org.lwjgl.opengl.GL11.*;
public class Button {
	public static Stack<Button> buttons = new Stack<Button>();
	Texture tex=null;
	public Button(Rectangle r,String text,int size,boolean active,ButtonStyle style){
		this.active=active;
		this.style=style;
		if(active=true){
			buttons.push(this);
		}
		try {
			tex=MAIN.texlder.getTexture(style.getButton(r.width, r.height, text, size));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void draw(){
		glBegin(GL_QUADS);
		
	}
	private ButtonStyle style=null;
	private boolean active=false;
}
