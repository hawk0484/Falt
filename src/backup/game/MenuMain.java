package game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

public class MenuMain implements Menu {
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
		
		//draw code
		
		Display.update();
		
	}
	public void click(int button, int x, int y, int clickcount) {
		
	}
	public String getState() {
		return "MenuMain";
	}
	public void update() {
		
	}
	
}
