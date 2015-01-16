package game;

import org.lwjgl.input.Keyboard;

public class Key {
	public final int keycode;
	public final char keychar;
	public Key(int kc, char c){
		keycode=kc;
		keychar=c;
	}
	public static Key n (int kc, char c){
		return new Key(kc,c);
	}
	public static Key n (char c){
		return new Key(Keyboard.getKeyIndex(""+c),c);
	}
}
