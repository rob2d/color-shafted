package com.whateversoft.colorshafted.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScoreDataHelper extends SQLiteOpenHelper
{
	public static final String TABLE_NAME = "high_scores";
	public static final String COLUMN_ID = "_id";				//0 - id
	public static final String COLUMN_NAME	= "name";			//1 - name of player(string)
	public static final String COLUMN_SCORE = "score";			//2 - score achieved(int)
	public static final String COLUMN_PLAYTIME	= "time";		//3  length of time played(int)
	public static final String COLUMN_DATE	= "date";			//4 - date played(string)
	public static final String COLUMN_STYLE = "style";			//5 - game style(int)
	public static final String COLUMN_LEVEL = "level";			//6 - game level
	
	//entry key constants
	public static final int KEY_ARCADE	 = 0;		 //represents an arcade game played
	public static final int KEY_SURVIVAL = 1;		 //represents a survival game played
	public static final int KEY_PSYCHOUT = 2;		 //represents a psychout game played
	 
	
	private static final String DATABASE_NAME = "whateversoft.colorshafted.db";
	private static final int	DATABASE_VERSION = 5;
	
	//database creation sql statement
	private static final String DATABASE_CREATE = 
			"create table " +
					TABLE_NAME + 								"( " + 
					COLUMN_ID + " integer primary key autoincrement, " +
					COLUMN_NAME + " text not null, " + 
					COLUMN_SCORE + " integer not null, " + 
					COLUMN_LEVEL + " integer not null, " +
					COLUMN_PLAYTIME + " integer not null, " + 
					COLUMN_DATE +  " string not null, " + 
					COLUMN_STYLE + " integer not null" + ");";

	public ScoreDataHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(ScoreDataHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will" +
						"destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
