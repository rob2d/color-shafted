package com.whateversoft.colorshafted;

import java.io.IOException;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.whateversoft.android.framework.MusicJukebox;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.MusicJukeboxAndroid;
import com.whateversoft.android.framework.impl.android.network.HttpClientApp;
import com.whateversoft.android.framework.impl.android.network.HttpRequestTask;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.game.CSGlobalAssets;
import com.whateversoft.colorshafted.screens.CSLoaderScreen;
import com.whateversoft.colorshafted.screens.HighScoreScr;
import com.whateversoft.colorshafted.screens.TestBlockScr;
import com.whateversoft.R;

public class ColorShafted extends CanvasGame implements HttpClientApp
{
	public static String APP_TAG = new String("COLORSHAFTED");

	public static enum GameMode 
	{ 
		ARCADE, PSYCHOUT, SURVIVAL, TUTORIAL; 
		/** returns the integer value of this mode to compare against the high score database */
		public int getServerValue()
		{
			switch(this)
			{
				case ARCADE:
					return 0;
				case PSYCHOUT:
					return 1;
				case SURVIVAL:
					return 2;
				default:
					return -1;
			}
		}
	};
	/** constant used to access the game's preferences */
	public final static String PREFERENCE_FILENAME = "CSAndroidv02";
	public static CanvasGame context;
	
	public CSGlobalAssets globalAssets;
	
	public MusicJukeboxAndroid musicPlayer;
	public ColorShafted()
	{
		super();
		//get a prompter to show Android UI messages
		prompter = new ColorShaftedPrompter(this);
		context = this;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
			
		musicPlayer = new MusicJukeboxAndroid(this, MusicJukebox.PlayMode.valueOf(game.getPreferences().getPref(CSSettings.KEY_MUSIC_PLAYMODE, CSSettings.DEFAULT_MUSIC_PLAYMODE)), 
					R.raw.cs_song1, R.raw.cs_song2, R.raw.cs_song3, R.raw.cs_song4, R.raw.cs_song5);
		loadMusicSettings();
	}	
		
	public Screen getStartScreen()
	{	
		//we first call up the CSLoader screen since it loads our global assets and sets our starting point
		//while giving time for initialization of necessary functions by other screens!
		return new CSLoaderScreen(this);
		//return new TestBlockScr(this);
	}

	@Override
	public MusicJukebox getMusic()
	{
		return musicPlayer;
	}

	@Override
	public void onSettingChanged(String key)
	{
		if(key.equals(CSSettings.KEY_ENABLE_MUS))
		{
			//if music has been disabled, make sure it is stopped
			if(!getPreferences().getPref(CSSettings.KEY_ENABLE_MUS, CSSettings.DEFAULT_ENABLE_MUS))
				getMusic().stop();
		}
		else if(key.equals(CSSettings.KEY_PLAY_TRACK_A))
		{
			getMusic().setTrackAsEnabled(0, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_A, 
														 CSSettings.DEFAULT_PLAY_TRACK_A));
		}
		else if(key.equals(CSSettings.KEY_PLAY_TRACK_B))
		{	
			getMusic().setTrackAsEnabled(1, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_B, 
				 										 CSSettings.DEFAULT_PLAY_TRACK_B));
		}
		else if(key.equals(CSSettings.KEY_PLAY_TRACK_C))
		{
			getMusic().setTrackAsEnabled(2, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_C,
														 CSSettings.DEFAULT_PLAY_TRACK_C));
		}
		else if(key.equals(CSSettings.KEY_MUSIC_PLAYMODE))
		{
			getMusic().setPlayMode(
					MusicJukebox.PlayMode.valueOf(game.getPreferences().getPref(CSSettings.KEY_MUSIC_PLAYMODE, 
							CSSettings.DEFAULT_MUSIC_PLAYMODE)));
		}
			
		if(!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_A, CSSettings.DEFAULT_PLAY_TRACK_A) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_B, CSSettings.DEFAULT_PLAY_TRACK_B) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_C, CSSettings.DEFAULT_PLAY_TRACK_C) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_D, CSSettings.DEFAULT_PLAY_TRACK_D))
				ColorShafted.context.getPreferences().setPref(CSSettings.KEY_ENABLE_MUS, false);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void launchSettings()
	{
		Intent settingsLauncher = new Intent(ColorShafted.this, Settings.class);
		startActivity(settingsLauncher);
	}
		
	/** method used to apply music settings to the jukebox at any point during the game when settings have changed or
	 *  should be loaded */
	public void loadMusicSettings()                
	{
			getMusic().setPlayMode(
					MusicJukebox.PlayMode.valueOf(game.getPreferences().getPref(CSSettings.KEY_MUSIC_PLAYMODE, 
							CSSettings.DEFAULT_MUSIC_PLAYMODE)));
			getMusic().setTrackAsEnabled(0, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_A, 
					 CSSettings.DEFAULT_PLAY_TRACK_A));
			getMusic().setTrackAsEnabled(1, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_B, 
					 CSSettings.DEFAULT_PLAY_TRACK_B));
			getMusic().setTrackAsEnabled(2, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_C,
					 CSSettings.DEFAULT_PLAY_TRACK_C));
			getMusic().setTrackAsEnabled(3, game.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_D,
					 CSSettings.DEFAULT_PLAY_TRACK_D));
			getMusic().setTrackAsEnabled(4, false);
		}

	@Override
	public void onServerResponse(HttpRequestTask httpTask,
			int serverRequestCode, String responseStr)
	{
		if(getCurrentScreen() instanceof HighScoreScr)
			((HighScoreScr)getCurrentScreen()).onServerResponse(httpTask, serverRequestCode, responseStr);
	}

	@Override
	public void onRequestTimeout(HttpRequestTask httpTask)
	{
		if(getCurrentScreen() instanceof HighScoreScr)
			((HighScoreScr)getCurrentScreen()).onRequestTimeout(httpTask);
	}

	@Override
	public void showServerErrorMsg()
	{
		Toast.makeText((Context)game, "Sorry, there was a problem communicating with the server. Please ensure you have connection to the network. If this error persists, please email us at" +
				"whateversoftapps@gmail.com", Toast.LENGTH_LONG).show();
	}
}