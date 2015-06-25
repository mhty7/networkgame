
import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
public class PlayerCharacter extends CharacterAbstruct{
	
	private float pi = MySetting.PI;
	private Texture texture2;
	private int alpha;
	public float turnAngle;
	public float accel_x;
	public float accel_y;
	public float velocity_x;
	public float velocity_y;
	public int name;
	
	
	public PlayerCharacter(float x,float y,float size,float angle,float turnAngle,int name,int alpha, Texture texture,Texture texture2) {
		super(x,y,size,0.0f,angle,texture);
		this.turnAngle=turnAngle;
		this.accel_x=0.0f;
		this.accel_y=0.0f;
		this.velocity_x=0.0f;
		this.velocity_y=0.0f;	
		this.name=name;
		this.alpha=alpha;
		this.texture2=texture2;
	}
	public float[] getPointingLocation(){
		float theta = angle/180.0f*pi;
		float[] arr=new float[2];
		arr[0]=x+(float)Math.cos(theta)*size*0.5f;
		arr[1]=y+(float)Math.sin(theta)*size*0.5f;
		return arr;
	}
	public void move(){
		velocity_x+=accel_x;
		velocity_y+=accel_y;
		x=velocity_x;
		y=velocity_y;
		velocity_x*=0.4f;
		velocity_y*=0.4f;
	}
	public void draw(GL gl){
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f,1.0f);
		gl.glScalef(size,size,1.0f);
		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,alpha);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(x, y-size*0.7f, 0.0f);
		gl.glScalef(0.126f,0.038f,1.0f);
		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture2, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,alpha);
		gl.glPopMatrix();
		
	}

}
