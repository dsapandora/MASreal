package opengl.Zh0k.solarsystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.*;import javax.microedition.khronos.opengles.GL10;
import java.lang.Math;

public class Starfield
{
	
	
	public Starfield(int numstars,float radius)
	{
		this.numstars = numstars;
		java.util.Random random = new java.util.Random();

		int[] vertices = new int[3*numstars];
		int[] colors = new int[4*numstars];
		for(int i=0;i<numstars;i++)
		{
			float theta = random.nextFloat()*360.0f;
			float phi = random.nextFloat()*360.0f;
			float xpos = radius*(float)(Math.sin((double)phi)*(float)Math.cos((double)theta));
			float ypos = radius*(float)(Math.sin((double)phi)*(float)Math.sin((double)theta));
			float zpos = radius*(float)(Math.cos((double)phi));
			vertices[i*3]=opengl.Zh0k.solarsystem.util.FixedPointFloat.floatToFixedPoint(xpos);
			vertices[i*3+1]=opengl.Zh0k.solarsystem.util.FixedPointFloat.floatToFixedPoint(ypos);
			vertices[i*3+2]=opengl.Zh0k.solarsystem.util.FixedPointFloat.floatToFixedPoint(zpos);
			int color = random.nextInt();
			//color=(color & 0xff);
			//color = //0x00000000 | (color<<16) | (color<<8) | color;
			colors[i*3]=color;
			colors[i*3+1]=color;
			colors[i*3+2]=color;
			colors[i*3+3]=color;
		}
	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	    vbb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = vbb.asIntBuffer();
	        mVertexBuffer.put(vertices);
	        mVertexBuffer.position(0);

	    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
	    cbb.order(ByteOrder.nativeOrder());
	        mColorBuffer = cbb.asIntBuffer();
	        mColorBuffer.put(colors);
	        mColorBuffer.position(0);
	}
	
	public void draw(GL10 gl,float zpos,float yang)
	{
        gl.glFrontFace(GL10.GL_CW);
        
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glRotatef(yang, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, zpos);

        gl.glDrawArrays(GL10.GL_POINTS,0,numstars);
	}
	private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;

    private int numstars;
    
}
