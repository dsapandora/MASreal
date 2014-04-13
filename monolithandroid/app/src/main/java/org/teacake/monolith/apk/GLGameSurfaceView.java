package org.teacake.monolith.apk;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import android.content.SharedPreferences;


public class GLGameSurfaceView extends GLSurfaceView
{

	public GLGameSurfaceView(Context context,GameOverlay overlay,SharedPreferences preferences) {
		super(context);
		// TODO Auto-generated constructor stub
        this.prefs = preferences;
        
        this.context = context;
        this.overlay = overlay;
        //this.overlay.setCurtain(100);
        this.viewType = GameRenderer.VIEW_INTRO;
        this.gameType = Game.GAME_MONOLITH;
        if (this.mRenderer!=null)
        {
        	this.mRenderer = null;
        }
        this.getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);
        //this.setDebugFlags(DEBUG_CHECK_GL_ERROR);
        this.mRenderer = new GameRenderer(context,overlay);
		
		this.setRenderer(mRenderer);
		setFocusableInTouchMode(true);
	}
	
	private GameRenderer mRenderer;
	
	
	

	@Override
	public void onPause()
	{
		
		super.onPause();
		
		mRenderer.onPause();
		if(this.gameType==Game.GAME_CLASSIC || this.gameType==Game.GAME_MONOLITH)
		{
			this.getOverlay().getOptions().getGame().saveGame(prefs);
		}
		//this.mRenderer = null;
	}
    @Override
    public void onResume()
    {
    	super.onResume();
    	mRenderer.onResume();
		//this.mRenderer = new GameRenderer(context,overlay);
		//this.setRenderer(mRenderer);
    }

    
    public void initGame(int gameType)
    {
            switch(gameType)
            {
                    case GameRenderer.VIEW_INTRO:
                    	this.mRenderer.setGameType(Game.GAME_MONOLITH);
                    	this.mRenderer.setViewType(GameRenderer.VIEW_INTRO);
                            overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
                    
                    overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                    
                            break;
                    case GameRenderer.VIEW_GAME:
                    	this.mRenderer.setGameType(overlay.getOptions().getGameType());
                    	this.mRenderer.setViewType(GameRenderer.VIEW_GAME);
                            switch(overlay.getOptions().getGameType())
                            {
                            case Game.GAME_CLASSIC:
                                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_CLASSIC);
                                    overlay.getOptions().setGame(new SimpleGameData());
                                    overlay.getOptions().getGame().setLevel(overlay.getOptions().getStartingLevel());
                                    overlay.setLevel(overlay.getOptions().getGame().getLevelName());
                                    overlay.getOptions().getGame().initGame(overlay.getOptions().getStartingLevel());
                                    break;
                            case Game.GAME_MONOLITH:
                                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_MONOLITH);
                                    overlay.getOptions().setGame(new MonolithGameData());
                                    overlay.getOptions().getGame().setLevel(overlay.getOptions().getStartingLevel());
                                    overlay.setLevel(overlay.getOptions().getGame().getLevelName());
                                    overlay.getOptions().getGame().initGame(overlay.getOptions().getStartingLevel());
                                    break;
                                    
                            }
                            overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                            break;
                    case GameRenderer.VIEW_OPTIONS:
                    		this.mRenderer.setGameType(Game.GAME_MONOLITH);
                            this.mRenderer.setViewType(GameRenderer.VIEW_OPTIONS);
                            overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_OPTIONS);
                            overlay.setDrawType(GameOverlay.DRAW_GAME_OPTIONS);
                            overlay.getOptions().setSelectionStatus(Options.STATUS_SELECTING);
                            
                            break;
                    
            }
            this.mRenderer.reinit();
    }

    public void stopMusic()
    {
    	
            //glThread.requestExitAndWait();
            //glThread = null;
    }

    public void setViewType(int viewType)
    {
            this.viewType = viewType;
            
            
    }
    public void setGameType(int gameType)
    {
            this.gameType = gameType;
            
    }
    

    public boolean onKeyDown(int keyCode, final KeyEvent msg) {
        boolean handled = false;
        
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN||keyCode==KeyEvent.KEYCODE_Z||keyCode==KeyEvent.KEYCODE_X||keyCode==KeyEvent.KEYCODE_C)
        {
        	try
        	{
        		queueEvent(new Runnable(){
                    public void run() {
                        mRenderer.setAction(GameRenderer.MSG_MOVE_DOWN, 0, 0);
                    }});


        	}
        	catch(Exception e)
        	{
        		
        	}
        	return true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode== KeyEvent.KEYCODE_A||keyCode== KeyEvent.KEYCODE_S)
        {
        	try
        	{
        		queueEvent(new Runnable(){
                    public void run() {
                        mRenderer.setAction(GameRenderer.MSG_MOVE_LEFT, 0, 0);
                    }});
        		
        		

        		
        	}
        	catch(Exception e)
        	{
        		
        	}
        	return true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT||keyCode== KeyEvent.KEYCODE_D||keyCode== KeyEvent.KEYCODE_F)
        {
        	try
        	{
        		
        		queueEvent(new Runnable(){
                    public void run() {
                        mRenderer.setAction(GameRenderer.MSG_MOVE_RIGHT, 0, 0);
                    }});        		

        	}
        	catch(Exception e)
        	{
        		
        	}
        	return true;
        }
        if(keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_L)
        {
        	try
        	{
        		queueEvent(new Runnable(){
                    public void run() {
                        mRenderer.setAction(GameRenderer.MSG_ROTATE, 0, 0);
                    }});            		
        		
        	}
        	catch(Exception e)
        	{
        		
        	}
        	return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	
        }
        

        
        return super.onKeyDown(keyCode, msg);
    }

    /**
     * Standard override for key-up. We actually care about these,
     * so we can turn off the engine or stop rotating.
     */

    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
    	int action = event.getAction();
    	boolean handled = false;
    	if(action==MotionEvent.ACTION_DOWN)
    	{
    		xval=(int)event.getX();
    		yval=(int)event.getY();

    		return true;
    	}
    	
    	if(action==MotionEvent.ACTION_UP)
    	{
    		int xnow = (int)event.getX();
    		int ynow = (int)event.getY();
    		if(xnow<20 && ynow<20)
    		{
    			zx =0 ;
    			zy =0 ;
        		try
        		{
            		queueEvent(new Runnable(){
                        public void run() {
                            mRenderer.setAction(GameRenderer.MSG_ROTATE_PLAYFIELD, zx, zy);
                        }});                			

        		}
        		catch(Exception e)
        		{
        			
        		}
    		}
    		return true;
    	}
    	if(action==MotionEvent.ACTION_MOVE)
    	{
            zx = zx+((int)event.getX()-xval);
            zy = zy+((int)event.getY()-yval);
      	  	xval=(int)event.getX();
      	  	yval=(int)event.getY();
    		try
    		{
        		queueEvent(new Runnable(){
                    public void run() {
                        mRenderer.setAction(GameRenderer.MSG_ROTATE_PLAYFIELD, zx, zy);
                    }});   
    		}
    		catch(Exception e)
    		{
    			
    		}      	  	
      	  	return true;
    	}

        return super.onTouchEvent(event);
    } 
    public GameRenderer getRenderer()
    {
    	return this.mRenderer;
    }
    private int xval;
    private int yval;
    private int zx;
    private int zy;    
    private SharedPreferences prefs;
    private int viewType;
    private int gameType;
    private GameOverlay overlay;
    private Sound soundManager;
    public GameOverlay getOverlay()
    {
            return overlay;
    }
    private android.content.Context context;
	
}
