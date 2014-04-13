package opengl.Zh0k.solarsystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.SurfaceHolder;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SolarSystemRenderer extends GLSurfaceView implements GLSurfaceView.Renderer, Camera.PreviewCallback,SurfaceHolder.Callback {


    private Planet mPlanet;
    private float mTransY;
    int onDrawFrameCounter=1;
    FloatBuffer cubeBuff;
    FloatBuffer texBuff;

    int[] cameraTexture;
    byte[] glCameraFrame=new byte[256*256]; //size of a texture must be a power of 2
    static float angle= 0.0f;

    public final static int X_VALUE= 0;
    public final static int Y_VALUE= 1;
    public final static int Z_VALUE= 2;
    Planet m_Earth;
    Planet m_Sun;
    float[] m_Eyeposition = {0.0f, 0.0f, 0.0f};


    public final static int SS_SUNLIGHT= GL10.GL_LIGHT0;
    public final static int SS_FILLLIGHT1= GL10.GL_LIGHT1;
    public final static int SS_FILLLIGHT2= GL10.GL_LIGHT2;

    public SolarSystemRenderer(Context c,boolean useTranslucentBackground)
    {
        super(c);
        this.setEGLConfigChooser(5, 6, 5, 8, 16, 0);
        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        this.setRenderer(this);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);


        m_Eyeposition[X_VALUE] = 0.0f;  //1
        m_Eyeposition[Y_VALUE] = 0.0f;
        m_Eyeposition[Z_VALUE] = 5.0f;

        m_Earth= new Planet(50, 50, .3f, 1.0f);  //2
        m_Earth.setPosition(0.0f, 0.0f, -2.0f);  //3

        m_Sun= new Planet(50, 50,1.0f, 1.0f);  //4
        m_Sun.setPosition(0.0f, 0.0f, 0.0f);  //5

    }

    public void onDrawFrame(GL10 gl)
    {
        onDrawFrameCounter++;

        float paleYellow[]={1.0f, 1.0f, 0.3f, 1.0f}; //1
        float white[]={1.0f, 1.0f, 1.0f, 1.0f};
        float cyan[]={0.0f, 1.0f, 1.0f, 1.0f};
        float black[]={0.0f, 0.0f, 0.0f, 0.0f}; //2
        float orbitalIncrement= 1.25f; //3
        float[] sunPos={0.0f, 0.0f, 0.0f, 1.0f};

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        bindCameraTexture(gl);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 4.2f, 0, 0, 0, 0, 1, 0);

        //gl.glRotatef(onDrawFrameCounter,1,0,0); //Rotate the camera image
         gl.glRotatef((float)Math.sin(onDrawFrameCounter/20.0f)*40,0,1,0); //Rotate the camera image
         gl.glRotatef((float)Math.cos(onDrawFrameCounter/40.0f)*40,0,0,1); //Rotate the camera ima

        gl.glPushMatrix();   //4
        gl.glTranslatef(-m_Eyeposition[X_VALUE], -m_Eyeposition[Y_VALUE],-m_Eyeposition[Z_VALUE]);
//5
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(sunPos));   //6
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(cyan));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(white));
        gl.glPushMatrix();   //7
        angle+=orbitalIncrement;   //8
        gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);   //9
        executePlanet(m_Earth, gl);   //10
        gl.glPopMatrix();   //11
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(paleYellow));
//12
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(black)); //13
        executePlanet(m_Sun, gl); //14
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, makeFloatBuffer(black)); //15
        gl.glPopMatrix();
    }

    private void executePlanet(Planet m_Planet, GL10 gl)
    {
        float posX, posY, posZ;
        posX = m_Planet.m_Pos[0]; //17
        posY = m_Planet.m_Pos[1];
        posZ = m_Planet.m_Pos[2];
        gl.glPushMatrix();
        gl.glTranslatef(posX, posY, posZ); //18
        m_Planet.draw(gl); //19
        gl.glPopMatrix();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        float aspectRatio;
        float zNear =.1f;
        float zFar =1000;
        float fieldOfView = 30.0f/57.3f; //30º converted to radians
        float size;

        gl.glEnable(GL10.GL_NORMALIZE);
        aspectRatio=(float)width/(float)height;

        gl.glMatrixMode(GL10.GL_PROJECTION);

        size = zNear * (float)(Math.tan((double)(fieldOfView/2.0f)));
        gl.glFrustumf(-size, size, -size /aspectRatio, size /aspectRatio, zNear, zFar);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        gl.glClearColor(0, 0, 0, 0);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        cubeBuff = makeFloatBuffer(camObjCoord);
        texBuff = makeFloatBuffer(camTexCoords);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        initLighting(gl);
    }

    private void initLighting(GL10 gl)
    {
        float[] sunPos={0.0f, 0.0f, 0.0f, 1.0f};

        float[] posFill1={-15.0f, 15.0f, 0.0f, 1.0f};
        float[] posFill2={-10.0f, -4.0f, 1.0f, 1.0f};

        float[] white={1.0f, 1.0f, 1.0f, 1.0f};
        float[] dimblue={0.0f, 0.0f, .2f, 1.0f};
        float[] cyan={0.0f, 1.0f, 1.0f, 1.0f};
        float[] yellow={1.0f, 1.0f, 0.0f, 1.0f};
        float[] magenta={1.0f, 0.0f, 1.0f, 1.0f};
        float[] dimmagenta={.75f, 0.0f, .25f, 1.0f};
        float[] dimcyan={0.0f, .5f, .5f, 1.0f};

//Lights go here.
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, makeFloatBuffer(sunPos));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, makeFloatBuffer(white));
        gl.glLightfv(SS_SUNLIGHT, GL10.GL_SPECULAR, makeFloatBuffer(yellow));
        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_POSITION, makeFloatBuffer(posFill1));
        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_DIFFUSE, makeFloatBuffer(dimblue));
        gl.glLightfv(SS_FILLLIGHT1, GL10.GL_SPECULAR, makeFloatBuffer(dimcyan));
        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_POSITION, makeFloatBuffer(posFill2));
        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_SPECULAR, makeFloatBuffer(dimmagenta));
        gl.glLightfv(SS_FILLLIGHT2, GL10.GL_DIFFUSE, makeFloatBuffer(dimblue));
