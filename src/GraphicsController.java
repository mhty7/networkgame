
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class GraphicsController {
	
	public static void drawSquare(GL gl,float x,float y,
			int r,int g, int b,int a){
		drawRectangle(gl,x,y,1.0f,1.0f,r,g,b,a);
		
	}

	private static void drawRectangle(GL gl,float x, float y, float width, float height,
			int r, int g, int b, int a) {
		float[] squareVertices={
				-0.5f*width+x, -0.5f*height+y,
				0.5f*width+x, -0.5f*height+y,
				-0.5f*width+x, 0.5f*height+y,
				0.5f*width+x, 0.5f*height+y };
		
		int[] squareColors={
				r,g,b,a,
				r,g,b,a,
				r,g,b,a,
				r,g,b,a };
		
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glColor4ui(squareColors[0], squareColors[1], squareColors[2], squareColors[3]);
		gl.glVertex3f(squareVertices[0],squareVertices[1],0.0f);
		gl.glColor4ui(squareColors[4], squareColors[5], squareColors[6], squareColors[7]);
		gl.glVertex3f(squareVertices[2],squareVertices[3],0.0f);
		gl.glColor4ui(squareColors[8], squareColors[9], squareColors[10], squareColors[11]);
		gl.glVertex3f(squareVertices[4],squareVertices[5],0.0f);
		gl.glColor4ui(squareColors[12], squareColors[13], squareColors[14], squareColors[15]);
		gl.glVertex3f(squareVertices[6],squareVertices[7],0.0f);
		gl.glEnd();
		
	}
	
	public static void drawTexture(GL gl,float x,float y,float width,float height,
			Texture texture,float u,float v,float tex_width,float tex_height,int r,int g,int b, int a){
	
		float[] squareVertices = {
		        -0.5f*width + x,-0.5f*height + y,
				 0.5f*width + x,-0.5f*height + y,
		        -0.5f*width + x,0.5f*height + y,
				 0.5f*width + x,0.5f*height + y };
		
		float[] texCoords = {
				u,v+tex_height,
				u+tex_width,v+tex_height,
				u,v,
				u+tex_width,v };

		gl.glEnable(GL.GL_TEXTURE_2D);
		texture.enable();
		texture.bind();
		
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glColor4ub((byte)r,(byte)g,(byte)b,(byte)a);
		gl.glTexCoord2f(texCoords[0],texCoords[1]);
		gl.glVertex3f(squareVertices[0],squareVertices[1],0.0f);
		gl.glTexCoord2f(texCoords[2],texCoords[3]);
		gl.glVertex3f(squareVertices[2],squareVertices[3],0.0f);
		gl.glTexCoord2f(texCoords[4],texCoords[5]);
		gl.glVertex3f(squareVertices[4],squareVertices[5],0.0f);
		gl.glTexCoord2f(texCoords[6],texCoords[7]);
		gl.glVertex3f(squareVertices[6],squareVertices[7],0.0f);
		gl.glEnd();
		texture.disable();
		gl.glDisable(GL.GL_TEXTURE_2D);
	
	}
	
	public static Texture loadTexture(GL gl,String path) throws GLException, IOException{
		
		File f=new File(path);
		Texture texture = TextureIO.newTexture(f,false);
		//TextureData textureData=TextureIO.newTextureData(f,GL.GL_RGBA,GL.GL_RGBA,false,TextureIO.PNG);
		//Texture texture = TextureIO.newTexture(textureData);
		
		
		/*
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, GL.GL_TRUE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_BASE_LEVEL, 0);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_LEVEL, 5);
		*/
		
		return texture;
	}
	

	
}
