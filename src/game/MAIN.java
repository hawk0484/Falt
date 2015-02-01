package game;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class MAIN{
	static Menu ActiveMenu = null;
	static Stack<Entity> Entities = new Stack<Entity>();
	static int CamWidth = 1280, CamHeight = 800;
	static float CamX = 0, CamY = 0, CamXVel=0, CamYVel=0, CamXMax=500, CamYMax=500,Scale=1, ScaleVel=0;
	public static String GameState = "Menu";
	public static World world = null;
	private static float[][] lastworld = null;
	public static TextureLoader texlder;
	public static Stack<Point> blockUpdates = new Stack<Point>();
	public MAIN(){
		texlder=new TextureLoader();
		GameState="Play";
		world=World.genWorld();
		setupLastWorld();
		updateEntireMap();
		ActiveMenu=new MenuMain();
		try {
			Controls.loadControls(new File("falt.cfg"));
		} catch (IOException e) {
			Controls.setDefaultControls();
			try {
				Controls.saveControls(new File("falt.cfg"));
			} catch (FileNotFoundException e1) {
				System.err.println("well, shit");
			}
		}
		try{
			//display modes
			Display.setDisplayMode(new DisplayMode(1280,800));
			Display.setTitle("Falt");
			Display.create();
			Display.setVSyncEnabled(true);
			Mouse.create();
			Keyboard.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
	    
		
		getDelta();
		lastFPS=getTime();
		//glTranslatef(0,0,-5);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		while (!Display.isCloseRequested()) {
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			//Camera
			glOrtho(CamX+CamWidth/2-(CamWidth/2)*Scale, CamX+CamWidth/2+(CamWidth/2)*Scale,CamY+CamHeight/2+(CamHeight/2)*Scale, CamY+CamHeight/2-(CamHeight/2)*Scale, 1, 0);
			glMatrixMode(GL_TEXTURE);
			
			glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );	
			glClearColor(0f,0f,0f,1.0f);
			update();
		    render();
		    updateFPS();
		    Display.update();
		}
		Display.destroy();
		Keyboard.destroy();
	}
	
	private int dif=0;
	/**
	 * DO NOT CALL
	 */
	private void render(){
		Display.sync(60);
		if(GameState=="Play"||GameState.startsWith("IG")){
			
			glPushMatrix();
			
			
			
			world.getTexture().bind();
			BufferedImage map = world.map;
			Point p;
			try{ //loop block updates
				while((p = blockUpdates.pop())!=null){
					updateBlock(p.x, p.y, map);
				}
			}catch(EmptyStackException e){
				
			}
				//draw map on screen
				glBegin(GL_QUADS);
				glTexCoord2f(0,0);
				glVertex2f(0, 0);
				glTexCoord2f(perc22(world.width),0);
				glVertex2f(world.width, 0);
				glTexCoord2f(perc22(world.width),perc22(world.height));
				glVertex2f(world.width, world.height);
				glTexCoord2f(0,perc22(world.height));
				glVertex2f(0, world.height);
			
			
			glEnd();
			for(Entity ent : Entities){
				glPushMatrix();
				ent.render();
				glPopMatrix();
			}
			
			glPopMatrix();
			glFlush();
			
		}
		if(GameState.startsWith("IG")||GameState.startsWith("Menu")){
			ActiveMenu.render();
		}
	}
	/**
	 * Use this function to scale image correctly
	 * @param f size
	 */
	public static float perc22(int f){ 
		int i=1;
		while(f>i)i*=2;
		return (f/((float)i));
	}
	/**
	 * Switch active menu
	 * @param m
	 */
	public void selectMenu(Menu m){
		ActiveMenu=m;
		GameState=m.getState();
	}
	/**
	 * add location to update list
	 * @param x of block
	 * @param y of block
	 */
	public void scheduleBlockUpdate(int x,int y){ 
		blockUpdates.push(new Point(x,y));
		
	}
	/**
	 * add every tile to update list
	 * @warning VERY SLOW
	 */
	public void updateEntireMap(){ 
		for(int y=0;y<world.height;y++)
			for(int x=0;x<world.width;x++){
				scheduleBlockUpdate(x, y);
			}
	}
	/**
	 * Initialises the lastworld variable
	 * @note is useless beyond MAIN constructor
	 */
	public void setupLastWorld(){
		lastworld=new float[world.width][world.height];
		for(int y=0;y<world.height;y++)
			for(int x=0;x<world.width;x++){
				lastworld[x][y]=-1;
			}
		
	}
	/**
	 * @warning DO NOT USE, ONLY USE {@link MAIN.scheduleBlockUpdate}
	 * @param x
	 * @param y
	 * @param map
	 */
	private void updateBlock(int x,int y,BufferedImage map){
		BufferedImage pixel = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		Color c = new Color(map.getRGB(x, y),true);
		int r=c.getRed(),g=c.getGreen(),b=c.getBlue(),a=c.getAlpha();
		
		if(a!=lastworld[x] [y]){
			boolean raised=a>=128;
			
			
			Color mc = Material.Materials[a-(128*(raised? 1:0))].color;
			r=(int) (((float)mc.getRed())*((float)c.getRed()/128f));
			g=(int) (((float)mc.getGreen())*((float)c.getGreen()/128f));
			b=(int) (((float)mc.getBlue())*((float)c.getBlue()/128f));
			if(r<0) r=0;
			if(g<0) g=0;
			if(b<0) b=0;
			int col=new Color(r,g,b,255).getRGB();
			pixel.setRGB(0, 0, col);
			glTexSubImage2D(GL_TEXTURE_2D,0,x,y,1,1,GL_RGB,GL_UNSIGNED_BYTE,convertImageData(pixel));
			lastworld[x][y]=a;
		}
	}
	Stack<Character> keys = new Stack<Character>();
	Stack<Integer> keycodes = new Stack<Integer>();
	/**
	 * @warning DO NOT CALL
	 */
	public void update(){
		if(GameState.startsWith("IG")||GameState.startsWith("Menu")){
			ActiveMenu.update();
		}
		float Mult=1f;
		if(GameState=="Play"){
			while(Keyboard.next()){
				char key = Keyboard.getEventCharacter();
				int keycode = Keyboard.getEventKey();
				boolean down=Keyboard.getEventKeyState();
				if(down){
					if(!keycodes.contains(keycode)){
						keys.push(Character.toLowerCase(key));
						keycodes.push(Character.toLowerCase(keycode));
					}
				}else{
					if(keycodes.contains(keycode)){
						keys.remove((int)keycodes.indexOf(Character.toLowerCase(keycode)));
						keycodes.remove(keycodes.indexOf(Character.toLowerCase(keycode)));
					}
				}
				
			}
			if(keys.contains(Controls.getControl("Camera North").keychar)){
				CamYVel-=calc(Mult*Scale);
			}if(keys.contains(Controls.getControl("Camera South").keychar)){
				CamYVel+=calc(Mult*Scale);
			}if(keys.contains(Controls.getControl("Camera East").keychar)){
				CamXVel+=calc(Mult*Scale);
			}if(keys.contains(Controls.getControl("Camera West").keychar)){
				CamXVel-=calc(Mult*Scale);
			}
			int wheel = Mouse.getDWheel();
			if(wheel>0){
				ScaleVel-=(Scale)/120;
			}else if(wheel<0){
				ScaleVel+=(Scale)/120;
			}
		}
		
		
		CamX+=CamXVel;
		CamY+=CamYVel;
		CamXVel*=0.9;
		CamYVel*=0.9;
		ScaleVel*=0.9;
		Scale+=ScaleVel;
	}
	/**
	 * @warning IGNORE
	 * @param mult
	 */
	public float calc(float mult){
		return (float) (mult);
	}
	/**
	 * main method, ignore
	 * @param args
	 */
	public static void main(String[] args){
		new MAIN();
	}
	int fps;
	long lastFPS;
	long lastFrame;
	public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
	public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
	public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
      
        return delta;
    }
	private ByteBuffer convertImageData(BufferedImage bufferedImage) {
	    ByteBuffer imageBuffer;
	    WritableRaster raster;
	    BufferedImage texImage;

	    ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
	            .getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
	            true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

	    raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
	            bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
	    texImage = new BufferedImage(glAlphaColorModel, raster, true,
	            new Hashtable());

	    // copy the source image into the produced image
	    Graphics g = texImage.getGraphics();
	    g.setColor(new Color(0,0,0));
	    g.fillRect(0, 0, 256, 256);
	    g.drawImage(bufferedImage, 0, 0, null);

	    // build a byte buffer from the temporary image
	    // that be used by OpenGL to produce a texture.
	    byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
	            .getData();

	    imageBuffer = ByteBuffer.allocateDirect(data.length);
	    imageBuffer.order(ByteOrder.nativeOrder());
	    imageBuffer.put(data, 0, data.length);
	    imageBuffer.flip();

	    return imageBuffer;
	}
}
