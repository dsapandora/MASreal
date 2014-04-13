package com.dsalab.masreal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class MainActivity extends Activity {
    // For tap event
    private GestureDetector mGestureDetector;
    private static final String TAG = "MainActivity";

    private CamLayer mPreview;
    static int counter=0;
    private SolarSystemRenderer glView;


    @Override
    protected void onDestroy()
    {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter++;
        if (counter==2) {
        //    MediaPlayer mp=MediaPlayer.create(this, R.raw.track);
         //   mp.start();
        }
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
        super.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();

        glView=new  SolarSystemRenderer(this,true);

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
    
}
