package com.whateversoft.colorshafted.database;

import static com.whateversoft.colorshafted.database.ScoreDataHelper.KEY_ARCADE;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.stream.JsonReader;

@SuppressWarnings("serial")
public class ScoreEntry implements Serializable
{
	private long    id;
	private String  name;
	private int		score;
	private int 	level;
	private long	playTime;
	private long  	date;
	private long	gameStyle;
	
	/** default constructor(for Cursor-created entries) */
	public ScoreEntry()
	{}
	
	/** explicitly supplied fields constructor(for explicity entry creation or testing) */
	public ScoreEntry(String name, int score, int level, long playTime, long date, int gameStyle)
	{
		setName(name);
		setScore(score);
		setLevel(level);
		setPlayTime(playTime);
		setDate(date);
		setGameStyle(gameStyle);
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getFormattedScore()
	{
		StringBuffer formattedScore = new StringBuffer(score + "");
		for(int charAt = formattedScore.length(); charAt <= 9; charAt++)
			formattedScore.insert(0, '0');
		return formattedScore.toString();
	}
	
	public int getScore()
	{
		return (int) score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public long getPlayTime()
	{
		return playTime;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public String getFormattedPlayTime()
	{
		int minutes 	   = (int)(playTime / 1000.0 / 60.0);
		int seconds 	   = (int)((playTime - (minutes * 1000 * 60))) / 1000;
		int centis 		   = (int)((playTime - ((minutes * 1000 * 60) + (seconds * 1000)))/10);
		return String.format((minutes > 9 ? "%d:" : "0%d:") + 
							 (seconds > 9 ? "%d:" : "0%d:") +
							 (centis  > 9 ? "%d" : "0%d"),
							 	minutes, seconds, centis);
	}
	

	
	public void setPlayTime(long playTime)
	{
		this.playTime = playTime;
	}
	
	public int getGameStyle()
	{
		return (int) gameStyle;
	}
	
	public void setGameStyle(int gameStyle)
	{
		this.gameStyle = gameStyle;
	}
	
	public long getDate()
	{
		return date;
	}
	
	public String getFormattedDate()
	{
		return new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(date);
	}
	
	public void setDate(long date)
	{
		this.date = date;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public String toString()
	{
		return String.format("%-12s%-15s%-12s%-12s%-14s", 
							 name, getFormattedScore(), getFormattedPlayTime(), 
							 date, (gameStyle == KEY_ARCADE) ? "ARCADE" : "SURVIVAL");
	}
}
