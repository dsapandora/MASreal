package com.dsalab.masreal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dsalab.masreal.conexion.conexion;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    // For tap event
    private GestureDetector mGestureDetector;
    private static final String TAG = "MainActivity";

    private CamLayer mPreview;
    static int counter=0;
    private SolarSystemRenderer glView;
    private SensorManager sManager;
    private SatellitelTask mAuthTask;


    @Override
    protected void onDestroy()
    {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sManager =(SensorManager)getSystemService(SENSOR_SERVICE);
        counter++;
        if (counter==2) {
        //    MediaPlayer mp=MediaPlayer.create(this, R.raw.track);
         //   mp.start();
        }
        callTribunal();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Stop the preview and release the camera.
            // Execute your logic as quickly as possible
            // so the capture happens quickly.

            return false;
        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            openOptionsMenu();
             return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }
    
    @Override
    protected void onPause() {
        sManager.unregisterListener(glView);
        super.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        glView=new  SolarSystemRenderer(this,true);
        sManager.registerListener(glView,sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
        sManager.registerListener(glView,sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_FASTEST);


       // mPreview = new CamLayer(this, glView);

        setContentView(glView);
        //addContentView(mPreview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menu_stop:
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // Nothing else to do, closing the Activity.
        finish();
    }
    
    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
            	 Log.d(TAG,"gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                    //handleGestureTap();
                	 openOptionsMenu();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    handleGestureTwoTap();
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    handleGestureSwipeRight();
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    handleGestureSwipeLeft();
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }

    private void handleGestureTwoTap()
    {
    	 Log.d(TAG,"handleGestureTwoTap() called.");
    }

    private void handleGestureSwipeRight()
    {
    	 Log.d(TAG,"handleGestureSwipeRight() called.");
    	 // Quit
         this.finish();
    }

    private void handleGestureSwipeLeft()
    {
    	 Log.d(TAG,"handleGestureSwipeLeft() called.");
    }

    public void callTribunal(){


        List<NameValuePair> postInfo = new ArrayList<NameValuePair>();

        mAuthTask = new SatellitelTask();
        mAuthTask.execute(postInfo);

    }

    public class SatellitelTask extends AsyncTask<List<NameValuePair>, Void, HttpResponse > {
        /**
         * Función que crea el thread que corre en el background
         * */


        @Override
        protected HttpResponse doInBackground(List<NameValuePair>... params) {
            return conexion.postCedula(getApplicationContext(), params[0]);
        }

        /** Método que hace el cambio de actividad cuando el login fue un exito*/

        @Override
        protected void onPostExecute(final HttpResponse response) {
            mAuthTask = null;
            try {
                String mensaje_respuesta = EntityUtils.toString(response.getEntity());
                Log.e("Mensaje",mensaje_respuesta);
                try {
                    JSONObject nuevo = new JSONObject(mensaje_respuesta);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();

            }



        }

    }
    
}
