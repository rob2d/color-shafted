package com.whateversoft.colorshafted.network;

/** The different server codes for HTTP requests made in Color Shafted */
public class ServerCodes
{
	/** code used to return a new device ID from the server */
	public final static int INSERT_SCORE    = 0;
	/** code used to return the place a player has had in his game mode on the 
	 *  high score board after sending his end game stats */
	public final static int SUBMIT_HIGHSCORE = 1;
	public final static int GET_HIGHSCORES  = 2;
}