//Materials go here.
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, makeFloatBuffer(cyan));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, makeFloatBuffer(white));
        gl.glLightf(SS_SUNLIGHT, GL10.GL_QUADRATIC_ATTENUATION,.001f);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 25);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 0.0f);
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(SS_SUNLIGHT);
        gl.glEnable(SS_FILLLIGHT1);
        gl.glEnable(SS_FILLLIGHT2);

        gl.glLoadIdentity();
    }

    /**
     * Generates a texture from the black and white array filled by the onPreviewFrame
     * method.
     */
    void bindCameraTexture(GL10 gl) {
        synchronized(this) {
            if (cameraTexture==null)
                cameraTexture=new int[1];
            else
                gl.glDeleteTextures(1, cameraTexture, 0);

            gl.glGenTextures(1, cameraTexture, 0);
            int tex = cameraTexture[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
            gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_LUMINANCE, 256, 256, 0, GL10.GL_LUMINANCE, GL10.GL_UNSIGNED_BYTE, ByteBuffer.wrap(glCameraFrame));
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        }
    }

    /**
     * This method is called if a new image from the camera arrived. The camera
     * delivers images in a yuv color format. It is converted to a black and white
     * image with a size of 256x256 pixels (only a fraction of the resulting image
     * is used). Afterwards Rendering the frame (in the main loop thread) is started by
     * setting the newFrameLock to true.
     */
    public void onPreviewFrame(byte[] yuvs, Camera camera) {
        int bwCounter=0;
        int yuvsCounter=0;
        for (int y=0;y<160;y++) {
            System.arraycopy(yuvs, yuvsCounter, glCameraFrame, bwCounter, 240);
            yuvsCounter=yuvsCounter+240;
            bwCounter=bwCounter+256;
        }
    }

    FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    final static float camObjCoord[] = new float[] {
            // FRONT
            -2.0f, -1.5f,  2.0f,
            2.0f, -1.5f,  2.0f,
            -2.0f,  1.5f,  2.0f,
            2.0f,  1.5f,  2.0f,
            // BACK
            -2.0f, -1.5f, -2.0f,
            -2.0f,  1.5f, -2.0f,
            2.0f, -1.5f, -2.0f,
            2.0f,  1.5f, -2.0f,
            // LEFT
            -2.0f, -1.5f,  2.0f,
            -2.0f,  1.5f,  2.0f,
            -2.0f, -1.5f, -2.0f,
            -2.0f,  1.5f, -2.0f,
            // RIGHT
            2.0f, -1.5f, -2.0f,
            2.0f,  1.5f, -2.0f,
            2.0f, -1.5f,  2.0f,
            2.0f,  1.5f,  2.0f,
            // TOP
            -2.0f,  1.5f,  2.0f,
            2.0f,  1.5f,  2.0f,
            -2.0f,  1.5f, -2.0f,
            2.0f,  1.5f, -2.0f,
            // BOTTOM
            -2.0f, -1.5f,  2.0f,
            -2.0f, -1.5f, -2.0f,
            2.0f, -1.5f,  2.0f,
            2.0f, -1.5f, -2.0f,
    };
    final static float camTexCoords[] = new float[] {
            // Camera preview
            0.0f, 0.0f,
            0.9375f, 0.0f,
            0.0f, 0.625f,
            0.9375f, 0.625f,

            // BACK
            0.9375f, 0.0f,
            0.9375f, 0.625f,
            0.0f, 0.0f,
            0.0f, 0.625f,
            // LEFT
            0.9375f, 0.0f,
            0.9375f, 0.625f,
            0.0f, 0.0f,
            0.0f, 0.625f,
            // RIGHT
            0.9375f, 0.0f,
            0.9375f, 0.625f,
            0.0f, 0.0f,
            0.0f, 0.625f,
            // TOP
            0.0f, 0.0f,
            0.9375f, 0.0f,
            0.0f, 0.625f,
            0.9375f, 0.625f,
            // BOTTOM
            0.9375f, 0.0f,
            0.9375f, 0.625f,
            0.0f, 0.0f,
            0.0f, 0.625f
    };

}
