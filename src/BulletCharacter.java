

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

public class BulletCharacter extends CharacterAbstruct {
	private float pi = MySetting.PI;
	public boolean flg;
	public int name;
	public BulletCharacter(float x,float y,float size,float velocity,float angle,Texture texture) {
		super(x,y,size,velocity,angle,texture);
		this.flg=false;
		this.name=0;
	}
	public void move(){
		float theta = angle/180.0f*pi;
		x=x+(float)Math.cos(theta)*velocity;
		y=y+(float)Math.sin(theta)*velocity;
	}
	public void draw(GL gl){
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f,1.0f);
		gl.glScalef(size,size,1.0f);
		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		gl.glPopMatrix();
	}
	public void warpOut(){
		if(x>=0.6f || x<=-0.6f || y>=0.6f || y<=-0.6f)
			this.flg=false;
	}


}
