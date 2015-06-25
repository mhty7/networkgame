

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;

public class EffectCharacter extends CharacterAbstruct{
	private int life;
	public boolean flg;
	private int max_life=200;
	private float pi = MySetting.PI;
	public EffectCharacter(float x,float y,float size,float velocity,float angle,Texture texture) {
		super(x,y,size,velocity,angle,texture);
		this.life=this.max_life;
		this.flg=false;
	}
	public boolean isAlive(){
		boolean b=false;
		if(life--<0){
			b=false;
			this.flg=false;
			life=this.max_life;
		}
		else{
			b=true;
		}
		return b;
	}
	public void move(){
		angle+=velocity/pi*180.0f;
	}
	public void draw(GL gl){
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f,1.0f);
		gl.glScalef(size,size,1.0f);
		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255*this.life/this.max_life);
		gl.glPopMatrix();
	}

}
