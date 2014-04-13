package org.teacake.monolith.apk;

import android.view.View;
import android.content.Context;
import android.graphics.*;

import android.content.res.Resources;;
public class GameOverlay extends View {
	public GameOverlay(Context context, HighScoreTable table, Options options)
	{
		
		super(context);
		this.options = options;
		this.hsTable = table;
		this.overlayType = OVERLAY_TYPE_GAME_MONOLITH;
		this.drawType = DRAW_NORMAL;
		res = context.getResources();
		curtainPaint = new Paint();
		curtainPaint.setARGB(255, 0, 0, 0);
		gameOverPaint = new Paint();
        gameOverPaint.setARGB(255, 255, 220, 60);
        gameOverPaint.setTextSize(36);
        gameOverPaint2 = new Paint();
        gameOverPaint2.setARGB(255,255,30,30);
        gameOverPaint2.setTextSize(36);
        statusTextPaint1 = new Paint();
        statusTextPaint2 = new Paint();
        statusTextPaint1.setARGB(200, 255, 0, 0);
        statusTextPaint1.setTextSize(14);
        statusTextPaint2.setTextSize(14);
        statusTextPaint2.setARGB(255, 128, 128, 128);
        optionsPaint = new Paint();
        optionsPaint.setARGB(255, 255, 10, 10);
        optionsPaint.setTextSize(22);
        selectedOptionsPaint = new Paint();
        selectedOptionsPaint.setARGB(255, 255, 240, 230);
        selectedOptionsPaint.setTextSize(22);
        hsPaint = new Paint();
        hsPaint.setTextSize(16);
        hsPaint.setARGB(255, 255, 220, 60);
        score="0";
        level="1";
        hiscore="0";
        lines="0";
        energy="0";
        message="monolith android";
        goalpha=0;
        direction=8;
        //this.curtain = 0;
        this.lastDrawTime = System.currentTimeMillis();
        this.nameEntryLength = 9;
        this.leftarrow = android.graphics.BitmapFactory.decodeResource(res, R.drawable.leftarrow);
        this.rightarrow = android.graphics.BitmapFactory.decodeResource(res, R.drawable.rightarrow);
		
		
		timer = System.currentTimeMillis();
		
		
	}
	
	@Override protected void onDraw(Canvas canvas)
	{
		
		timer = System.currentTimeMillis();
		int timeindex = this.currentTextColor%5000;
		
		goalpha=goalpha+direction;
		if(goalpha>255)
		{
			direction=-8;
			goalpha=goalpha+direction;
		}
		if(goalpha<16)
		{
			direction=8;
			goalpha=goalpha+direction;
		}
		
		if(overlayType==OVERLAY_TYPE_INTRO)
		{
			if(timeindex<1500)
			{
				goalpha = (timeindex*255)/1500;
			}
			else if(timeindex>3500)
			{
				goalpha = 255-((timeindex-3500)*255)/1500;
			}
			else
			{
				goalpha = 255;
			}
			
		}
		this.gameOverPaint.setAlpha(goalpha);
		this.hsPaint.setAlpha(goalpha);
        this.gameOverXPos = getTextWidth(res.getString( R.string.s_game_over),gameOverPaint);
        this.evolvingXPos = getTextWidth(res.getString( R.string.s_evolving ),gameOverPaint);
        drawCurtain(canvas);
		
        switch (overlayType)
		{
		case OVERLAY_TYPE_INTRO:
			drawIntroOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_CLASSIC:
			drawClassicGameOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_MONOLITH:
			drawMonolithGameOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_PUZZLE:
			drawPuzzleGameOverlay(canvas);
			break;
		case OVERLAY_TYPE_OPTIONS:
			drawOptionsOverlay(canvas);
			break;
		
		}
        if(this.drawType == DRAW_NAME_ENTRY)
        {
        	drawNameEntry(canvas);
        }
        
	}
	
    public int getTextWidth(String str,Paint paint)
    {
    	float widths[] =new float[str.length()];
    	paint.getTextWidths(str,widths); 
    	float totalwidth=0;
    	for(int i=0;i<widths.length;i++)
    	{
    		totalwidth+=widths[i];
    	}
    	return (int)totalwidth;
    }
    
