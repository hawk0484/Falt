package game;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ComponentStyle {
	BufferedImage leftside=null,rightside=null,bottomside=null,topside=null,topright=null,topleft=null,bottomright=null,bottomleft=null,center=null;
	private Font font=null;
	int bt=0;
	public ComponentStyle(File f, int bThick,String font){
		BufferedImage bi = null;
		bt=bThick;
		this.font=new Font(font,Font.PLAIN,0);
		try {
			bi = ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leftside=bi.getSubimage(0, bThick, bThick, bi.getHeight()-bThick*2);
		rightside=bi.getSubimage(bi.getWidth()-bThick, bThick, bThick, bi.getHeight()-bThick*2);
		topside=bi.getSubimage(bThick,0, bi.getWidth()-bThick*2, bThick);
		bottomside=bi.getSubimage(bThick,bi.getHeight()-bThick, bi.getWidth()-bThick*2, bThick);
		topleft=bi.getSubimage(0, 0, bThick, bThick);
		topright=bi.getSubimage(bi.getWidth()-bThick, 0, bThick, bThick);
		bottomleft=bi.getSubimage(0, bi.getHeight()-bThick, bThick, bThick);
		bottomright=bi.getSubimage(bi.getWidth()-bThick, bi.getHeight()-bThick, bThick, bThick);
		center = bi.getSubimage(bThick,bThick,bi.getWidth()-bThick*2,bi.getHeight()-bThick*2);
	}
	/**
	 * return image of the style at specified size
	 * @param width
	 * @param height
	 * @return
	 */
	public BufferedImage getComponentImage(int width,int height){
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		
		g.drawImage(topleft,0,0,bt,bt,null);
		g.drawImage(topright,width-bt,0,bt,bt,null);
		g.drawImage(bottomleft,0,height-bt,bt,bt,null);
		g.drawImage(bottomright,width-bt,height-bt,bt,bt,null);
		g.drawImage(topside,bt,0,width-bt*2,bt,null);
		g.drawImage(bottomside,bt,height-bt,width-bt*2,bt,null);
		g.drawImage(leftside,0,bt,bt,height-bt*2,null);
		g.drawImage(rightside,width-bt,bt,bt,height-bt*2,null);
		g.drawImage(center,bt, bt, width-bt*2, height-bt*2, null);
		return bi;
	}
	/**
	 * Calls getComponentImage and adds text
	 * @param width
	 * @param height
	 * @param text
	 * @param textsize
	 * @return
	 */
	public BufferedImage getButtonImage(int width,int height,String text,int textsize){
		BufferedImage bi = getComponentImage(width,height);
		Graphics g = bi.getGraphics();
		font=font.deriveFont(Font.PLAIN, textsize);
		g.setFont(font);
		g.drawString(text, width/2, height/2);
		return bi;
	}
	public static final ComponentStyle test = new ComponentStyle(new File("testStyle.png"),1,"Ariel");
	public static final ComponentStyle menuBoarderStyle = new ComponentStyle(new File("menuBoarderStyle.png"),64,"Ariel");
}
