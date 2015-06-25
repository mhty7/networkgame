
import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
public class EnemyCharacter extends CharacterAbstruct{
	
	private float pi = MySetting.PI;
	public float turnAngle;
	public EnemyCharacter(float x,float y,float size,float velocity,float angle,float turnAngle, Texture texture) {
		super(x,y,size,velocity,angle,texture);
		this.turnAngle=turnAngle;
		
	}
	public void move(){
		float theta = angle/180.0f*pi;
		x=x+(float)Math.cos(theta)*velocity;
		y=y+(float)Math.sin(theta)*velocity;
	}
	public boolean isInside(float x,float y){
		boolean b=false;
		float dx=x-this.x;
		float dy=y-this.y;
		
		if(dx*dx+dy*dy<=(size*0.5f)*(size*0.5f)){
			b=true;
		}
		return b;
	}
	public void draw(GL gl){
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f,1.0f);
		gl.glScalef(size,size,1.0f);
		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		gl.glPopMatrix();
	}
	public void warp(){
		if(x>=0.8f)
			x-=1.6f;
		if(x<=-0.8f)
			x+=1.6f;
		if(y>=0.8f)
			y-=1.6f;
		if(y<=-0.8f)
			y+=1.6f;
	}

}