	private void drawIntroOverlay(Canvas canvas)
	{
		Long now = System.currentTimeMillis();
		
		String logo=res.getString(R.string.app_name);
		
		int index = (int)((now-this.lastDrawTime)%35000);
		
		this.currentTextColor = index;
		if (index>=0 && index<10000)
		{
			logo = res.getString(R.string.app_name);
		}
		if(index>=10000 && index<15000)
		{
			logo = res.getString(R.string.s_authorname);
		}
		if(index>=15000 && index<20000)
		{
			logo = res.getString(R.string.s_website);
		}
		if(index>=20000 && index<25000)
		{
			logo = res.getString(R.string.s_press_menu_to_play);
		}
		if(index>=25000 && index<35000)
		{
			
			int highScoresHeight = 12*18;
			int basey=(canvas.getHeight()-highScoresHeight)/2;
			int highScoresWidth = this.getTextWidth(res.getString(R.string.s_highscores), hsPaint);
			int xoffset = (canvas.getWidth()-248)/2;
			canvas.drawText(res.getString(R.string.s_highscores), (canvas.getWidth()-highScoresWidth)/2, basey,hsPaint);
			canvas.drawText(res.getString(R.string.s_playername), xoffset+32, basey+18, hsPaint);
			canvas.drawText(res.getString(R.string.s_score), xoffset+150, basey+18, hsPaint);
			canvas.drawText(res.getString(R.string.s_level), xoffset+230, basey+18, hsPaint);
			
			
			for(int i=0;i<hsTable.getHighScoreCount();i++)
			{
				String number = ""+(i+1)+".";
				while(number.length()<3)
				{
					number = " "+number;
				}
				String score = "" +hsTable.getHighScore(i).getScore();
				while(score.length()<8)
				{
					score = "0"+score;
				}
				String name = "" +hsTable.getHighScore(i).getName();
				while(name.length()<3)
				{
					name = " "+name;
				}
				String level = "" + hsTable.getHighScore(i).getLevel();
				canvas.drawText(number, xoffset, basey+2*18+i*18, hsPaint);
				canvas.drawText(name, xoffset+32, basey+2*18+i*18, hsPaint);
				canvas.drawText(score, xoffset+150, basey+2*18+i*18, hsPaint);
				canvas.drawText(level,xoffset+240,basey+2*18+i*18,hsPaint);
				
			}
			
		}
		else
		{
			int textxpos = this.getTextWidth(logo,this.gameOverPaint);
			canvas.drawText(logo, (canvas.getWidth()-textxpos)/2, (canvas.getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
		}
		
	}
    public void drawString(Canvas canvas,String str,int x,int y)
    {
    	canvas.drawText(str, x+1, y+1, statusTextPaint2);
    	canvas.drawText(str, x, y, statusTextPaint1);
    }
    public void drawCurtain(Canvas canvas)
    {
    	long now = System.currentTimeMillis();
    	int width = canvas.getWidth();
    	int height = canvas.getHeight();
    	float aperture = 1.0f;
    	if(now-this.lastDrawTime<5000)
    	{
    		canvas.drawRect(0,0, width,height, curtainPaint);
    		
    	}
    	else if(now-this.lastDrawTime>=5000 && now-this.lastDrawTime<10000)
    	{
    		aperture = ((float)(now-this.lastDrawTime-5000))/5000.0f;
    		canvas.drawRect(0,0,width,(height/2)-(height/2)*aperture,curtainPaint);
    		canvas.drawRect(0,(height/2)+(height/2)*aperture,width,height, curtainPaint);
    		//canvas.drawRect(0,height/2,width,(height/2)*aperture,curtainPaint);
    	}
    	
    }
    
    //private void drawOptionText(Canvas canvas,int x, int y,String text, Paint thePaint)
    //{
    //	
    //	canvas.drawBitmap(leftarrow,null,new Rect(x,y,x+leftarrow.getWidth(),y+leftarrow.getHeight()),thePaint);
    //	int theWidth = this.getTextWidth(text, thePaint);
    //	canvas.drawText(text, x+leftarrow.getWidth(), y+leftarrow.getHeight(), thePaint);
    //	canvas.drawBitmap(rightarrow, null, new Rect(x+leftarrow.getWidth()+theWidth,y,x+leftarrow.getWidth()+theWidth+rightarrow.getWidth(),y+rightarrow.getHeight()),thePaint);
    //}
    private void drawCenteredText(Canvas canvas, int y, String text, Paint paint)
    {
    	int theWidth = this.getTextWidth(text, paint);
    	canvas.drawText(text, (canvas.getWidth()-theWidth)/2, y, paint);
    }
    //private void drawCenteredOptionText(Canvas canvas, int y, String text, Paint thePaint)
    //{
    //	int theWidth = this.getTextWidth(text, thePaint);
    //	int theFullWidth = theWidth+this.leftarrow.getWidth()+this.rightarrow.getWidth();
    //	int x = (canvas.getWidth()-theFullWidth)/2;
    //	canvas.drawBitmap(leftarrow,null,new Rect(x,y,x+leftarrow.getWidth(),y+leftarrow.getHeight()),thePaint);
    //	
    //	canvas.drawText(text, x+leftarrow.getWidth(), y+leftarrow.getHeight(), thePaint);
    //	canvas.drawBitmap(rightarrow, null, new Rect(x+leftarrow.getWidth()+theWidth,y,x+leftarrow.getWidth()+theWidth+rightarrow.getWidth(),y+rightarrow.getHeight()),thePaint);
    //	
    //}
    private void drawCenteredOptionText(Canvas canvas, int y, String text, Paint thePaint,boolean leftArrow,boolean rightArrow,boolean animate)
    {
    	
    	int offset = (int)(this.timer%1000)/100;
    	if(!animate)
    	{
    		offset=0;
    	}
    	else
    	{
    		if(offset>5)
    		{
    			offset=10-offset;
    		}
    	}
    	int theWidth = this.getTextWidth(text, thePaint);
    	int theFullWidth = theWidth+this.leftarrow.getWidth()+this.rightarrow.getWidth();
    	int x = (canvas.getWidth()-theFullWidth)/2;
    	if(leftArrow) canvas.drawBitmap(leftarrow,null,new Rect(x-offset,y,x+leftarrow.getWidth()-offset,y+leftarrow.getHeight()),thePaint);
    	canvas.drawText(text, x+leftarrow.getWidth(), y+leftarrow.getHeight(), thePaint);
    	if(rightArrow) canvas.drawBitmap(rightarrow, null, new Rect(x+leftarrow.getWidth()+theWidth+offset,y,x+leftarrow.getWidth()+theWidth+rightarrow.getWidth()+offset,y+rightarrow.getHeight()),thePaint);
    	
    }
    //private void drawTextSelector(Canvas canvas, int y, String title, String value, Paint titlePaint,Paint valuePaint)
    //{
    //	int theWidth = this.getTextWidth(title, titlePaint);
    //	canvas.drawText(title, (canvas.getWidth()-theWidth)/2, y, titlePaint);
    //	drawCenteredOptionText(canvas,y+((int)titlePaint.getTextSize())/2+1,value,valuePaint);
    //}
    private void drawTextSelector(Canvas canvas, int y, String title, String value, Paint titlePaint, Paint valuePaint, boolean leftArrow, boolean rightArrow,boolean animate)
    {
    	int theWidth = this.getTextWidth(title, titlePaint);
    	canvas.drawText(title, (canvas.getWidth()-theWidth)/2, y, titlePaint);
    	drawCenteredOptionText(canvas,y+((int)titlePaint.getTextSize())/2+1,value,valuePaint, leftArrow, rightArrow, animate);
    	
    }
    private void drawOptionsOverlay(Canvas canvas)
    {
    	int transitionMilliseconds = 1500;
    	int firstEntry = canvas.getHeight()/3;
    	int widgetdistance=80;
    	
    	
    	boolean[] animate = {false,false,false,false,false,false,false};
    	int[] coordinates = {0,0,0,0,0,0,0};
    	Paint[] p = new Paint[coordinates.length];
    	

    	int interpolatedOffset = (int)(options.interpolatePosition(transitionMilliseconds)*widgetdistance);
    	
    	
    	coordinates[options.getCurrentSelectedOption()]=firstEntry-interpolatedOffset;
    	for(int i=options.getCurrentSelectedOption()+1;i<coordinates.length;i++)
    	{
    		coordinates[i]=firstEntry+(i-options.getCurrentSelectedOption())*widgetdistance-interpolatedOffset;
    		
    	}
    	for(int i=1;i<=options.getCurrentSelectedOption();i++)
    	{
    		coordinates[options.getCurrentSelectedOption()-i]=firstEntry-i*widgetdistance-interpolatedOffset;
    	}
    	
    	animate[options.getCurrentSelectedOption()]=true;
    	p[options.getCurrentSelectedOption()]=this.selectedOptionsPaint;
    	for(int i=0;i<coordinates.length;i++)
    	{
    		if(i==options.getCurrentSelectedOption())
    		{
    			p[i]=this.selectedOptionsPaint;
    			continue;
    		}
    		Paint newp = new Paint();
    		
    		newp.setARGB(255, 255, 10, 10);
    		newp.setTextSize(22);
            p[i]=newp;
            int distance=0;
            if(coordinates[i]>firstEntry)
            {
            	distance = coordinates[i]-firstEntry;
            }
            else
            {
            	distance = firstEntry-coordinates[i];
            }
            float alpharatio = 0.0f;
            if(distance<canvas.getHeight())
            {
            	alpharatio=1.0f-((float)distance*2)/(float)canvas.getHeight();
            }
            

            p[i].setAlpha((int)(alpharatio*255));
            
    	}
    	p[options.getCurrentSelectedOption()].setAlpha(255);

    	drawCenteredText(canvas, 40, res.getString(R.string.s_options),this.gameOverPaint);
    	drawTextSelector(canvas, coordinates[Options.OPTION_BACK], "",res.getString(R.string.s_back),p[Options.OPTION_BACK],p[Options.OPTION_BACK],animate[Options.OPTION_BACK],false,animate[Options.OPTION_BACK]);
    	
    	String gametype="";
    	switch(options.getGameType())
    	{
    	case Game.GAME_CLASSIC:
    		gametype = res.getString(R.string.s_classicgame);
    		break;
    		
    	case Game.GAME_MONOLITH:
    		gametype = res.getString(R.string.s_monolithgame);
    		break;
    	
    	}
    	String gameDifficulty = ""; 
    	drawTextSelector(canvas, coordinates[Options.OPTION_GAMETYPE], res.getString(R.string.s_gametype),gametype,p[Options.OPTION_GAMETYPE],p[Options.OPTION_GAMETYPE],animate[Options.OPTION_GAMETYPE],animate[Options.OPTION_GAMETYPE],animate[Options.OPTION_GAMETYPE]);
    	
    	switch(options.getDifficultyLevel())
    	{
    	case Options.DIFFICULTY_EASY:
    		gameDifficulty = res.getString(R.string.s_difficulty_easy);
    		break;
    	case Options.DIFFICULTY_NORMAL:
    		gameDifficulty = res.getString(R.string.s_difficulty_normal);
    		break;
    	case Options.DIFFICULTY_EXPERT:
    		gameDifficulty = res.getString(R.string.s_difficulty_expert);
    		break;
    	}
    	
    	drawTextSelector(canvas, coordinates[Options.OPTION_DIFFICULTY], res.getString(R.string.s_difficulty),gameDifficulty,p[Options.OPTION_DIFFICULTY],p[Options.OPTION_DIFFICULTY],animate[Options.OPTION_DIFFICULTY],animate[Options.OPTION_DIFFICULTY],animate[Options.OPTION_DIFFICULTY]);
    	String currentLevel = ""+this.options.getStartingLevel();
    	drawTextSelector(canvas,coordinates[Options.OPTION_STARTING_LEVEL], res.getString(R.string.s_starting_level),currentLevel,p[Options.OPTION_STARTING_LEVEL],p[Options.OPTION_STARTING_LEVEL],animate[Options.OPTION_STARTING_LEVEL],animate[Options.OPTION_STARTING_LEVEL],animate[Options.OPTION_STARTING_LEVEL]);
    	String music = options.isMusicEnabled()?res.getString(R.string.s_on):res.getString(R.string.s_off);
    	drawTextSelector(canvas,coordinates[Options.OPTION_MUSIC], res.getString(R.string.s_music),music,p[Options.OPTION_MUSIC],p[Options.OPTION_MUSIC],animate[Options.OPTION_MUSIC],animate[Options.OPTION_MUSIC],animate[Options.OPTION_MUSIC]);
    	String sound = options.isSoundEnabled()?res.getString(R.string.s_on):res.getString(R.string.s_off);
    	drawTextSelector(canvas,coordinates[Options.OPTION_SOUND], res.getString(R.string.s_sound),sound,p[Options.OPTION_SOUND],p[Options.OPTION_SOUND],animate[Options.OPTION_SOUND],animate[Options.OPTION_SOUND],animate[Options.OPTION_SOUND]);
    	
    	
       	drawTextSelector(canvas, coordinates[Options.OPTION_OK],"", res.getString(R.string.s_ok),p[Options.OPTION_OK],p[Options.OPTION_OK],false,animate[Options.OPTION_OK],animate[Options.OPTION_OK]);
        
    }
	private void drawClassicGameOverlay(Canvas canvas)
	{
        this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
        this.drawString(canvas,""+score, 10, 34);
        this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
        this.drawString(canvas,""+level, 10, 74);
        this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
        this.drawString(canvas,""+lines,10,114);
        if(message.equals("Game Over"))
        {
        	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }

    }
	private void drawMonolithGameOverlay(Canvas canvas)
	{
        this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
        this.drawString(canvas,""+score, 10, 34);
        this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
        this.drawString(canvas,""+level, 10, 74);
        this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
        this.drawString(canvas,""+lines,10,114);
    	this.drawString(canvas,res.getString(R.string.s_energy),10,134);
    	this.drawString(canvas,this.energy,10,154);		
        if(message.equals("Game Over"))
        {
        	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }
        if(message.equals("Evolving..."))
        {
        	canvas.drawText(res.getString(R.string.s_evolving), (getWidth()-this.evolvingXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }
	}
	private void drawPuzzleGameOverlay(Canvas canvas)
	{
		
	}
	private void drawNameEntry(Canvas canvas)
	{
		int xw=22;
		for(int i=0;i<this.nameEntry.length();i++)
		{
			String charstr = this.nameEntry.substring(i,i+1);
			if(i!=this.currentCharacterPosition)
			{
				canvas.drawText(charstr,(canvas.getWidth()-8*16)/2+i*xw,canvas.getHeight()/2+40,gameOverPaint);
			}
		}
		String charstr = characters.substring(this.currentCharacter,this.currentCharacter+1);
		if (charstr.equals("<"))
		{
			canvas.drawText("D",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+20,statusTextPaint1);
			canvas.drawText("E",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+40,statusTextPaint1);				
			canvas.drawText("L",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+60,statusTextPaint1);
		}
		else
		if(charstr.equals("@"))
		{
			canvas.drawText("E",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+20,statusTextPaint1);
			canvas.drawText("N",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+40,statusTextPaint1);				
			canvas.drawText("D",(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+60,statusTextPaint1);
		}
		else
		{
			canvas.drawText(charstr,(canvas.getWidth()-8*16)/2+this.currentCharacterPosition*xw,canvas.getHeight()/2+40,gameOverPaint2);
		}
	}
	private String score;
	public boolean moveForward()
	{
		char c = this.characters.charAt(currentCharacter);
		if(c=='@')
		{
			this.hsTable.isHighScore(Integer.parseInt(this.score,10), this.nameEntry, this.level);
			this.hsTable.saveHighScores();
			this.drawType = DRAW_NORMAL;
			return false;
		}
		if(this.currentCharacterPosition<this.nameEntryLength-1)
		{
			
			if(c=='<')
			{
				if(this.currentCharacterPosition>0)
				{
					this.nameEntry=this.nameEntry.substring(0,currentCharacterPosition)+" "+this.nameEntry.substring(currentCharacterPosition+1);
					currentCharacterPosition--;
				}
				return true;

			}
			

			
			this.nameEntry = this.nameEntry.substring(0,currentCharacterPosition)+this.characters.charAt(this.currentCharacter)+this.nameEntry.substring(currentCharacterPosition+1);
			this.currentCharacterPosition++;
			return true;
		}
		return true;
	}

	public void moveBack()
	{
		if(this.currentCharacterPosition>0)
		{
			this.currentCharacterPosition--;
			for(int i=0;i<characters.length();i++)
			{
				if(characters.substring(i,i+1).equals(this.nameEntry.substring(this.currentCharacterPosition, this.currentCharacterPosition+1)));
				{
					this.currentCharacter = i;
					break;
				}
			}
		}
	}
	
	public void selectNextChar()
	{
		currentCharacter++;
		if(this.currentCharacter>characters.length()-1)
		{
			currentCharacter=0;
		}
	}
	
	public void selectPreviousChar()
	{
		currentCharacter--;
		if(this.currentCharacter<0)
		{
			currentCharacter=characters.length()-1;
		}
	}
	
	public void setScore(String score)
	{
		this.score = score;
	}
	
	private String hiscore;
	
	public void setHiscore(String hiscore)
	{
		this.hiscore = hiscore;
	}
	
	private String level;
	
	public void setLevel(String level)
	{
		this.level = level;
	}
	
	private String energy;
	
	public void setEnergy(String energy)
	{
		this.energy = energy;
	}
	
	private String lines;
	
	public void setLines(String lines)
	{
		this.lines = lines;
	}
	
	private String message;
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	private int overlayType;
	
	public void setOverlayType(int overlayType)
	{
		this.overlayType = overlayType;
	}
	
	private int drawType;
	
	public void setDrawType(int drawType)
	{
		if(drawType == DRAW_NAME_ENTRY)
		{
			this.nameEntry = "         ";
			this.currentCharacter = 0;
			this.currentCharacterPosition=0;
		}
		this.drawType = drawType;
	}
	
	String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZ <@";
	
	int currentCharacter=0;
	
	int currentCharacterPosition=0;
	
	//public void setCurtain(int theCurtain)
	//{
	//	this.curtain = theCurtain;
	//}
	
	public HighScoreTable getHighScoreTable()
	{
		return this.hsTable;
	}
	public Options getOptions()
	{
		return this.options;
	}
	
	private Paint curtainPaint;
	private Paint gameOverPaint;
	private Paint gameOverPaint2;
	private Paint statusTextPaint1;
	private Paint statusTextPaint2;
	private Paint hsPaint;
	private Paint optionsPaint;
	private Paint selectedOptionsPaint;
	private Bitmap leftarrow;
	private Bitmap rightarrow;
	private int gameOverXPos;
	private int evolvingXPos;
	private Resources res;
	private int goalpha;
	private int direction;
	//private int curtain;
	private long lastDrawTime;
	private int currentTextColor;
	private HighScoreTable hsTable;
	private String nameEntry;
	private int nameEntryLength;
	private Options options;
	private long timer;
	
	public static final int OVERLAY_TYPE_INTRO = 0;
	public static final int OVERLAY_TYPE_GAME_CLASSIC=1;
	public static final int OVERLAY_TYPE_GAME_MONOLITH=2;
	public static final int OVERLAY_TYPE_GAME_PUZZLE=3;
	public static final int OVERLAY_TYPE_OPTIONS=4;
	
	public static final int DRAW_NORMAL=0;
	public static final int DRAW_NAME_ENTRY=1;
	public static final int DRAW_GAME_OPTIONS=2;
	
}
