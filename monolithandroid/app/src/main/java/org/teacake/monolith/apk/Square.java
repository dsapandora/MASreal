package org.teacake.monolith.apk;


	import java.nio.ByteBuffer;
	import java.nio.ByteOrder;
	import java.nio.IntBuffer;
	
	

	import javax.microedition.khronos.opengles.GL10;

	public class Square
	{
		
	    public Square()
	    {
	        
	    	int one = 0x10000;
	    	int texCoords[] = {
	    			
	    			//0, one, one, one, 0, 0, one, 0
	    			one, 0, one, one, 0, 0, 0, one
	    	};
	        
	        int vertices[] = {
	        		-one, -one, one, one, -one, one,
	        		-one, one, one, one, one, one
	               

	            };

	        int colors[] = {
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                  
	            };

	        byte indices[] = {
	                0, 1, 2, 1, 2, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

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

	        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	        mIndexBuffer.put(indices);
	        mIndexBuffer.position(0);
	        
	    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
	    	tbb.order(ByteOrder.nativeOrder());
	    	mTextureBuffer = tbb.asIntBuffer();
	    	mTextureBuffer.put(texCoords);
	    	mTextureBuffer.position(0);
	    
	    }
	    public Square(int r, int g, int b, int a)
	    {
	        //int one = 0x10000;
	        int one = 0x10000;
	        int texCoords[] = {
	    			
	        		one, one,
	        		one, 0, 
	        		0, 0, 
	        		0, one
	    	};
	        int vertices[] = {
	        		-one, -one, one,
	        		-one,  one, one,
	        		 one,  one, one, 
	        		 one, -one, one


	    		            };
	        int colors[] = {
	                  r,      g,    b,  a,
	                  r >> 1, g >> 1,   b >> 1,  a,
	                  r >> 2, g >> 2,    b >> 2,  a,
	                  r,    g,    b,  a

	            };

	        byte indices[] = {
	        		1,3,0,1,2,3
	        		//0, 1, 2, 0, 2, 3
	        		
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

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

		        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		        mIndexBuffer.put(indices);
		        mIndexBuffer.position(0);
		        
			    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		    	tbb.order(ByteOrder.nativeOrder());
		    	mTextureBuffer = tbb.asIntBuffer();
		    	mTextureBuffer.put(texCoords);
		    	mTextureBuffer.position(0);       
	    }

	    
	    public Square(int r, int g, int b, int a, boolean color)
	    {
	        //int one = 0x10000;
	        int one = 0x10000;
	        int texCoords[] = {
	    			
	        		0, one, one, one, 0, 0, one, 0
	    	};
	        int vertices[] = {
	        		-one, -one, one, one, -one, one,
	        		-one, one, one, one, one, one


	    		            };


	        int colors[] = {
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,

	            };

	        byte indices[] = {
	        		0, 1, 2, 2, 1, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

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

	        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	        mIndexBuffer.put(indices);
	        mIndexBuffer.position(0);
	        
		    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
	    	tbb.order(ByteOrder.nativeOrder());
	    	mTextureBuffer = tbb.asIntBuffer();
	    	mTextureBuffer.put(texCoords);
	    	mTextureBuffer.position(0);
	    	
	    }    
	    
	    public Square(int r, int g, int b, int a, boolean color,boolean horizontal)
	    {
	        //int one = 0x10000;
	        int one = 0x10000;
	        int texCoords[] = {
	    			
	        		0, one, one, one, 0, 0, one, 0
	    	};
	        int vertices[] = {
	        		-one, 0,  -one,
	        		one, 0,-one, 
	        		-one, 0, one, 
	        		one, 0, one, 
	        };
	        int colors[] = {
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,

	            };

	        byte indices[] = {
	        		0, 1, 2, 2, 1, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

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

	        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	        mIndexBuffer.put(indices);
	        mIndexBuffer.position(0);
	        
		    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
	    	tbb.order(ByteOrder.nativeOrder());
	    	mTextureBuffer = tbb.asIntBuffer();
	    	mTextureBuffer.put(texCoords);
	    	mTextureBuffer.position(0);
	    	
	    }   	    	
	    		
	    public void setPosition(float x, float y, float z)
	    {
	    	xpos = x;
	    	ypos = y;
	    	zpos = z;
	    }
	    public void draw(GL10 gl)
	    {
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glTranslatef(xpos, ypos, zpos);
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 4);
	        
	    }
	    public void draw(GL10 gl, float scale)
	    {
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glTranslatef(xpos, ypos, zpos);
	        gl.glScalef(scale, scale, scale);
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);    	
	    }
	    public void draw(GL10 gl, float scalex, float scaley, float scalez)
	    {
            gl.glFrontFace(GL10.GL_CW);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            this.textures.setTexture(textureid);
            //gl.glDisable(GL10.GL_DEPTH_TEST);
            //gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
            gl.glNormal3f(0, 0, 1);
            gl.glTranslatef(xpos, ypos, zpos);
            
            gl.glScalef(scalex, scaley, scalez);
            gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
            
            gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
            //gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 3);
            //gl.glDrawArrays(GL10.GL_TRIANGLES, 3, 3);
            //gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 3, 3);
            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
	    
	    public void draw(GL10 gl, float scalex, float scaley, float scalez,float angle)
	    {
	    	gl.glLoadIdentity();
	        gl.glFrontFace(GL10.GL_CCW);
	    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
	    	this.textures.setTexture(textureid);
	    	//gl.glDisable(GL10.GL_DEPTH_TEST);
	    	gl.glDisable(gl.GL_CULL_FACE);//
	    	gl.glEnable(GL10.GL_BLEND);
	    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
	    	gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glNormal3f(0, 0, 1);
	        //gl.glRotatef(angle, 1.0f, 0.0f, 0.0f);
	        gl.glTranslatef(xpos, ypos, zpos);
	        gl.glScalef(scalex, scaley, scalez);
	        
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                    //GL10.GL_REPLACE
                    GL10.GL_MODULATE
                    );

	        //gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6);
	        //gl.glDrawArrays(mode, first, count)
	        //gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6);
	        gl.glDisable(GL10.GL_TEXTURE_2D);
	        gl.glDisable(GL10.GL_BLEND);
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //gl.glEnable(gl.GL_CULL_FACE);
	        gl.glEnable(gl.GL_DEPTH_TEST);

	    }
	    
	    
	    
	    public void setTextures(GLTextures textures)
	    {
	    	this.textures = textures;
	    }
	    public void setTextureId(int id)
	    {
	    	this.textureid = id;
	    }
	    private IntBuffer   mVertexBuffer;
	    private IntBuffer   mColorBuffer;
	    private ByteBuffer  mIndexBuffer;
	    private IntBuffer  mTextureBuffer;
	    private float xpos;
	    private float ypos;
	    private float zpos;
	    private GLTextures textures;
	    private int textureid;
	

}
