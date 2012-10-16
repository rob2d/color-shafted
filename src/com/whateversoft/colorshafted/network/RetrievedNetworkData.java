package com.whateversoft.colorshafted.network;

import com.whateversoft.android.framework.impl.android.network.HttpTools;
import com.whateversoft.colorshafted.database.ScoreEntry;

public class RetrievedNetworkData
{
	ScoreEntry[] highScoreData;

	/** retrieve a polled JSON String and convert it to high score data to be used on our network */
	public void setHighScoreData(StringBuffer hSD)
	{
		highScoreData = HttpTools.gson.fromJson(hSD.toString(), ScoreEntry[].class);
	}
}
