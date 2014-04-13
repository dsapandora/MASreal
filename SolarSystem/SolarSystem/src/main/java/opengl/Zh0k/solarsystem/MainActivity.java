package opengl.Zh0k.solarsystem;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private CamLayer mPreview;
    static int counter=0;
    private SensorManager sManager;

    private SolarSystemRenderer glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter++;
        if (counter==2) {
            MediaPlayer mp=MediaPlayer.create(this, R.raw.track);
            mp.start();
        }
/*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView view = new GLSurfaceView(this);
        //Dar 8 píxeles por componente y 16 de Z
        view.setEGLConfigChooser(8,8,8,8,16,0); //The new line
        view.setRenderer(new SolarSystemRenderer(true));
        setContentView(view);
        */
    }

    /** Called when the activity is first created. */
    @Override
    public void onResume() {
        super.onResume();
        sManager =(SensorManager)getSystemService(SENSOR_SERVICE);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        glView=new  SolarSystemRenderer(this,true);
        sManager.registerListener(glView,sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);


        mPreview = new CamLayer(this, glView);

        setContentView(glView);
        addContentView(mPreview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(glView);
        if (counter>=2) {
            System.exit(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
}
