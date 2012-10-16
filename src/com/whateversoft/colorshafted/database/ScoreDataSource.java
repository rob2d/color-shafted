package com.whateversoft.colorshafted.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.whateversoft.colorshafted.ColorShafted.GameMode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/** represents a comment database */
public class ScoreDataSource
{
	private SQLiteDatabase database;
	private ScoreDataHelper dbHelper;
	private String[] allColumns = { ScoreDataHelper.COLUMN_ID, 	  ScoreDataHelper.COLUMN_NAME, 
									ScoreDataHelper.COLUMN_SCORE, ScoreDataHelper.COLUMN_LEVEL,
		    						ScoreDataHelper.COLUMN_PLAYTIME, 
								    ScoreDataHelper.COLUMN_DATE,  ScoreDataHelper.COLUMN_STYLE,  };
	
	private final String ORDER_BY_SCORE = ScoreDataHelper.COLUMN_SCORE + " DESC";
	private final String ORDER_BY_SCORE_ASC = ScoreDataHelper.COLUMN_SCORE;

	/** */
	public ScoreDataSource(Context context)
	{
		dbHelper = new ScoreDataHelper(context);
	}
	
	public void open() throws SQLException		//open an instance of writable database
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		database.close();
	}
	
	public ScoreEntry createHighScore(String name, int score, int level, int playTime, long date, GameMode gameMode)
	{
		ContentValues values = new ContentValues();
		
		values.put(ScoreDataHelper.COLUMN_NAME, name);
		values.put(ScoreDataHelper.COLUMN_SCORE, score);
		values.put(ScoreDataHelper.COLUMN_LEVEL, level);
		values.put(ScoreDataHelper.COLUMN_PLAYTIME, playTime);
		values.put(ScoreDataHelper.COLUMN_DATE, date);
		values.put(ScoreDataHelper.COLUMN_STYLE, gameMode == GameMode.ARCADE ? 0 : 1);
		long insertId = database.insert(ScoreDataHelper.TABLE_NAME, null, values);
		Cursor cursor = database.query(ScoreDataHelper.TABLE_NAME, allColumns, ScoreDataHelper.COLUMN_ID + " = " + insertId, 
									   null, null, null, ORDER_BY_SCORE);
		cursor.moveToFirst();
		return cursorToScore(cursor);
	}

	public void deleteComment(ScoreEntry comment)
	{
		long id = comment.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(ScoreDataHelper.TABLE_NAME, ScoreDataHelper.COLUMN_ID + " = " + id, null);
	}
	
	/** retrieve a list of all of the comment entries in the table */
	public List<ScoreEntry> getAllScores()
	{
		List<ScoreEntry> comments = new ArrayList<ScoreEntry>();
		Cursor cursor = database.query(ScoreDataHelper.TABLE_NAME, //create cursor that points to all entries
				allColumns, null, null, null, null, ORDER_BY_SCORE);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ScoreEntry comment = this.cursorToScore(cursor);	//create a comment from all cursor entries on the row
			comments.add(comment);
			cursor.moveToNext();							//move to next
		}
		
		cursor.close();	//release cursor resources
		return comments;
	}
	
	public int findScoreRank(String name, int score, long playTime, GameMode mode)
	{
		int rank = 1;
		//create the cursor/entry iterator based off of the query
		Cursor cursor = database.query(ScoreDataHelper.TABLE_NAME, allColumns, 
									   ScoreDataHelper.COLUMN_STYLE + "=" + (mode == GameMode.ARCADE ? "0" : "1"), 
										null, null, null, ORDER_BY_SCORE);
		Log.d("COLORSHAFTED", "name = " +  name + ", score = " + score + ", playTime = " + playTime + "mode = " + mode);
		cursor.moveToFirst();		//initialize it
		while(!cursor.isAfterLast())//loop while there are still entries to loop through
		{
			ScoreEntry scoreEntry = cursorToScore(cursor);	//create a comment from all cursor entries on the row
			
			Log.d("COLORSHAFTED", "scoreEntry.name = " + scoreEntry.getName() + ", scoreEntry.score = " + scoreEntry.getScore() + ", scoreEntry.playTime = " + scoreEntry.getPlayTime() + "scoreEntry.mode = " + scoreEntry.getGameStyle());
			
			if(scoreEntry.getName().equals(name) && scoreEntry.getScore() == score && scoreEntry.getPlayTime() == playTime)
			{
				return rank;
			}
			cursor.moveToNext();	//move to next
			rank++;
		}
		return -1;
	}
	
	public StringBuilder findScoreRankOverall(String name, int score, long playTime, GameMode mode)
	{
		int rankCount =  1;
		int rankFound = -1;
		//create the cursor/entry iterator based off of the query
		Cursor cursor = database.query(ScoreDataHelper.TABLE_NAME, allColumns, 
									   ScoreDataHelper.COLUMN_STYLE + "=" + (mode == GameMode.ARCADE ? "0" : "1"), 
										null, null, null, ORDER_BY_SCORE);
		cursor.moveToFirst();		//initialize it
		while(!cursor.isAfterLast())//loop while there are still entries to loop through
		{
			ScoreEntry scoreEntry = cursorToScore(cursor);	//create a comment from all cursor entries on the row
			if(scoreEntry.getName().equals(name) && scoreEntry.getScore() == score && scoreEntry.getPlayTime() == playTime)
			{
				rankFound = rankCount;
			}
			cursor.moveToNext();							//move to next
			rankCount++;
		}
		if(rankFound != -1)
			return new StringBuilder("" + rankFound + "/" + (rankCount-1));
		else
			return new StringBuilder("N/A - Error(Please Let Us Know!) :/)");
	}
	
	public void deleteAllScores()
	{
		database.delete(ScoreDataHelper.TABLE_NAME, null, null);
	}
	
	/** convert a cursor's data to a high score entry */
	private ScoreEntry cursorToScore(Cursor cursor)
	{
		ScoreEntry highScore = new ScoreEntry();
		highScore.setId(cursor.getLong(0));
		highScore.setName(cursor.getString(1));
		highScore.setScore(cursor.getInt(2));
		highScore.setLevel(cursor.getInt(3));
		highScore.setPlayTime(cursor.getLong(4));
		highScore.setDate(cursor.getLong(5));
		highScore.setGameStyle(cursor.getInt(6));
		return highScore;
	}
}
