package com.whateversoft.colorshafted.network;

import android.util.Log;

import com.whateversoft.android.framework.impl.android.network.HttpClientApp;
import com.whateversoft.android.framework.impl.android.network.HttpRequestTask;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSSettings;

/** AsyncTask which requests high scores in Color Shafted */
public class SubmitScoreRequest extends HttpRequestTask
{
	/** what was returned for the highest rank index we have achieved online */
	public final static int RINDEX_HIGHEST_RANK = 0;
	/** what was returned for whether or not we have achieved our highest rank on this try */
	public final static int RINDEX_WAS_HIGHEST 	= 1;
	/** what was returned for what device id we have on the server */
	public final static int RINDEX_DEVICE_ID	= 2;
	
	HttpClientApp callerApp;
	int  		  deviceId;
	int  		  score;
	int  		  gameStyle;
	long 		  datePlayed;
	long 		  playTime;
	int			  level;
	String		  name;
	
	public SubmitScoreRequest(String n, int s, int l, long pT, long date, int gS, int dId,  HttpClientApp app)
	{
		super(app, HttpRequestTask.HTTP_POST, 
				Long.parseLong(ColorShafted.context.game.getPreferences().getPref(
						CSSettings.KEY_HTTP_TIMEOUT, 
						CSSettings.DEFAULT_HTTP_TIMEOUT)));
		Log.d("HttpRequests", "submit score request created with deviceId = " + dId + ", gameStyle = " + 
						gameStyle);
		Log.d("HttpRequests", "HighScoresRequest's instance refers to memory location " +  this);
		deviceId  = dId;
		score     = s;
		gameStyle = gS;
		playTime  = pT;
		datePlayed= date;
		name	  = n;
		level	  = l;
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		Log.d("HttpRequests", "submit score request is launching it's doInBackground(String... params) method");
		String returnStr = super.doInBackground("http://whateversoft.webuda.com/color_shafted/v80/submit_score.php",
							//String.valueOf(prefs.getPref(CSSettings.KEY_HTTP_TIMEOUT, 
							//						 CSSettings.DEFAULT_HTTP_TIMEOUT)),
										"game_style",   String.valueOf(gameStyle),
										"score",        String.valueOf(score),
										"device_id", 	String.valueOf(deviceId),
										"name",		 	name ,
										"play_time", 	String.valueOf(playTime),
										"date",		 	String.valueOf(datePlayed),
										"level",		String.valueOf(level));
		Log.d("HttpRequests", "SubmitScores is about to return " + returnStr);
		return returnStr; 	
	}
	
	@Override
	public void onPostExecute(String responseStr)
	{
		super.onPostExecute(responseStr);
		Log.d("HttpRequests", "SubmitScoreRequest running onPostExecute()");
	}

	@Override
	public int getResponseCode()
	{
		return ServerCodes.SUBMIT_HIGHSCORE;
	}
}