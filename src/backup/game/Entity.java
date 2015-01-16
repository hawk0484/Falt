package game;

public class Entity {
	protected Entity(int id){
		Entitys[id]=this;
	}
	public static final Entity[] Entitys = new Entity[256];
}
