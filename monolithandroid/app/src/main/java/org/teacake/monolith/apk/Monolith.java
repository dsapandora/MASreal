package org.teacake.monolith.apk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Monolith extends Activity
{
	
	private static final int ID_PLAY_GAME = Menu.FIRST;
	private static final int ID_OPTIONS = Menu.FIRST + 1;
	private static final int ID_EXIT = Menu.FIRST+2;
    

    private GLGameSurfaceView gsf;
    private GameOverlay overlay;
    private View gameHudView;
    private HighScoreTable hsTable;
    private Options options;
    
    private Game game;
    private android.widget.CheckBox checkboxAcceptLicense;
    private android.widget.Button buttonOK;
    private android.widget.Button buttonCancel;
    private android.widget.TextView textviewLicense;
    public SharedPreferences.Editor prefsEditor;
    public SharedPreferences prefs;
    public android.view.View licenseView;
    private boolean soundInitialized;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        soundInitialized = false;
        prefs = this.getPreferences(android.content.Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
        //prefsEditor.putBoolean("LicenseAccepted", false);
        //prefsEditor.commit();
        
        if(false/*!prefs.getBoolean("LicenseAccepted", false)*/)
        {
        	
        	//this.licenseView = this.findViewById(R.layout.licenseagreement);
        	this.licenseView = View.inflate(this, R.layout.licenseagreement, null);
        	this.setContentView(licenseView);
        	this.checkboxAcceptLicense = (android.widget.CheckBox)this.findViewById(R.id.checkLicenseAgreement);
        	this.textviewLicense = (android.widget.TextView)this.findViewById(R.id.textviewLicenseAgreement);
        	this.buttonOK= (android.widget.Button)this.findViewById(R.id.buttonOK);
        	this.buttonCancel = (android.widget.Button)this.findViewById(R.id.buttonCancel);
        	this.buttonCancel.setOnClickListener(
        											new View.OnClickListener()
        											{
        												public void onClick(View view)
        												{
        													exitNotAcceptedApplication();
        												}
        											}
        										);
        	this.buttonOK.setOnClickListener(
					new View.OnClickListener()
					{
						public void onClick(View view)
						{
							if(checkboxAcceptLicense.isChecked())
							{
								startAcceptedApplication();
								
							}
							
						}
					}
				);
        }
        else
        {
        	initActivity();
        }
 
        
         
    }
    public void startAcceptedApplication()
    {
    	prefsEditor.putBoolean("LicenseAccepted", true);
		//prefsEditor.commit();
		licenseView.setVisibility(View.INVISIBLE);
		licenseView = null;		
		initActivity();
		
		
    }
    public void initActivity()
    {
        
        this.soundInitialized = true;
        hsTable = new HighScoreTable(this,10);
        game = new MonolithGameData();
        
        options = new Options(game,prefs);
        overlay = new GameOverlay(this,hsTable,options);
        overlay.setVisibility(View.VISIBLE);
        overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        
        gameHudView = View.inflate(this, R.layout.game, null);
        gsf = new GLGameSurfaceView(this,overlay,prefs);

        setContentView(gsf);
        gsf.setVisibility(View.VISIBLE);   
        
        this.addContentView(overlay,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        this.addContentView(gameHudView, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        gameHudView.findViewById(R.id.ImageButton01).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_LEFT, 0, 0);
			                    }});
			        		
			        		

			        		
			        	}
			        	catch(Exception e)
			        	{
			        		
			        	}
					}
				}
			);		
        gameHudView.findViewById(R.id.ImageButton02).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_RIGHT, 0, 0);
			                    }});
			        		
			        		

			        		
			        	}
			        	catch(Exception e)
			        	{
			        		
			        	}
					}
				}
			);	
        gameHudView.findViewById(R.id.ImageButton03).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_ROTATE, 0, 0);
			                    }});
			        		
			        		

			        		
			        	}
			        	catch(Exception e)
			        	{
			        		
			        	}
					}
				}
			);		
        gameHudView.findViewById(R.id.ImageButton04).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_DOWN, 0, 0);
			                    }});
			        		
			        		

			        		
			        	}
			        	catch(Exception e)
			        	{
			        		
			        	}
					}
				}
			);
        
        
        
    }
    
    
    @Override
    public void onResume()
    {
    	
    	super.onResume();
    	//initActivity();
    	gsf.onResume();
    }
    @Override
    public void onPause()
    {
    	super.onPause();
    	gsf.onPause();
    	System.exit(0);
    	//gsf.stopMusic();
    }

 
    public void playGame()
    {
    	
		gsf.setGameType(overlay.getOptions().getGameType());
		
		gsf.initGame(GameRenderer.VIEW_GAME);
		
    	
    }
    public void showOptions()
    {
    	gsf.setGameType(game.getGameType());
    	
    	gsf.initGame(GameRenderer.VIEW_OPTIONS);
    	
    	
    }

    

    public void exitApplication()
    {
    		
    	
			finish();
    }
    public void exitNotAcceptedApplication()
    {
    	
		finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ID_PLAY_GAME:
            playGame();
            return true;
        case ID_OPTIONS:
        	showOptions();
        	return true;
        //case ID_EXIT:
        //	exitApplication();
        //	return true;
        
        }
       
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, ID_PLAY_GAME,0 ,R.string.s_play);
        menu.add(0, ID_OPTIONS,0,R.string.s_options);
        //menu.add(0, ID_EXIT,0,R.string.s_exit);
        return true;
    }   
    

    
     
}