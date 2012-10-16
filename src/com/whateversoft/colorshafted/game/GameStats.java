package com.whateversoft.colorshafted.game;

import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSSettings;

/** game stats to track a game of color shafted */
public class GameStats
{
	/** in-game data */
	public static int score,
			   		  lives,
			   		  playTime,
			   		  shield,
			   		  difficulty,
			   		  startingDifficulty,
			   		  bombs,
			   		  enemiesHit,
			   		  enemiesEvaded;
	
	/** field to track additional data */
	public static int localScoreRanking,
					  successfulCollisions,
					  bombsUsed,
					  maxCombo;
	/** percentage of blocks missed compared to hit */
	public static int 
					  evasionPercentage;
	
	/** stores the player's high score in the form of rankMade/rankCount */
	public static StringBuilder rankingSummary = new StringBuilder();
	
	public static GameMode gameStyle = GameMode.ARCADE;
	
	public static void resetStatsClassic(int b, ColorShafted game)
	{
		try
		{
			lives		 = Integer.valueOf(game.getPreferences().getPref(CSSettings.KEY_LIVES_ARCADE, CSSettings.DEFAULT_LIVES_ARCADE));
		}
		catch(NumberFormatException nFE)
		{
			lives = Integer.valueOf(CSSettings.DEFAULT_LIVES_ARCADE);
		}
		bombs      = b;
	}
	
	public static void resetStatsSurvival(int sh, int b, ColorShafted game)
	{
		shield     = sh;
		bombs      =  b;
	}
	
	public static void newGameStats(ColorShafted game)
	{
		if(gameStyle == GameMode.ARCADE || gameStyle == GameMode.PSYCHOUT)
			resetStatsClassic(5, game);	//CHANGE TO THE RELATIVE OPTIONS LATER
		else
		if(gameStyle == GameMode.SURVIVAL)
			resetStatsSurvival(1000, 5, game);
		score      			 = 0;
		playTime      		 = 0;
		maxCombo      		 = 0;
		bombsUsed     		 = 0;
		enemiesHit    		 = 0;
		enemiesEvaded 		 = 0;
		successfulCollisions = 0;
		localScoreRanking     = -1;
		rankingSummary.setLength(0);
		
		difficulty = game.getPreferences().getPref(CSSettings.KEY_DIFFICULTY, CSSettings.DEFAULT_DIFFICULTY);
		startingDifficulty = difficulty;
	}
	
	public static String getFormattedPlayTime()
	{
		int minutes 	   = (int)(playTime / 1000.0 / 60.0);
		int seconds 	   = (int)((playTime - (minutes * 1000 * 60))) / 1000;
		int centis 		   = (int)((playTime - ((minutes * 1000 * 60) + (seconds * 1000)))/10);
		return String.format((minutes > 9 ? "%d:" : "0%d:") + 
							 (seconds > 9 ? "%d:" : "0%d:") +
							 (centis  > 9 ? "%d" : "0%d"),
							 	minutes, seconds, centis);
	}
}
