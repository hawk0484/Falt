package game;

public interface Menu {
	public void render();
	public void click(int button, int x, int y, int clickcount);
	public String getState();
	public void update();
}
