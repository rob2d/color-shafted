package com.whateversoft.colorshafted.network;

import android.util.Log;

import com.whateversoft.android.framework.impl.android.network.HttpClientApp;
import com.whateversoft.android.framework.impl.android.network.HttpRequestTask;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.constants.CSSettings;

/** AsyncTask which requests high scores in Color Shafted */
public class HighScoresRequest extends HttpRequestTask
{
	HttpClientApp callerApp;
	int gameStyle;
	int index;
	int resultCount;
	
	public HighScoresRequest(int gS, int i, int rC, HttpClientApp app)
	{
		super(app, HttpRequestTask.HTTP_POST, 
				Long.parseLong(ColorShafted.context.game.getPreferences().getPref(
						CSSettings.KEY_HTTP_TIMEOUT, 
						CSSettings.DEFAULT_HTTP_TIMEOUT)));
		Log.d("HttpRequests", "highscore request created with gameStyle = " + 
		gS + " and index = "+i + " and result_count = " + rC);
		Log.d("HttpRequests", "HighScoresRequest's instance refers to memory location " +  this);
		gameStyle = gS;
		index = i;
		resultCount = rC;
	}
	
	@Override
	protected String doInBackground(String... params)
	{
		Log.d("HttpRequests", "highscore request is launching it's doInBackground(String... params) method");
		String returnStr = super.doInBackground("http://whateversoft.webuda.com/color_shafted/v80/retrieve_scores.php",
							//String.valueOf(prefs.getPref(CSSettings.KEY_HTTP_TIMEOUT, 
							//						 CSSettings.DEFAULT_HTTP_TIMEOUT)),
										"game_style",   String.valueOf(gameStyle),
										"index",        String.valueOf(index),
										"result_count", String.valueOf(resultCount));
		Log.d("HttpRequests", "HighScoresRequest is about to return " + returnStr);
		return returnStr; 	
	}
	
	@Override
	public void onPostExecute(String responseStr)
	{
		super.onPostExecute(responseStr);
		Log.d("HttpRequests", "HighScoresRequest running onPostExecute()");
	}

	@Override
	public int getResponseCode()
	{
		return ServerCodes.GET_HIGHSCORES;
	}
	
	public int getGameStyle()
	{
		return gameStyle;
	}
}