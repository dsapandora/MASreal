package org.teacake.monolith.apk;
import android.content.SharedPreferences;
public class Options 
{
	public Options(Game currentGame, SharedPreferences preferences)
	{
		this.preferences = preferences;
		
		this.setMusicEnabled(true);
		this.setSoundEnabled(true);
		this.game = currentGame;
		this.gameType = preferences.getInt("gameType", Game.GAME_MONOLITH);
		this.difficultyLevel = preferences.getInt("difficultyLevel", Options.DIFFICULTY_NORMAL);
		this.startingLevel = preferences.getInt("startingLevel", 1);
		this.enabledmusic = preferences.getBoolean("enabledmusic", true);
		this.enabledsound = preferences.getBoolean("enabledsound", true);
		
		if(this.gameType==Game.GAME_MONOLITH)
		{
			this.game = new MonolithGameData();
			this.game.initGame(this.startingLevel);
		}
		else
		{
			this.game = new SimpleGameData();
			this.game.initGame(this.startingLevel);
		}
				

		if(game.getGameType()==Game.GAME_CLASSIC)
		{
			this.newGame = new SimpleGameData();
		}
		else
		{
			this.newGame = new MonolithGameData();
		}
		
		this.newGame.setLevel(this.game.getLevels()[0]);

		
		this.currentSelectedOption = 0;//Options.ALLOPTIONS[0];
		this.previousSelectedOption = 0;
		setSelectionStatus(Options.STATUS_SELECTING);
		this.currentSelectedOptionTime = System.currentTimeMillis();
		this.previousSelectedOptionTime = this.currentSelectedOptionTime;
		
		this.changedOption = Options.OPTION_NONE;
		
	}
	public void savePreferences()
	{
		android.content.SharedPreferences.Editor editor= preferences.edit();
		editor.putInt("difficultyLevel", this.difficultyLevel);
		editor.putInt("startingLevel", this.startingLevel);
		editor.putInt("gameType",this.gameType);
		editor.putBoolean("enabledsound", this.enabledsound );
		editor.putBoolean("enabledmusic", this.enabledmusic);
		editor.commit();
	}
	private SharedPreferences preferences;
	private int selectionStatus;
	private boolean enabledmusic;
	private int difficultyLevel;
	public int getSelectionStatus()
	{
		return this.selectionStatus;
	}
	public void setSelectionStatus(int status)
	{
		this.selectionStatus = status;
	}
	public void setDifficultyLevel(int difficultyLevel)
	{
		this.difficultyLevel = difficultyLevel;
	}
	public int getDifficultyLevel()
	{
		return this.difficultyLevel;
	}
	public void setMusicEnabled(boolean flag)
	{
		this.enabledmusic = flag;
	}
	public boolean isMusicEnabled()
	{
		return this.enabledmusic;
	}
	private boolean enabledsound;
	public void setSoundEnabled(boolean flag)
	{
		this.enabledsound = flag;
	}
	public boolean isSoundEnabled()
	{
		return this.enabledsound;
	}
	public void setGame(Game game)
	{
		this.game= game;
	}
	public void setNewGame(Game game)
	{
		this.newGame = game;
	}
	public Game getGame()
	{
		return this.game;
	}
	
