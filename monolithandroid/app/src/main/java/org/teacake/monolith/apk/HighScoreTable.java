package org.teacake.monolith.apk;
import android.app.*;
import android.content.*;


public class HighScoreTable {
	
	public HighScoreTable(Activity activity, int highScoreTableSize)
	{
		this.highScores = new java.util.Vector<HighScore>();
		this.activity = activity;
		SharedPreferences prefs = this.activity.getPreferences(android.content.Context.MODE_PRIVATE);
		
		this.highScoreTableSize = highScoreTableSize;
		this.editor = prefs.edit();
		for(int i=0;i<highScoreTableSize;i++)
		{
	
			int score = prefs.getInt("score"+i, 0);
			String name = prefs.getString("name"+i, "teacake");
			String level = prefs.getString("level"+i, "1");
			HighScore hs = new HighScore(score,name,level);
			this.highScores.add(hs);
		}
	}
	public boolean isHighScore(int score, String name, String level)
	{
		
		for(int i=0;i<highScores.size();i++)
		{
			if(highScores.get(i).getScore()<score)
			{
				for(int j=highScores.size()-1;j>i;j--)
				{
					highScores.set(j, highScores.get(j-1));
				}
				highScores.set(i, new HighScore(score,name,level));
				return true;
			}
		}
		return false;
	}
	public boolean isHighScore(int score)
	{
		for(int i=0;i<highScores.size();i++)
		{
			if(highScores.get(i).getScore()<score)
			{
				
				return true;
			}
		}
		return false;
		
	}
	public void saveHighScores()
	{
		for(int i=0;i<highScoreTableSize;i++)
		{
	
			editor.putInt("score"+i, highScores.get(i).getScore());
			editor.putString("name"+i, highScores.get(i).getName());
			editor.putString("level"+i, highScores.get(i).getLevel());
		}
		editor.commit();
		
	}
	public int getHighScoreCount()
	{
		return this.highScoreTableSize;
	}
	public HighScore getHighScore(int highscorepos)
	{
		return highScores.get(highscorepos);
	}
	private android.app.Activity activity;
	private java.util.Vector<HighScore> highScores;
	private int highScoreTableSize;
	private SharedPreferences.Editor editor;

}
