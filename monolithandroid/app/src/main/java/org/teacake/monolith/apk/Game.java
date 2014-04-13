package org.teacake.monolith.apk;

public interface Game
{
	public static final int STATUS_STARTUP=0;
	public static final int STATUS_PLAYING=1;
	public static final int STATUS_PAUSE=2;
	public static final int STATUS_GAME_OVER=3;
	public static final int STATUS_EVOLVING=4;
	public static final int GAME_CLASSIC = 0;
	public static final int GAME_MONOLITH = 1;
	public static final int GAME_PUZZLE = 2;
	public void clearGrid();
	public void initGame(int theStartingLevel);
	public void setStatus(int status);
	public int getStatus();
	public void setTimer(int time);
	public int getGridValue(int x, int y);
	public int getPreviousGridValue(int x, int y);
	public void setGridValue(int x, int y, int value);
	public void gameLoop();
	public void moveBlockLeft();
	public void moveBlockRight();
	public boolean moveBlockDown();
	public boolean canMoveBlockDown();
	public void clearCompleteLines();
	public void flagCompletedLines();
	public int getClearedLineCount();
	
	public void rotateCurrentBlockClockwise();
	public void setScore(int score);
	public int getScore();
	public void setLevel(int level);
	public int getLevel();
	public String getLevelName();
	public int[] getLevels();
	public void setLines(int lines);
	public int getLines();
	public void setTimerEnabled(boolean flag);
	public int getEnergy();
	public void setEnergy(int energy);
	public boolean isTimerEnabled();
	public Block getCurrentBlock();
	public Block getNextBlock();
	public int getTimer();
	public int[] getClearedLines();
	public int getCurrentStep();
	public void setStep(int step);
	public boolean isBlockPlaced();
	public int getGameType();
	public void saveGame(android.content.SharedPreferences preferences);
	public void loadGame(android.content.SharedPreferences preferences);
}
