package game;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Stack;

public class Menu extends MenuComponent{
	private String state="";
	private Texture tex = null;
	public Menu(String state,ComponentStyle style,Rectangle bb){
		this.state=state;
		this.style=style;
		boundingBox=bb;
		try {
			tex=MAIN.texlder.getTexture(style.getComponentImage(bb.width, bb.height));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void render(int parentx, int parenty){
		MAIN.drawImage(tex, parentx, parenty);
	}
	/**
	 * used for pausing the game or completely stop rendering
	 * @return the state for the game to be in when this menu appears
	 * At the beginning of the name start with either 
	 * IG : for it to not pause the game but disable controls
	 * Menu : for it to not run the game or do normal rendering
	 * Play : for it to ignore this menu is there(used for HUD)
	 */
	public String getState(){
		return state;
	}
	protected ComponentStyle style = null;
	public static final Menu mainMenu = (Menu) new Menu("Menu Main Menu",ComponentStyle.highTechStyle,new Rectangle(0,0,500,900)).add(new MenuComponent[]{
		new MenuButton(new Rectangle(400,400,100,30),"Play",40,true,ComponentStyle.test)
	});
}
