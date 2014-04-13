package opengl.Zh0k.solarsystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SolarSystemRenderer implements GLSurfaceView.Renderer {

    private boolean mTranslucentBackground;
    private Planet mPlanet;
    private float mTransY;

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

    public SolarSystemRenderer(boolean useTranslucentBackground)
    {
        mTranslucentBackground = useTranslucentBackground;

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
        float paleYellow[]={1.0f, 1.0f, 0.3f, 1.0f}; //1
        float white[]={1.0f, 1.0f, 1.0f, 1.0f};
        float cyan[]={0.0f, 1.0f, 1.0f, 1.0f};
        float black[]={0.0f, 0.0f, 0.0f, 0.0f}; //2
        float orbitalIncrement= 1.25f; //3
        float[] sunPos={0.0f, 0.0f, 0.0f, 1.0f};

        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f,0.0f, 0.0f, 1.0f);
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
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glDepthMask(false);
        //initGeometry(gl);
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

    protected static FloatBuffer makeFloatBuffer(float[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }
}
