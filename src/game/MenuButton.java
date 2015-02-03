package game;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Stack;
import static org.lwjgl.opengl.GL11.*;
public class MenuButton extends MenuComponent{
	public static Stack<MenuButton> buttons = new Stack<MenuButton>();
	Texture tex=null;
	/**
	 * Default constructor for buttons, call from menu class in constructor to use.
	 * @param r
	 * @param text
	 * @param size
	 * @param active
	 * @param style
	 */
	public MenuButton(Rectangle r,String text,int size,boolean active,ComponentStyle style){
		this.active=active;
		this.style=style;
		boundingBox=r;
		try {
			tex=MAIN.texlder.getTexture(style.getButtonImage(r.width, r.height, text, size));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void render(int xx,int yy){
		MAIN.drawImage(tex, xx, yy);
	}
	/**
	 * sets internal variable to true
	 */
	public void activate(){
		active=true;
	}
	/**
	 * sets internal variable to false
	 */
	public void deactivate(){
		active=false;
	}
	private ComponentStyle style=null;
	private boolean active=false;
}