	private Game game;
	private Game newGame;
	private int gameType;
	public void setGameType(int gameType)
	{
		this.gameType = gameType;
	}
	public int getGameType()
	{
		return this.gameType;
	}
	private int currentLevelIndex;
	private int startingLevel;
	public void setStartingLevel(int startingLevel)
	{
		this.startingLevel = startingLevel;
	}
	public int getStartingLevel()
	{
		return this.startingLevel;
	}
	public int getFirstLevel()
	{
		return this.newGame.getLevels()[0];
	}
	public void setFirstLevel()
	{
		this.currentLevelIndex=0;
		this.startingLevel = this.newGame.getLevels()[currentLevelIndex];
	}
	public void setNextLevel()
	{
		if(this.currentLevelIndex<this.newGame.getLevels().length-1)
		{
			this.currentLevelIndex++;
			this.startingLevel = this.newGame.getLevels()[this.currentLevelIndex];
		}
		else
		{
			this.currentLevelIndex = 0;
			this.startingLevel = this.newGame.getLevels()[0];
		}
	}
	public void setPreviousLevel()
	{
		if(this.currentLevelIndex>0)
		{
			currentLevelIndex--;
			this.startingLevel = this.newGame.getLevels()[currentLevelIndex];
		}
		else
		{
			this.currentLevelIndex = this.newGame.getLevels().length-1;
			this.startingLevel = this.newGame.getLevels()[this.currentLevelIndex];
		}
	}
	public int getCurrentSelectedOption()
	{
		return this.currentSelectedOption;
	}
	public int getPreviousSelectedOption()
	{
		return this.previousSelectedOption;
	}
	public void nextOption()
	{
		
		this.previousSelectedOption = currentSelectedOption;
		this.previousSelectedOptionTime = currentSelectedOptionTime;
		if(currentSelectedOption<ALLOPTIONS.length-1)
		{
			
			currentSelectedOption++;
		}
		else
		{
		
			currentSelectedOption =0;
		}
		currentSelectedOptionTime = System.currentTimeMillis();
	}
	public void previousOption()
	{
		this.previousSelectedOption = currentSelectedOption;
		this.previousSelectedOptionTime = currentSelectedOptionTime;
		if(currentSelectedOption>0)
		{
			currentSelectedOption--;
		}
		else
		{
			currentSelectedOption = ALLOPTIONS.length-1;
		}
		currentSelectedOptionTime = System.currentTimeMillis();
	}
	public float interpolatePosition(int milliseconds)
	{
		long now = System.currentTimeMillis();
		float retval = 0.0f;
		if(now-this.previousSelectedOptionTime>milliseconds)
		{
			retval = 1.0f;
		}
		else
		{
			retval = ((float)(System.currentTimeMillis()-this.previousSelectedOptionTime))/(float)milliseconds;
		}
		if(this.currentSelectedOption>this.previousSelectedOption)
		{
			return -(1.0f-retval);
		}
		else
		{
			return (1.0f-retval);
		}

	}
	public void setNextValue()
	{
		switch(currentSelectedOption)
		{
		case OPTION_BACK:
			this.changedOption = OPTION_BACK;
			break;
		case OPTION_GAMETYPE:
			if(this.gameType==Game.GAME_MONOLITH)
			{
				gameType=Game.GAME_CLASSIC;
				this.newGame = new SimpleGameData();
			}
			else
			{
				gameType= Game.GAME_MONOLITH;
				this.newGame = new MonolithGameData();
			}
			this.changedOption = OPTION_GAMETYPE;
			break;
		case OPTION_DIFFICULTY:
			switch(this.difficultyLevel)
			{
			case DIFFICULTY_EASY:
				this.difficultyLevel = DIFFICULTY_NORMAL;
				break;
			case DIFFICULTY_NORMAL:
				this.difficultyLevel = DIFFICULTY_EXPERT;
				break;
			case DIFFICULTY_EXPERT:
				this.difficultyLevel = DIFFICULTY_EASY;
				break;
			}
			this.changedOption = OPTION_DIFFICULTY;
			break;
		case OPTION_STARTING_LEVEL:
			this.setNextLevel();
			this.changedOption = OPTION_STARTING_LEVEL;
			break;
		case OPTION_MUSIC:
			this.setMusicEnabled(!this.enabledmusic);
			this.changedOption = OPTION_MUSIC;
			break;
		case OPTION_SOUND:
			this.setSoundEnabled(!this.enabledsound);
			this.changedOption = OPTION_SOUND;
			break;
		case OPTION_OK:
			this.setSelectionStatus(Options.STATUS_OK);
			this.changedOption = OPTION_OK;
			switch(this.getGameType())
			{
				case Game.GAME_CLASSIC:
					game = new SimpleGameData();
					break;
				case Game.GAME_MONOLITH:
					game = new MonolithGameData();
					break;
			}
			
			this.game.setLevel(this.getStartingLevel());
			initNewGame();
			break;
			
		}
	}
	private void initNewGame()
	{
		switch(game.getGameType())
		{
			case Game.GAME_CLASSIC:
				newGame = new SimpleGameData();
				newGame.setLevel(game.getLevel());
			break;
			case Game.GAME_MONOLITH:
				newGame = new MonolithGameData();
				newGame.setLevel(game.getLevel());
			break;
		}
	}
	public void resetOptions()
	{
		this.setSelectionStatus(Options.STATUS_SELECTING);
	}
	public void setPreviousValue()
	{
		switch(currentSelectedOption)
		{
			case OPTION_BACK:
				this.setSelectionStatus(Options.STATUS_BACK);
				this.changedOption = OPTION_BACK;
			break;
			case OPTION_GAMETYPE:
				if(this.gameType==Game.GAME_MONOLITH)
				{
					gameType=Game.GAME_CLASSIC;
					
				}
				else
				{
					gameType=Game.GAME_MONOLITH;
				}
				this.changedOption = OPTION_GAMETYPE;
			break;
			case OPTION_DIFFICULTY:
				switch(this.difficultyLevel)
				{
				case DIFFICULTY_EASY:
					this.difficultyLevel = DIFFICULTY_EXPERT;
					break;
				case DIFFICULTY_NORMAL:
					this.difficultyLevel = DIFFICULTY_EASY;
					break;
				case DIFFICULTY_EXPERT:
					this.difficultyLevel = DIFFICULTY_NORMAL;
					break;
				}
				
				this.changedOption = OPTION_DIFFICULTY;
			break;
			case OPTION_STARTING_LEVEL:
				this.setPreviousLevel();
				this.changedOption = OPTION_STARTING_LEVEL;
			break;
			case OPTION_MUSIC:
				this.setMusicEnabled(!this.enabledmusic);
				this.changedOption = OPTION_MUSIC;
			break;
			case OPTION_SOUND:
				this.setSoundEnabled(!this.enabledsound);
				this.changedOption = OPTION_SOUND;
			break;
			case OPTION_OK:
				this.changedOption = OPTION_NONE;
			break;
			
		}		
	}
	public int getChangedOption()
	{
		int retval = this.changedOption;
		this.changedOption = OPTION_NONE;
		return retval;
	}
	public static final int DIFFICULTY_NORMAL = 0;
	public static final int DIFFICULTY_EXPERT = 1;
	public static final int DIFFICULTY_EASY=2;
	private int changedOption;
	
	private int currentSelectedOption;
	private long currentSelectedOptionTime;
	private int previousSelectedOption;
	private long previousSelectedOptionTime;
	public static final int OPTION_NONE=-1;
	public static final int OPTION_BACK = 0;
	public static final int OPTION_GAMETYPE = 1;
	public static final int OPTION_DIFFICULTY = 2;
	public static final int OPTION_STARTING_LEVEL = 3;
	public static final int OPTION_MUSIC = 4;
	public static final int OPTION_SOUND = 5;
	public static final int OPTION_OK = 6;
	public static final int STATUS_SELECTING =11;
	public static final int STATUS_OK = 10;
	public static final int STATUS_BACK = 12;
	public static int[] ALLOPTIONS = {
										OPTION_BACK,
										OPTION_GAMETYPE,
										OPTION_DIFFICULTY,
										OPTION_STARTING_LEVEL,
										OPTION_MUSIC,
										OPTION_SOUND,
										OPTION_OK
										}; 
}
