package game;

import java.awt.Rectangle;
import java.util.Stack;

public class MenuComponent {
	/**
	 * Do not call but override for custom rendering
	 */
	public void render(int parentx,int parenty){
		
	}
	/**
	 * Used for rendering the components inside this component
	 * @param parentx
	 * @param parenty
	 */
	public void renderComponents(int parentx,int parenty){
		render(parentx,parenty);
		for(int i=0;i<components.size();i++){
			MenuComponent mc = components.get(i);
			int xx=mc.boundingBox.x+parentx;
			int yy=mc.boundingBox.y+parenty;
			mc.renderComponents(xx, yy);
		}
		
	}
	/**
	 * DO NOT USE
	 * this recurses to lower components
	 * @param button
	 * @param x
	 * @param y
	 * @param clickcount
	 */
	public void clickComponents(int button, int x, int y, int clickcount){
		
		for(int i=0;i<components.size();i++){
			MenuComponent mc = components.get(i);
			if(mc.boundingBox.contains(x,y)){
				mc.click(button,x-mc.boundingBox.x,y-mc.boundingBox.y,clickcount);
			}
		}
		click(button,x,y,clickcount);
	}
	
	/**
	 * Override this for click events
	 * @param button
	 * @param x
	 * @param y
	 * @param clickcount
	 */
	public void click(int button, int x, int y, int clickcount){
		
	}
	
	/**
	 * get bounding box
	 */
	public Rectangle getBB(){return boundingBox;}
	Rectangle boundingBox;
	/**
	 * Add component to draw list and click list
	 * @param mc
	 */
	public void add(MenuComponent mc){
		components.push(mc);
	}
	/**
	 * add entire array to components
	 * @param mc
	 */
	public MenuComponent add(MenuComponent[] mcs){
		for(MenuComponent mc : mcs)
			components.push(mc);
		return this;
	}
	/**
	 * Component stack
	 */
	Stack<MenuComponent> components = new Stack<MenuComponent>();
	
}
