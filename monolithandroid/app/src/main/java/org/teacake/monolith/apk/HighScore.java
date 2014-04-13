package org.teacake.monolith.apk;


public class HighScore
{
	public HighScore(int score, String name, String level)
	{
		this.score = score;
		this.name = name;
		this.level = level;
	}
	private int score;
	public int getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score = score; 
	}
	private String name;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	private String level;
	public String getLevel()
	{
		return this.level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
}
