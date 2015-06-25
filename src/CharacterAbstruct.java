

import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;

public class CharacterAbstruct {
	public float x;
	public float y;
	public float size;
	public float velocity;
	public float angle;
	protected Texture texture;
	public CharacterAbstruct(float x,float y,float size,float velocity,float angle,Texture texture) {
		this.x=x;
		this.y=y;
		this.size=size;
		this.velocity=velocity;
		this.angle=angle;
		this.texture=texture;
	}
	protected void move(){
	}
	protected void draw(GL gl){
		
	}
	
}
