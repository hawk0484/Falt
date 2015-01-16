package game;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
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
	static float CamX = 0, CamY = 0, CamXVel=0, CamYVel=0, CamXMax=500, CamYMax=500,Scale=1;
	public static String GameState = "Menu";
	public static World world = null;
	private static BufferedImage lastworld = null;
	public static TextureLoader texlder;
	public MAIN(){
		texlder=new TextureLoader();
		GameState="Play";
		world=World.loadWorld(new File("test world.png"));
		updateLastWorld(world.map);
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
		
		glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
		glOrtho(0, CamWidth,CamHeight, 0, 0, 1);
		glMatrixMode(GL_TEXTURE);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		getDelta();
		lastFPS=getTime();
		//glTranslatef(0,0,-5);
		ByteBuffer dataarray = convertImageData(world.map);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		while (!Display.isCloseRequested()) {
			glClearColor(0.2f,0.2f,0.2f,1.0f);
			update();
		    render();
		    updateFPS();
		    Display.update();
		}
		Display.destroy();
		Keyboard.destroy();
	}
	private int dif=0;
	public void render(){
		Display.sync(60);
		if(GameState=="Play"||GameState.startsWith("IG")){
			float OffsetX=-CamX+CamWidth/2;
			float OffsetY=-CamY+CamHeight/2;
			world.getTexture().bind();
			glPushMatrix();
			glScalef(Scale, Scale, Scale);
			glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(OffsetX, OffsetY);
			glTexCoord2f(1,0);
			glVertex2f(OffsetX+CamWidth, OffsetY);
			glTexCoord2f(1,1);
			glVertex2f(OffsetX+CamWidth, OffsetY+CamHeight);
			glTexCoord2f(0,1);
			glVertex2f(OffsetX, OffsetY+CamHeight);
			
			glEnd();
			if(dif>5) dif=0;
			dif++;
			BufferedImage map = world.map;
			for(int y=dif;y<world.height;y+=5){
				for(int x=dif;x<world.width;x+=5){
					if(map.getRGB(x, y)!=lastworld.getRGB(x, y)){
						BufferedImage pixel = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
						pixel.setRGB(x, y, map.getRGB(x, y));
						glTexSubImage2D(GL_TEXTURE_2D,0,x,y,1,1,GL_RGB,GL_UNSIGNED_BYTE,convertImageData(pixel));
					}
				}
			}
			
			for(Entity ent : Entities){
				glPushMatrix();
				ent.render();
				glPopMatrix();
			}
			updateLastWorld(map);
			
			glPopMatrix();
			glFlush();
			
		}
		if(GameState.startsWith("IG")||GameState.startsWith("Menu")){
			ActiveMenu.render();
		}
	}
	private void updateLastWorld(BufferedImage map){
		lastworld=map.getSubimage(0, 0, map.getWidth(), map.getHeight());
	}
	public void selectMenu(Menu m){
		ActiveMenu=m;
		GameState=m.getState();
	}
	Stack<Character> keys = new Stack<Character>();
	Stack<Integer> keycodes = new Stack<Integer>();
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
				Scale*=0.99;
			}else if(wheel<0){
				Scale*=1.01;
			}
			System.out.println(Scale);
		}
		
		
		CamX+=CamXVel;
		CamY+=CamYVel;
		CamXVel*=0.9;
		CamYVel*=0.9;
	}
	public float calc(float mult){
		return (float) (mult);
	}
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
