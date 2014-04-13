package org.teacake.monolith.apk;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Canvas;
import android.opengl.GLSurfaceView.Renderer;

import android.os.SystemClock;
import android.util.Log;

public class GameRenderer implements Renderer {
	
	private boolean done;


	public GameRenderer( android.content.Context context,GameOverlay overlay)
	{
		
		done=false;
		isPaused = false;
		this.overlay = overlay;
		this.context = context;
		initSound();
        mCube = new Cube[8];
        mCube[0] = new Cube(0xff00,0,0,0x10000);
        mCube[1] = new Cube(0,0xff00,0,0x10000);
        mCube[2] = new Cube(0,0,0xff00,0x10000);
        mCube[3] = new Cube(0xff00,0xff00,0,0x10000);
        mCube[4] = new Cube(0xff00,0,0xff00,0x10000);
        mCube[5] = new Cube(0,0xff00,0xff00,0x10000);
        mCube[6] = new Cube(0xf000,0xf0000,0,0x10000);
        mCube[7] = new Cube(0xffff,0x0ffff,0xffff,0x00ff);
        mStarfield = new Starfield(100,90.0f);	
        mMoon = new Square(0xffff,0x0ffff,0xffff,0xffff);
        mEarth = new Square(0xffff,0x0ffff,0xffff,0xffff);
        mExplosionRing = new Square(0xffff,0x0ffff,0xffff,0xffff);
        mExplosionRing = new Square(0xffff,0x0ffff,0xffff,0xffff,true,true);
        this.mPlayfieldCube = new Cube(0x8000,0x8000,0x8000,0x0);
        running=false;
        highscoreEntry = false;
        this.explodingCubes = new java.util.LinkedList<ExplodingCube>();
        this.explodingRings = new java.util.LinkedList<ExplodingRing>();
        randomgen = new java.util.Random(SystemClock.uptimeMillis());
        //this.overlay.setCurtain(100);
        this.game = overlay.getOptions().getGame();
        action = MSG_DO_NOTHING;
        this.initLinearInterpolators();		
	}
	public void initSound()
	{
		this.soundManager = new SoundPoolManager(context);
        soundManager.addSound(R.raw.monolith3, true);
		soundManager.addSound(R.raw.explosion2, false);
		soundManager.addSound(R.raw.place, false);
		soundManager.addSound(R.raw.rotate,false);
		soundManager.addSound(R.raw.pluck, false);
		soundManager.addSound(R.raw.pluck2, false);
		soundManager.addSound(R.raw.speech, false);
		soundManager.addSound(R.raw.evolving, false );
		soundManager.addSound(R.raw.gameover, false);
		
		
		soundManager.startSound();
		soundManager.startMusic(R.raw.monolith3);
		if(!overlay.getOptions().isMusicEnabled())
		{
			soundManager.pauseMusic(R.raw.monolith3);
		}	
	}
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		//Initialize OpenGL...
		/*
         * Get an EGL instance
         */
		if(!this.isPaused)
		{
			this.drawFrame(gl);
		}
        	

	
		
	}

	public void onPause()
	{
		this.isPaused = true;
		
		if(this.soundManager!=null)
		{
			this.soundManager.pauseMusic(R.raw.monolith3);
		}
	    Log.d("GameRenderer", "onPause");
	}
	public void onResume()
	{
		//this.reinit();
		this.isPaused = false;
		this.lastcalltime = System.currentTimeMillis();
		if(overlay.getOptions().isMusicEnabled())
		{
			this.soundManager.resumeMusic(R.raw.monolith3);
		}
	    Log.d("GameRenderer", "onResume");
	}
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.d("GameRenderer", "onSurfaceChanged");
		w=width;
		h=height;
	    gl.glViewport(0, 0, w, h);



		
	}

	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	    Log.d("GameRenderer", "onSurfaceCreated");
	    
		// TODO Auto-generated method stub
		if(this.textures!=null)
		{
			this.textures = null;
		}
		this.textures = new GLTextures(gl,this.context);
		this.textures.add(R.drawable.moon2small);
		this.textures.add(R.drawable.earthsmall);
		this.textures.add(R.drawable.ringsmall);
		this.textures.loadTextures();
		mMoon.setTextureId(R.drawable.moon2small);
		mMoon.setTextures(this.textures);
		mEarth.setTextureId(R.drawable.earthsmall);
		mEarth.setTextures(textures);
		mExplosionRing.setTextureId(R.drawable.ringsmall);
		mExplosionRing.setTextures(textures);
		this.reinit();
				

	}
	public void setViewType(int viewtype)
	{
		this.viewType = viewtype;
	}
	public void setGameType(int gametype)
	{
		this.gametype = gametype;
	}


	
	public void reinit()
	{
		//this.overlay.setCurtain(100);
    	//xval = 0;
    	//yval =0;
        zx=0.0f;
        zy=0.0f;
        xy=0.0f;
        xoff = -10.0f;
    	yoff = 21.0f;
    	zoff = -50.0f;

        
    	this.sayGameOver = true;
        game = overlay.getOptions().getGame();
        
        
       
        
        game.initGame(this.overlay.getOptions().getStartingLevel());
        game.setTimerEnabled(true);
        game.setStatus(SimpleGameData.STATUS_PLAYING);
        
        demogame = new MonolithGameData();
        demogame.initGame(1);
        demogame.setScore(0);
        demogame.setLines(0);
        demogame.setEnergy(100);
        
        demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
        this.setupDemoGrid();
        this.lastcalltime = SystemClock.uptimeMillis();
        this.startGameTime = this.lastcalltime;
        rangle=0;	
        this.running = true;
        this.mSizeChanged = false;
        if(this.overlay.getOptions().isSoundEnabled())
		{
			soundManager.playSound(R.raw.speech);
		}
        this.sayEvolving = true;
        this.timeaccumulator = 0;
	}
	

    public synchronized void doMoveDown()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
    	game.moveBlockDown();
    
    	game.gameLoop();
    	if(game.isBlockPlaced())
    	{
    		//android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_PLACE_BLOCK);
    		if(overlay.getOptions().isSoundEnabled())
    		{
    			this.soundManager.playSound(R.raw.place);
    		}
    		//message.sendToTarget();    		
    	}
    	game.flagCompletedLines();
    	this.createExplosions(game);
    }
    public synchronized void doMoveLeft()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockLeft();
    }
    public synchronized void doMoveRight()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockRight();
    
    }
    public synchronized void doRotatePlayfield(int zx,int zy)
    {
    	this.zx = zx;
    	this.zy = zy;
    }
    public synchronized void doRotatePlayfield(long time)
    {
    	long offset = time-startGameTime;
    	long chooser = offset%60000;
    	xy=this.linearInterpolators[1][0][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zy=this.linearInterpolators[1][1][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zx=this.linearInterpolators[1][2][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);

    }
    
    
    public synchronized void doRotatePlayfieldNormal(long time)
    {
    	long offset = time-startGameTime;
    	long chooser = offset%60000;
    	xy=this.linearInterpolators[0][0][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zy=this.linearInterpolators[0][1][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zx=this.linearInterpolators[0][2][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    }    
    public synchronized void doRotateBlock()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
		//android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_ROTATE_BLOCK);
		if(overlay.getOptions().isSoundEnabled())
		{
			this.soundManager.playSound(R.raw.rotate);
		}
			//message.sendToTarget();
    	game.rotateCurrentBlockClockwise();
    }
    public synchronized void stopMusic()
    {
		//android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_EXIT);
		soundManager.stopSound();
    	//message.sendToTarget();  
    	this.soundManager.stopSound();
    }

    protected void drawNextPiece(GL10 gl)
    {
    	
    	//float ystart=10.0f;
    	
    	if(game.getNextBlock().color>=0 && game.getNextBlock().color<this.mCube.length)
    	{
    		gl.glLoadIdentity();
    		Cube c = this.mCube[game.getNextBlock().color];
    		//gl.glTranslatef(10.0f, ystart, zoff);
    		gl.glTranslatef(18.0f, 0.0f, 0.0f);
    		gl.glRotatef(rangle, 0.0f, 0.0f, 1.0f);
    		gl.glTranslatef(-8.0f,-2.0f ,0.0f );
    		for(int i=0;i<4;i++)
    		{
    			gl.glPushMatrix();
    			c.setPosition((game.getNextBlock().xPos+game.getNextBlock().subblocks[i].xpos)*2.0f,-(game.getNextBlock().yPos+game.getNextBlock().subblocks[i].ypos)*2.0f,zoff);
    			c.draw(gl);
    			gl.glPopMatrix();
    		}
    	}
    }
    protected void drawStarfield(GL10 gl)
    {
    	
    }
    protected void drawExplodingCube(GL10 gl, ExplodingCube c)
    {
    	Cube dc = this.mCube[c.blocktype];
    	gl.glLoadIdentity();
    	dc.setPosition(c.x, c.y, c.z);
    	dc.draw(gl);
    }
    protected void drawBlocks(GL10 gl,Game thegame)
    {
    	for(int y=0;y<20;y++)
    	{
    		for(int x=0;x<10;x++)
    		{
    			if(thegame.getGridValue(x, y)!=-1)
    			{
    				Cube c = this.mCube[thegame.getGridValue(x, y)];
    				gl.glLoadIdentity();
    				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
    				c.draw(gl);
    			}
    		}
    	}
    }
    protected void drawBlocks(GL10 gl,Game thegame, int currentFrame,int maxFrame)
    {
    	float upscalef = (float)currentFrame/(float)maxFrame;
    	float downscalef = 1.0f-upscalef;
    	for(int y=0;y<20;y++)
    	{
    		for(int x=0;x<10;x++)
    		{
    			
    			if(thegame.getGridValue(x, y)!=-1)
    			{
    				if(thegame.getPreviousGridValue(x, y)==-1)
    				{
        				Cube c = this.mCube[thegame.getGridValue(x, y)];
        				gl.glLoadIdentity();
        				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
        				c.draw(gl,upscalef);     					
    				}
    				else
    				{
    					Cube c = this.mCube[thegame.getGridValue(x, y)];
    					gl.glLoadIdentity();
    				
    					c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
    					c.draw(gl);
    				}
    			}
    			else
    			{
    				if(thegame.getPreviousGridValue(x, y)!=-1)
    				{
        				Cube c = this.mCube[thegame.getPreviousGridValue(x, y)];
        				gl.glLoadIdentity();
        				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
        				c.draw(gl,downscalef);   					
    				}
    				
    			}
    		}
    	}
    } 
    protected void drawBlocks(GL10 gl)
    {
    	int[] clearedlines = game.getClearedLines();
    	
    	for(int y=0;y<20;y++)
    	{
    		boolean skipline=false;
    		if(clearedlines[y]==1)
    		{
    			skipline=true;
    		}
    		if(!skipline)
    		{
	    		for(int x=0;x<10;x++)
	    		{
	    			if(game.getGridValue(x, y)!=-1)
	    			{
	    				Cube c = this.mCube[game.getGridValue(x, y)];
	    				gl.glLoadIdentity();
	    				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
	    				c.draw(gl);
	    			}
	    		}
    		}
    	}
    }
    protected void drawFallingBlock(GL10 gl)
    {
    	int result = 0;
    	float offset = 0.0f;
    	if(/*now-lastcalltime>game.getTimer() ||*/ game.getStatus()==SimpleGameData.STATUS_EVOLVING)
    	{
    		result=0;
    	}
    	else
    	{
    		result=(int)((now-lastcalltime)%game.getTimer());
    		offset = (float)((now-lastcalltime)%game.getTimer())/(float)game.getTimer();
    	}
    	float ystart=21.0f;
    	if(game.getCurrentBlock().color>=0 && game.getCurrentBlock().color<this.mCube.length)
    	{
    		Cube c = this.mCube[game.getCurrentBlock().color];
    	
    		for(int i=0;i<4;i++)
    		{
    			gl.glLoadIdentity();
    			if(result!=0 && game.canMoveBlockDown())
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart-offset*2.0f,zoff);
    			}
    			else
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart,zoff);
    			}
    			c.draw(gl);
    		}
    	}
    }
    protected void drawFallingBlock(GL10 gl, int result)
    {
    	
    	float offset = 0.0f;

    	offset = (float)(this.timeaccumulator%game.getTimer())/(float)game.getTimer();
    	
    	float ystart=21.0f;
    	if(game.getCurrentBlock().color>=0 && game.getCurrentBlock().color<this.mCube.length)
    	{
    		Cube c = this.mCube[game.getCurrentBlock().color];
    	
    		for(int i=0;i<4;i++)
    		{
    			gl.glLoadIdentity();
    			if(result!=0 && game.canMoveBlockDown())
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart-offset*2.0f,zoff);
    			}
    			else
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart,zoff);
    			}
    			c.draw(gl);
    		}
    	}
    }    
    protected void drawCubeExplosion(GL10 gl)
    {
    	
    	java.util.ListIterator<ExplodingCube > iter=this.explodingCubes.listIterator();
    	ExplodingCube c = null;
    	long now = System.currentTimeMillis();
    	if(iter!=null)
    	{
    		
    		while(iter.hasNext())
    		{
    			
    			c=iter.next();
    			if(c.frame>MAX_EXPLOSION_FRAME)
    			{
    				
    				iter.remove();
    			}
    			else
    			{
    				float elapsedTime = (now- c.startTime)/1000.0f;
    				drawExplodingCube(gl, c);
    				switch(c.explosionType)
    				{
    				case 1:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;

    					break;
    				case 2:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					
    					break;
    				case 3:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					break;
    				case 4:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					break;
    					default:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;

    						break;
    				}
    				c.frame++;
    			}
    		}
    	}
    }
    protected void drawRingExplosions(GL10 gl)
    {
    	java.util.ListIterator<ExplodingRing > iter=this.explodingRings.listIterator();
    	ExplodingRing r = null;
    	long now = System.currentTimeMillis();
    	if(iter!=null)
    	{
    		
    		while(iter.hasNext())
    		{
    			
    			r=iter.next();
    			if(r.frame>7)
    			{
    				
    				iter.remove();
    			}
    			else
    			{
    				
    				this.mExplosionRing.setPosition(r.x, r.y, r.z);
    				float scale= r.frame*2.4f;
    				mExplosionRing.draw(gl,scale,1.0f,scale,90.0f);
    				r.frame=r.frame+1;
    			}
    		}
    	}    	
    }
    public void createExplosions(Game game)
    {
    	int[] clearedLines = game.getClearedLines();
    	int linecount=game.getClearedLineCount();
    	
    	for (int y=0;y<clearedLines.length;y++)
    	{
    		if (clearedLines[y]==1)
    		{
    			for(int x=0;x<10;x++)
    			{
    				ExplodingCube c = new ExplodingCube
    				(
    						xoff+x*2.0f,
    						yoff-y*2.0f,
    						zoff,
    						(randomgen.nextFloat()-0.5f)/4.0f,
    						(randomgen.nextFloat()-0.5f)*8,
    						(randomgen.nextFloat()-0.5f)*4,
    						game.getGridValue(x, y),
    						0
    						
    				);
    				switch(linecount)
    				{
    				case 1:
    					c.explosionType=0;
    					break;
    				case 2:
    					c.explosionType=1;
    					break;
    				case 3:
    					c.explosionType=2;
    					break;
    					
    				case 4:
    					c.explosionType=3;
    					break;
    				default:
    					c.explosionType=0;
    					break;
    				}
    				c.explosionType=0;
    				
    				this.explodingCubes.add(c);
    				ExplodingRing r = new ExplodingRing(xoff+9.0f,yoff-y*2.0f,zoff);
    				this.explodingRings.add(r);
    			}
        		//android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_EXPLOSION);
        		//message.sendToTarget();
    		}
    	}
    	if(linecount>0)
    	{
			if(this.overlay.getOptions().isSoundEnabled())
			{
				this.soundManager.playSound(R.raw.explosion2);
		
			}

    	}
    }
    /*
     * Draw the playfield for the tetris field
     * 
     */
    protected void drawPlayfield(GL10 gl)
    {

    	/*
    	for(int i=0;i<20;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff-2.0f,yoff-i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff+2.0f*10,yoff-i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    	}
    	for(int i=0;i<10;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff+i*2.0f,yoff-(20*2.0f), zoff);
    		mPlayfieldCube.draw(gl);
    		
    	}
    	*/
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff-2.0f,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f, 20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+2.0f*10,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f,20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+4.5f*2.0f,yoff-(20*2.0f), zoff);
		mPlayfieldCube.draw(gl,10.0f,1.0f,1.0f);
		
		
		
    }
    public void setupDemoGrid()
    {
    	java.util.Random randomgen = new java.util.Random();
    	int result;
    	for (int y=0;y<20;y++)
    	{
    		for (int x=0;x<10;x++)
    		{
    			result = randomgen.nextInt(21);
    			if(result<7)
    			{
    			
    				this.demogame.setGridValue(x, y, result % 6);
    			}
    			else
    			{
    				this.demogame.setGridValue(x, y, -1);
    			}
    		}
    	}
    }    
    protected void drawIntroScreen(GL10 gl,Canvas canvas, int w, int h)
    {
    	long now = SystemClock.uptimeMillis();
    	if(demogame.getStatus()!=SimpleGameData.STATUS_EVOLVING)
    	{
    		demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
    	}
    	if(demogame.getCurrentStep()>50)
    	{
    		setupDemoGrid();
    		demogame.setEnergy(100);
    		demogame.setStep(0);
    	}
    	int result = 0;
    	if(now-lastcalltime>demogame.getTimer())
    	{
    		result=10;
    	}
    	else
    	{
    		result=(int)((now-lastcalltime)%demogame.getTimer());
    		result = (result*10)/demogame.getTimer();
    	}
    	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);
    	//canvas.drawText("energy="+demogame.getEnergy()+" result="+opengl.Zh0k.solarsystem.util.FixedPointFloat.floatToFixedPoint(0.25f), 10, 10, paint);
    	this.drawBlocks(gl,demogame,result,10);

    	//lastdrawtime = System.currentTimeMillis();
    	//this.drawBlocks(gl, demogame);
    	
    	
    }
    protected void drawIntroScreen(GL10 gl,int w, int h)
    {
    	long now = SystemClock.uptimeMillis();
    	if(demogame.getStatus()!=SimpleGameData.STATUS_EVOLVING)
    	{
    		demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
    	}
    	if(demogame.getCurrentStep()>50)
    	{
    		setupDemoGrid();
    		demogame.setEnergy(100);
    		demogame.setStep(0);
    	}
    	int result = (int)((now-lastcalltime)%demogame.getTimer());
    	result = (result*10)/demogame.getTimer();
    	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);
    	//canvas.drawText("energy="+demogame.getEnergy()+" result="+opengl.Zh0k.solarsystem.util.FixedPointFloat.floatToFixedPoint(0.25f), 10, 10, paint);
    	
    	mMoon.setPosition(xoff+13,yoff-22, zoff-50);
		mMoon.draw(gl,18.0f,18.0f,1.0f);
		//this.drawRingExplosion(gl, 0.0f, 0.0f, 0.0f, 90.0f, 10.0f);
    	
		//mExplosionRing.setPosition(xoff+10, yoff-22, zoff-45);
		//mExplosionRing.draw(gl,1.0f,1.0f,1.0f,90.0f); //found it!!!!
		
		
		//mExplosionRing.draw(gl,28.0f,28.0f,1.0f,-90.0f);
		this.drawBlocks(gl,demogame,result,10);
    	//lastdrawtime = System.currentTimeMillis();
    	//this.drawBlocks(gl, demogame);
    	
    	
    }
    private void drawFrame(GL10 gl)
	{
		
        float ratio = (float)w / h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        
        android.opengl.GLU.gluPerspective(gl, 60, ratio, 1, 130);
	    

		long current = SystemClock.uptimeMillis();
		
		rangle=rangle+((current-lastdrawtime)/1000.0f)*2.0f;
     	if(rangle>360.0f)
     	{
     		rangle=0.0f;
     	}			
     	lastdrawtime=current;
     	overlay.postInvalidate(); 

        /*
         * dithering is enabled by default in OpenGL, unfortunately
         * it has a significant impact on performance in software
         * implementation. Often, it's better to just turn it off.
         */
         gl.glDisable(GL10.GL_DITHER);

        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear(). However we must make sure to set the scissor
         * correctly first. The scissor is always specified in window
         * coordinates:
         */
        gl.glClearColor(0,0,0,0);
        
        
        gl.glEnable(GL10.GL_SCISSOR_TEST);
        gl.glScissor(0, 0, w, h);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_CULL_FACE);
        //gl.glCullFace(GL10.GL_CW);
        gl.glShadeModel(GL10.GL_SMOOTH);
        //gl.glDepthFunc(GL10.GL_GREATER);
        //gl.glDepthRangef(1, 100);
        //gl.glDepthMask(false);
        gl.glEnable(GL10.GL_DEPTH_TEST);            

        /*
         * Now we're ready to draw some 3D object
         */
        
        
        gl.glScalef(0.5f, 0.5f, 0.5f);

        //gl.glRotatef(zy, 1, 0, 0);
        gl.glTranslatef(0, 0, zoff);
        if(this.viewType==VIEW_INTRO)
        {
        	gl.glRotatef(rangle, 0.0f, 0.0f, 1.0f);
        }
        gl.glRotatef(zx, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(zy, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(xy, 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(0,0,-zoff);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        

        
        gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        if(!this.backgroundInitialized)
        {
        	this.backgroundInitialized = true;
        }
        switch(this.viewType)
        {
        	case VIEW_INTRO:
        	
	            now = SystemClock.uptimeMillis();
	            if(now>lastcalltime+demogame.getTimer())
	            {

	            		lastcalltime = now;
	            		this.demogame.gameLoop();
	            }
	            this.drawIntroScreen(gl,w,h);
            	//String logo="MonolithAndroid";            	
        	break;
        	
        	case VIEW_OPTIONS:
        		//int savedaction = action;
            	
	            now = SystemClock.uptimeMillis();
	            if(now>lastcalltime+demogame.getTimer())
	            {

	            		lastcalltime = now;
	            		this.demogame.gameLoop();
	            }
	            this.drawIntroScreen(gl,w,h);
            	//this.drawIntroScreen(gl, canvas, w, h);
            	if (action == MSG_ROTATE)
        		{
            		
        			action=MSG_DO_NOTHING;
        			overlay.getOptions().previousOption();
        			if(overlay.getOptions().isSoundEnabled())
        			{
        				this.soundManager.playSound(R.raw.pluck2);
        			}
        		}
        		if (action == MSG_MOVE_LEFT)
        		{
        			action=MSG_DO_NOTHING;
        			overlay.getOptions().setPreviousValue();
        			if(overlay.getOptions().isSoundEnabled())
        			{
        				this.soundManager.playSound(R.raw.pluck);
        			}
            	
        		}
        		if (action == MSG_MOVE_RIGHT)
        		{
        			action=MSG_DO_NOTHING;
        			overlay.getOptions().setNextValue();
        			if(overlay.getOptions().isSoundEnabled())
        			{
        				this.soundManager.playSound(R.raw.pluck);
        			}

        		}
        		if (action == MSG_MOVE_DOWN)
        		{
        			action=MSG_DO_NOTHING;
        			overlay.getOptions().nextOption();
        			if(overlay.getOptions().isSoundEnabled())
        			{
        				this.soundManager.playSound(R.raw.pluck2);
        			}

        		}
        		if(overlay.getOptions().getSelectionStatus()==Options.STATUS_BACK)
        		{
        			this.viewType=VIEW_GAME;
        			this.setViewType(VIEW_INTRO);
        			overlay.setDrawType(GameOverlay.DRAW_NORMAL);
        			overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        		}
        		if(overlay.getOptions().getSelectionStatus()==Options.STATUS_OK)
        		{
        			this.game = this.overlay.getOptions().getGame();
        			this.game.setLevel(this.overlay.getOptions().getStartingLevel());
        			this.overlay.getOptions().getGame().setLevel(this.overlay.getOptions().getStartingLevel());
        			this.viewType=VIEW_GAME;
        			this.setViewType(VIEW_INTRO);
        			overlay.setDrawType(GameOverlay.DRAW_NORMAL);
        			overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        			overlay.getOptions().savePreferences();
        			
        		}
        		int changed = overlay.getOptions().getChangedOption();
        		if(changed == Options.OPTION_MUSIC)
        		{
        			if(overlay.getOptions().isMusicEnabled())
        			{
        				this.soundManager.resumeMusic(R.raw.monolith3);
        				
        			}
        			else
        			{
        				this.soundManager.pauseMusic(R.raw.monolith3);
        			}
        		}
    		break;
        	case VIEW_GAME:
	            switch (game.getStatus())
	            {
		            case SimpleGameData.STATUS_GAME_OVER:
		            	this.overlay.setMessage("Game Over");
		            	if(this.sayGameOver)
		            	{
		            		if(overlay.getOptions().isSoundEnabled())
		            		{
		            			this.soundManager.playSound(R.raw.gameover);
		            		}
		            		this.sayGameOver = false;
		            	}
		            	if(!this.highscoreEntry)
		            	{
		            		if(this.overlay.getHighScoreTable().isHighScore(this.game.getScore()))
		            		{
		            			this.highscoreEntry=true;
		            			this.overlay.setDrawType(GameOverlay.DRAW_NAME_ENTRY);
		            		}
		            	}
		            	else
		            	{
	            			if(action==MSG_ROTATE)
	            			{
	            				this.overlay.selectPreviousChar();
	            			}
	            			if(action==MSG_MOVE_DOWN)
	            			{
	            				this.overlay.selectNextChar();
	            			}
	            			if(action==MSG_MOVE_LEFT)
	            			{
	            				this.overlay.moveBack();
	            			}
	            			if(action==MSG_MOVE_RIGHT)
	            			{
	            				this.highscoreEntry = this.overlay.moveForward();
	            				if(!this.highscoreEntry)
	            				{
	            					this.viewType =VIEW_INTRO;
	            					this.overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
	            				}
	            			}
		            	}
		            	break;
		            case SimpleGameData.STATUS_EVOLVING:
		            	this.overlay.setMessage("Evolving...");
	                	if(this.sayEvolving)
	                	{
	                		if(this.overlay.getOptions().isSoundEnabled())
	                		{
	                			this.soundManager.playSound(R.raw.evolving);
	                		}
	                		this.sayEvolving = false;
	                	}
		            break;
		            default:
		            	this.overlay.setMessage("");
		            	
		            break;
	            }
            	if (action == MSG_ROTATE)
        		{
        			action=MSG_DO_NOTHING;
        			doRotateBlock();
        		}
        		if (action == MSG_MOVE_LEFT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveLeft();
            	
        		}
        		if (action == MSG_MOVE_RIGHT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveRight();
        		}
        		if (action == MSG_MOVE_DOWN)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveDown();
        		}            	
        		if(this.overlay.getOptions().getDifficultyLevel() == Options.DIFFICULTY_EXPERT)
        		{
        			
        			this.doRotatePlayfield(current);
        		}
        		if(this.overlay.getOptions().getDifficultyLevel()==Options.DIFFICULTY_NORMAL)
        		{
        			this.doRotatePlayfieldNormal(current);
        		}
        		
        		gl.glLoadIdentity();
            	
            	
            	//gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        		mMoon.setPosition(xoff+13,yoff-22, zoff-50);
        		
        		mMoon.draw(gl,18.0f,18.0f,1.0f);
        		gl.glLoadIdentity();
        		//gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        		mEarth.setPosition(xoff+13,yoff-2, zoff+60);
        		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        		mEarth.draw(gl,8.0f,8.0f,1.0f);
            	gl.glLoadIdentity();
            	mStarfield.draw(gl,0,rangle);        		
        		drawPlayfield(gl);


                drawNextPiece(gl);

        		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        		
        		//gl.glPopMatrix();
                //this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
                //this.drawString(canvas,""+game.getScore(), 10, 34);
                //this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
                //this.drawString(canvas,""+game.getLevel(), 10, 74);
                //this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
                //this.drawString(canvas,""+game.getLines(),10,114);
        		this.overlay.setLevel(this.overlay.getOptions().getGame().getLevelName());
        		this.overlay.setScore(""+this.overlay.getOptions().getGame().getScore());
        		this.overlay.setLines(""+this.overlay.getOptions().getGame().getLines());
	            if(this.gametype==Game.GAME_MONOLITH)
	            {
	            	
	            	this.overlay.setEnergy(""+game.getEnergy());
	            	//this.drawString(canvas,res.getString(R.string.s_energy),10,134);
	            	//this.drawString(canvas,""+game.getEnergy(),10,154);

	            }
	            
	            //canvas.drawText("zx="+zx+" zy="+zy,10,134,paint);


	            
	            Cube c = this.mCube[0];
	            c.setPosition(0.0f, 0.0f, -30.0f);
	            mAngle += 1.2f;
	            
	            now = SystemClock.uptimeMillis();
	            if(game.getStatus()==SimpleGameData.STATUS_PLAYING)
	            {
	            	this.sayEvolving = true;
	            }
	            long deltatime = now-lastcalltime;
	            timeaccumulator+=deltatime;
	            long simsteps = timeaccumulator/game.getTimer();
	            long remainder = timeaccumulator%game.getTimer();
	            int blockoffset = 0;
            	if(game.getStatus()==SimpleGameData.STATUS_PLAYING || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
            	{
            		if(simsteps>0)
            		{
            			for(long i=0;i<simsteps;i++)
            			{
            				
            				game.gameLoop();
            				if(game.isBlockPlaced())
            				{
                		//android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_PLACE_BLOCK);
                		//message.sendToTarget();
            					if(this.overlay.getOptions().isSoundEnabled())
            					{
            						this.soundManager.playSound(R.raw.place);
            					}
            				}
            				game.flagCompletedLines();
            				this.createExplosions(game);
	            		
            			}
            			timeaccumulator=remainder;
            		}
            		blockoffset=((int)remainder*10)/game.getTimer();
            		
	            	
            	}
            	//lastcalltime = now;
            	if(this.game.getStatus()==SimpleGameData.STATUS_EVOLVING)
            	{
            		drawFallingBlock(gl,0);
            	}
            	else
            	{
            		drawFallingBlock(gl,blockoffset);
            	}
            	
                if(game.getStatus()==SimpleGameData.STATUS_EVOLVING)
                {

                	

                	
                	
                	
                	int result=10;
                	long now = SystemClock.uptimeMillis();;
                	if(now-lastcalltime>game.getTimer())
                	{
                		result=10;
                	}
                	else
                	{
                		result = (int)((now-lastcalltime)%game.getTimer());
                		result = (result*10)/game.getTimer();
                	}
            	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);

            		this.drawBlocks(gl,game,result,10);
                }
                else
                {
                 	drawBlocks(gl);
                }
                lastcalltime = now;
            	if(game.getStatus()==SimpleGameData.STATUS_PLAYING || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
	            {
	            	this.drawCubeExplosion(gl);
	            	this.drawRingExplosions(gl);
	            	
	            }		            

                
	            //game.gameLoop();
        
        	break;
        	}
            
        	gl.glPopMatrix();
    }


    // ------------------------------------------------------------------------


    
    //private static final int INVALIDATE = 1;


    
	public static final int VIEW_INTRO=0;
	public static final int VIEW_GAME=1;
	public static final int VIEW_OPTIONS=2;
	public static final int MSG_DO_NOTHING=-1;
	public static final int MSG_ROTATE=0;
	public static final int MSG_MOVE_LEFT=1;
	public static final int MSG_MOVE_RIGHT=2;
	public static final int MSG_MOVE_DOWN=3;
	public static final int MSG_ROTATE_PLAYFIELD=4;
    public Game	game;
    public Game demogame;
    
    private Starfield		mStarfield;
    private Cube[]          mCube;
    private Cube 			mPlayfieldCube;
    private Square			mMoon;
    private Square			mEarth;
    private Square			mExplosionRing;
    private float           mAngle;
    

    private float			rangle;
    private boolean			highscoreEntry;
    //private int xval;
    //private int yval;
    public float zx=0.0f;
    public float zy=0.0f;
    public float xy=0.0f;
    private float xoff;
    private float yoff;
    private float zoff;
    private long now;
    private long lastcalltime;
    private float testangle;
    
    
    
    
    
    public boolean running;
    //private Resources res;
    int gametype;
    private int viewType;
    //private javax.sound.midi.AndroidMIDIPlayBackEngine soundEngine;
    //private android.media.MediaPlayer mediaPlayer;
    public String message;
    private java.util.LinkedList<ExplodingCube> explodingCubes;
    private java.util.LinkedList<ExplodingRing> explodingRings;
    public static final float Y_ACCELERATION=-0.3f;
    public static final float Z_ACCELERATION=0.3f;
    public static final int MAX_EXPLOSION_FRAME=100;
    public int mWidth;
    public int mHeight;
    public boolean mSizeChanged = true;
    private long lastdrawtime;
    private java.util.Random randomgen;
    
    private boolean backgroundInitialized;
    
    private GameOverlay overlay;
    public GameOverlay getGameOverlay()
    {
    	return this.overlay;
    }
    
    public int action;
    //private SoundSystem soundSystem;
    private Sound soundManager;
    private android.content.Context context;
    public void setAction(int action,int arg1, int arg2)
    {
    	this.action = action;
    	//Log.d("action", "action is ="+action);
    			
    	if(action==MSG_ROTATE_PLAYFIELD)
    	{
    		switch(getGameOverlay().getOptions().getDifficultyLevel())
    		{
    		case Options.DIFFICULTY_EXPERT:
    			break;
    		case Options.DIFFICULTY_NORMAL:
    			break;
    			default:
    			doRotatePlayfield(arg1,arg2);	
    			
    		}
    	}
    }

    public void initLinearInterpolators()
    {
    	linearInterpolators = new LinearInterpolator[2][3][10];
    	for(int a=0;a<2;a++)
    	{
    		for(int b=0;b<3;b++)
    		{
    			for(int c=0;c<10;c++)
    			{
    				linearInterpolators[a][b][c] = new LinearInterpolator();
    			}
    		}
    	}
    	

    	linearInterpolators[0][0][0].addValue(0, 0);        //level 1 Normal
    	linearInterpolators[0][0][0].addValue(0, 60000);
    	linearInterpolators[0][1][0].addValue(0,0);
    	linearInterpolators[0][1][0].addValue(60, 30000);
    	linearInterpolators[0][1][0].addValue(0, 60000);
    	linearInterpolators[0][2][0].addValue(0, 0);
    	linearInterpolators[0][2][0].addValue(0, 60000);

    	
    	linearInterpolators[0][0][1].addValue(0, 0);         //level 2 Normal 
    	linearInterpolators[0][0][1].addValue(60, 15000);
    	linearInterpolators[0][0][1].addValue(0, 30000);
    	linearInterpolators[0][0][1].addValue(-60,45000);
    	linearInterpolators[0][0][1].addValue(0,60000);
    	linearInterpolators[0][1][1].addValue(0, 0);
    	linearInterpolators[0][1][1].addValue(0, 60000);
    	linearInterpolators[0][2][1].addValue(0, 0);
    	linearInterpolators[0][2][1].addValue(0, 60000);

    	
    	linearInterpolators[0][0][2].addValue(0, 0);        //level 3 Normal
    	linearInterpolators[0][0][2].addValue(0, 60000);
    	linearInterpolators[0][1][2].addValue(0,0);
    	linearInterpolators[0][1][2].addValue(0, 60000);
    	linearInterpolators[0][2][2].addValue(0, 0);
    	linearInterpolators[0][2][2].addValue(30, 15000);
    	linearInterpolators[0][2][2].addValue(0, 30000);
    	linearInterpolators[0][2][2].addValue(-30, 45000);
    	linearInterpolators[0][2][2].addValue(0, 60000);
    	
    	linearInterpolators[0][0][3].addValue(0, 0);        //level 4 Normal
    	linearInterpolators[0][0][3].addValue(0, 60000);
    	linearInterpolators[0][1][3].addValue(0,0);
    	linearInterpolators[0][1][3].addValue(60, 15000);
    	linearInterpolators[0][1][3].addValue(0, 30000);
    	linearInterpolators[0][1][3].addValue(-60, 45000);
    	linearInterpolators[0][1][3].addValue(0, 60000);
    	linearInterpolators[0][2][3].addValue(0, 0);
    	linearInterpolators[0][2][3].addValue(0, 60000);
    	
    	linearInterpolators[0][0][4].addValue(0, 0);         //level 5 Normal 
    	linearInterpolators[0][0][4].addValue(90, 15000);
    	linearInterpolators[0][0][4].addValue(0,30000);
    	linearInterpolators[0][0][4].addValue(-90,45000);
    	linearInterpolators[0][0][4].addValue(0,60000);
    	linearInterpolators[0][1][4].addValue(0, 0);
    	linearInterpolators[0][1][4].addValue(0, 60000);
    	linearInterpolators[0][2][4].addValue(0, 0);
    	linearInterpolators[0][2][4].addValue(0, 60000);
    	
    	linearInterpolators[0][0][5].addValue(0, 0);         //level 6 Normal 
    	linearInterpolators[0][0][5].addValue(60, 15000);
    	linearInterpolators[0][0][5].addValue(0,30000);
    	linearInterpolators[0][0][5].addValue(-60,45000);
    	linearInterpolators[0][0][5].addValue(0,60000);
    	linearInterpolators[0][1][5].addValue(0,0);
    	linearInterpolators[0][1][5].addValue(30, 15000);
    	linearInterpolators[0][1][5].addValue(0, 30000);
    	linearInterpolators[0][1][5].addValue(-30, 45000);
    	linearInterpolators[0][1][5].addValue(0, 60000);
    	linearInterpolators[0][2][5].addValue(0, 0);
    	linearInterpolators[0][2][5].addValue(0, 60000);
    	
    	linearInterpolators[0][0][6].addValue(0, 0);         //level 7 Normal 
    	linearInterpolators[0][0][6].addValue(60, 15000);
    	linearInterpolators[0][0][6].addValue(0,30000);
    	linearInterpolators[0][0][6].addValue(-60,45000);
    	linearInterpolators[0][0][6].addValue(0,60000);
    	linearInterpolators[0][1][6].addValue(0,0);
    	linearInterpolators[0][1][6].addValue(60, 15000);
    	linearInterpolators[0][1][6].addValue(0, 30000);
    	linearInterpolators[0][1][6].addValue(-60, 45000);
    	linearInterpolators[0][1][6].addValue(0, 60000);
    	linearInterpolators[0][2][6].addValue(0, 0);
    	linearInterpolators[0][2][6].addValue(0, 60000);
    	
    	linearInterpolators[0][0][7].addValue(0, 0);         //level 8 Normal 
    	linearInterpolators[0][0][7].addValue(60, 15000);
    	linearInterpolators[0][0][7].addValue(0,30000);
    	linearInterpolators[0][0][7].addValue(-60,45000);
    	linearInterpolators[0][0][7].addValue(0,60000);
    	linearInterpolators[0][1][7].addValue(0,0);
    	linearInterpolators[0][1][7].addValue(60, 15000);
    	linearInterpolators[0][1][7].addValue(0, 30000);
    	linearInterpolators[0][1][7].addValue(-60, 45000);
    	linearInterpolators[0][1][7].addValue(0, 60000);
    	linearInterpolators[0][2][7].addValue(0, 0);
    	linearInterpolators[0][2][7].addValue(15, 30000);
    	linearInterpolators[0][2][7].addValue(0, 60000);
    	
    	linearInterpolators[0][0][8].addValue(0, 0);         //level 9 Normal 
    	linearInterpolators[0][0][8].addValue(60, 15000);
    	linearInterpolators[0][0][8].addValue(0,30000);
    	linearInterpolators[0][0][8].addValue(-60,45000);
    	linearInterpolators[0][0][8].addValue(0,60000);
    	linearInterpolators[0][1][8].addValue(0,0);
    	linearInterpolators[0][1][8].addValue(60, 15000);
    	linearInterpolators[0][1][8].addValue(0, 30000);
    	linearInterpolators[0][1][8].addValue(-60, 45000);
    	linearInterpolators[0][1][8].addValue(0, 60000);
    	linearInterpolators[0][2][8].addValue(0, 0);
    	linearInterpolators[0][2][8].addValue(15, 30000);
    	linearInterpolators[0][2][8].addValue(0, 60000);

    	linearInterpolators[0][0][9].addValue(0, 0);         //level 10 Normal 
    	linearInterpolators[0][0][9].addValue(-60, 15000);
    	linearInterpolators[0][0][9].addValue(0,30000);
    	linearInterpolators[0][0][9].addValue(60,45000);
    	linearInterpolators[0][0][9].addValue(0,60000);
    	linearInterpolators[0][1][9].addValue(0,0);
    	linearInterpolators[0][1][9].addValue(-60, 15000);
    	linearInterpolators[0][1][9].addValue(0, 30000);
    	linearInterpolators[0][1][9].addValue(60, 45000);
    	linearInterpolators[0][1][9].addValue(0, 60000);
    	linearInterpolators[0][2][9].addValue(0, 0);
    	linearInterpolators[0][2][9].addValue(15, 30000);
    	linearInterpolators[0][2][9].addValue(0, 60000);
    	
    	
    	
    	linearInterpolators[1][0][0].addValue(0, 0);        //level 1 Expert
    	linearInterpolators[1][0][0].addValue(0, 60000);
    	linearInterpolators[1][1][0].addValue(0,0);
    	linearInterpolators[1][1][0].addValue(180, 30000);
    	linearInterpolators[1][1][0].addValue(0, 60000);
    	linearInterpolators[1][2][0].addValue(0, 0);
    	linearInterpolators[1][2][0].addValue(0, 60000);

    	
    	linearInterpolators[1][0][1].addValue(0, 0);         //level 2 Expert 
    	linearInterpolators[1][0][1].addValue(180, 15000);
    	linearInterpolators[1][0][1].addValue(0, 30000);
    	linearInterpolators[1][0][1].addValue(-180,45000);
    	linearInterpolators[1][0][1].addValue(0,60000);
    	linearInterpolators[1][1][1].addValue(0, 0);
    	linearInterpolators[1][1][1].addValue(0, 60000);
    	linearInterpolators[1][2][1].addValue(0, 0);
    	linearInterpolators[1][2][1].addValue(0, 60000);

    	
    	linearInterpolators[1][0][2].addValue(0, 0);        //level 3 Expert
    	linearInterpolators[1][0][2].addValue(0, 60000);
    	linearInterpolators[1][1][2].addValue(0,0);
    	linearInterpolators[1][1][2].addValue(0, 60000);
    	linearInterpolators[1][2][2].addValue(0, 0);
    	linearInterpolators[1][2][2].addValue(180, 15000);
    	linearInterpolators[1][2][2].addValue(0, 30000);
    	linearInterpolators[1][2][2].addValue(-180, 45000);
    	linearInterpolators[1][2][2].addValue(0, 60000);
    	
    	linearInterpolators[1][0][3].addValue(0, 0);        //level 4 Expert
    	linearInterpolators[1][0][3].addValue(0, 60000);
    	linearInterpolators[1][1][3].addValue(0,0);
    	linearInterpolators[1][1][3].addValue(180, 15000);
    	linearInterpolators[1][1][3].addValue(0, 30000);
    	linearInterpolators[1][1][3].addValue(-180, 45000);
    	linearInterpolators[1][1][3].addValue(0, 60000);
    	linearInterpolators[1][2][3].addValue(0, 0);
    	linearInterpolators[1][2][3].addValue(0, 60000);
    	
    	linearInterpolators[1][0][4].addValue(0, 0);         //level 5 Expert 
    	linearInterpolators[1][0][4].addValue(360, 15000);
    	linearInterpolators[1][0][4].addValue(0,30000);
    	linearInterpolators[1][0][4].addValue(-360,45000);
    	linearInterpolators[1][0][4].addValue(0,60000);
    	linearInterpolators[1][1][4].addValue(0, 0);
    	linearInterpolators[1][1][4].addValue(0, 60000);
    	linearInterpolators[1][2][4].addValue(0, 0);
    	linearInterpolators[1][2][4].addValue(0, 60000);
    
    	linearInterpolators[1][0][5].addValue(0, 0);         //level 6 Expert 
    	linearInterpolators[1][0][5].addValue(180, 15000);
    	linearInterpolators[1][0][5].addValue(0,30000);
    	linearInterpolators[1][0][5].addValue(-180,45000);
    	linearInterpolators[1][0][5].addValue(0,60000);
    	linearInterpolators[1][1][5].addValue(0,0);
    	linearInterpolators[1][1][5].addValue(30, 15000);
    	linearInterpolators[1][1][5].addValue(0, 30000);
    	linearInterpolators[1][1][5].addValue(-30, 45000);
    	linearInterpolators[1][1][5].addValue(0, 60000);
    	linearInterpolators[1][2][5].addValue(0, 0);
    	linearInterpolators[1][2][5].addValue(0, 60000);
    	
    	linearInterpolators[1][0][6].addValue(0, 0);         //level 7 Expert 
    	linearInterpolators[1][0][6].addValue(180, 15000);
    	linearInterpolators[1][0][6].addValue(0,30000);
    	linearInterpolators[1][0][6].addValue(-180,45000);
    	linearInterpolators[1][0][6].addValue(0,60000);
    	linearInterpolators[1][1][6].addValue(0,0);
    	linearInterpolators[1][1][6].addValue(180, 15000);
    	linearInterpolators[1][1][6].addValue(0, 30000);
    	linearInterpolators[1][1][6].addValue(-180, 45000);
    	linearInterpolators[1][1][6].addValue(0, 60000);
    	linearInterpolators[1][2][6].addValue(0, 0);
    	linearInterpolators[1][2][6].addValue(0, 60000);
    	
    	linearInterpolators[1][0][7].addValue(0, 0);         //level 8 Expert 
    	linearInterpolators[1][0][7].addValue(180, 15000);
    	linearInterpolators[1][0][7].addValue(0,30000);
    	linearInterpolators[1][0][7].addValue(-180,45000);
    	linearInterpolators[1][0][7].addValue(0,60000);
    	linearInterpolators[1][1][7].addValue(0,0);
    	linearInterpolators[1][1][7].addValue(180, 15000);
    	linearInterpolators[1][1][7].addValue(0, 30000);
    	linearInterpolators[1][1][7].addValue(-180, 45000);
    	linearInterpolators[1][1][7].addValue(0, 60000);
    	linearInterpolators[1][2][7].addValue(0, 0);
    	linearInterpolators[1][2][7].addValue(15, 30000);
    	linearInterpolators[1][2][7].addValue(0, 60000);
    
    	linearInterpolators[1][0][8].addValue(0, 0);         //level 9 Expert 
    	linearInterpolators[1][0][8].addValue(360, 15000);
    	linearInterpolators[1][0][8].addValue(0,30000);
    	linearInterpolators[1][0][8].addValue(-360,45000);
    	linearInterpolators[1][0][8].addValue(0,60000);
    	linearInterpolators[1][1][8].addValue(0,0);
    	linearInterpolators[1][1][8].addValue(180, 15000);
    	linearInterpolators[1][1][8].addValue(0, 30000);
    	linearInterpolators[1][1][8].addValue(-180, 45000);
    	linearInterpolators[1][1][8].addValue(0, 60000);
    	linearInterpolators[1][2][8].addValue(0, 0);
    	linearInterpolators[1][2][8].addValue(15, 30000);
    	linearInterpolators[1][2][8].addValue(0, 60000);

    	linearInterpolators[1][0][9].addValue(0, 0);         //level 10 Expert 
    	linearInterpolators[1][0][9].addValue(-360, 15000);
    	linearInterpolators[1][0][9].addValue(0,30000);
    	linearInterpolators[1][0][9].addValue(360,45000);
    	linearInterpolators[1][0][9].addValue(0,60000);
    	linearInterpolators[1][1][9].addValue(0,0);
    	linearInterpolators[1][1][9].addValue(-360, 15000);
    	linearInterpolators[1][1][9].addValue(0, 30000);
    	linearInterpolators[1][1][9].addValue(360, 45000);
    	linearInterpolators[1][1][9].addValue(0, 60000);
    	linearInterpolators[1][2][9].addValue(0, 0);
    	linearInterpolators[1][2][9].addValue(15, 30000);
    	linearInterpolators[1][2][9].addValue(0, 60000);   	
    	
    	
    	
    	
    	
    	
    	
    	
    }
    private LinearInterpolator[][][] linearInterpolators;
    private boolean sayEvolving;
    private boolean sayGameOver;
    private GLTextures textures;
    private long startGameTime;
    private long timeaccumulator;
    private int w;
    private int h;
    private boolean isPaused;
}
    