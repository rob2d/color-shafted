package com.whateversoft.colorshafted.screens;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.GamePreferences;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.network.HttpClientApp;
import com.whateversoft.android.framework.impl.android.network.HttpRequestTask;
import com.whateversoft.android.framework.impl.android.network.HttpTools;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShaftedPrompter;
import com.whateversoft.colorshafted.ColorShaftedPrompter.GameResultAction;
import com.whateversoft.colorshafted.constants.CSScreens;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.database.ScoreDataSource;
import com.whateversoft.colorshafted.game.GameStats;
import com.whateversoft.colorshafted.network.SubmitScoreRequest;

public class GameOverScr extends Screen implements HttpClientApp
{
	public ImageEntity gameOverTxtImg, gameOverBgImg, gameOverTapImg;
	Rect   fadeScreenRect = new Rect(0, 0, ScreenInfo.virtualWidth, ScreenInfo.virtualHeight);
	
	public int bgY;
	public float backgroundAngle = 0;
	public boolean gOSndPlayed = false;

	public String userName = "";
	
	public final int FADE_IN_TIME = 25;
	
	public GameOverScr(Game game)
	{
		super(game, new GameOverScrAssets(game), 1);
		gameOverBgImg = new ImageEntity(ScreenInfo.virtualWidth/2, ScreenInfo.virtualHeight/2, 
				assets.getImage(GameOverScrAssets.IMG_BG), 0, this);
		gameOverBgImg.y = gameOverBgImg.y = 
				 400 -(700 - game.getGraphics().getHeight())/2 + 
				 Math.round(Math.cos(Math.toRadians(backgroundAngle)) * (700 - game.getGraphics().getHeight())/2);
		gameOverTxtImg = new ImageEntity(ScreenInfo.virtualWidth/2, ScreenInfo.virtualHeight/2, 
				assets.getImage(GameOverScrAssets.IMG_GAMEOVER_TXT), 0, this);
		gameOverTapImg = new ImageEntity(ScreenInfo.virtualWidth/2, ScreenInfo.virtualHeight - 12,
				assets.getImage(GameOverScrAssets.IMG_GAMEOVER_TAPTXT), 0, this);
	}

	@Override
	public void timedLogic()
	{
		if(!gOSndPlayed)
		{
			if(game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
				assets.getSound(GameOverScrAssets.SND_GAMEOVER).play(1f);
			gOSndPlayed = true;
		}
			
			//MOVE THE SPACE BACKGROUND
		backgroundAngle+= 0.25;
		backgroundAngle %= 360;
		gameOverBgImg.y = 
				 400 -(700 - game.getGraphics().getHeight())/2 + 
				 Math.round(Math.cos(Math.toRadians(backgroundAngle)) * (700 - game.getGraphics().getHeight())/2);
	}

	@Override
	public void fadeInLogic(float deltaTime)
	{
		if(fadeInTimer >= FADE_IN_TIME)
		{
			fadingIn = false;
		}
	}
	
	@Override
	public void screenTapped()
	{
		gameOverTapImg.visible = false;
		drawEntities(game.getGraphics());
		responseSequence();
	}
	
	@Override
	public void backPressed()
	{
		responseSequence();
	}
	
	/** prompt for a high score and save our current entry into the local database */
	public void promptForHighScore()
	{
		//prompt for high scores
		userName = ((ColorShaftedPrompter)game.getPrompter()).promptForHighScore();
		//load our highscore database into ram
		ScoreDataSource dataSource = new ScoreDataSource(((CanvasGame)game));
		dataSource.open();
		//insert the player's score
		dataSource.createHighScore(userName, GameStats.score, GameStats.difficulty, GameStats.playTime, 
			Calendar.getInstance().getTimeInMillis(),  GameStats.gameStyle);
		GameStats.localScoreRanking = dataSource.findScoreRank(userName, GameStats.score, GameStats.playTime, GameStats.gameStyle);
		GameStats.rankingSummary   = dataSource.findScoreRankOverall(userName, GameStats.score, GameStats.playTime, GameStats.gameStyle);
		dataSource.close();
	}
	
	@Override
	public void drawEntities(Graphics g)
	{
		super.drawEntities(g);
		if(fadingIn)
		{
			g.drawRect(fadeScreenRect, Color.argb(255 - (int)(((float)fadeInTimer/FADE_IN_TIME) * 255.0), 
					255, 255, 255), true);
		}
	}

	@Override
	public void onServerResponse(HttpRequestTask httpTask,
			int serverRequestCode, String responseStr)
	{
		game.stopLoadingDialog();
		
		if(responseStr != null)
		{
			//parse the information we just received into a string array
			responseStr = HttpTools.htmlParser.clearHTMLTags(responseStr);	//clear HTML tags from string first!
			Integer[] returnValues = HttpTools.gson.fromJson(responseStr, Integer[].class);
			
			//check that our device id has been assigned and if not, do so with the retrieved server data
			int deviceId = Integer.valueOf(game.getPreferences().getPref(CSSettings.KEY_DEVICE_ID, CSSettings.DEFAULT_DEVICE_ID));
			if(deviceId == -1)
			{
				//assign the newly returned device ID and save to your device
				deviceId = returnValues[SubmitScoreRequest.RINDEX_DEVICE_ID];
				game.getPreferences().setPref(CSSettings.KEY_DEVICE_ID, deviceId);
			}

			//get server
			boolean wasHighestScore = returnValues[SubmitScoreRequest.RINDEX_WAS_HIGHEST] == 1;
			int highestScore = returnValues[SubmitScoreRequest.RINDEX_HIGHEST_RANK];
			
			if(wasHighestScore)
			{
				Toast.makeText((ColorShafted)game, 
						"You have made a new high score, congratulations!", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText((ColorShafted)game, 
						"Not quite your best, huh?", Toast.LENGTH_LONG).show();
			}
			
			goToScreen(new HighScoreBufferScr(highestScore, GameStats.gameStyle, true, game));
		}
		else
		{
			Toast.makeText((ColorShafted)game, 
					"There was a problem connecting to the server. Please check your connection and try again " +
					"or email us at whateversoftapps@gmail.com to let us know!", Toast.LENGTH_LONG).show();
			goToScreen(new HighScoreBufferScr(GameStats.localScoreRanking, GameStats.gameStyle, false, game));
		}
	}

	@Override
	public void onRequestTimeout(HttpRequestTask httpTask)
	{
		game.stopLoadingDialog();
		Toast.makeText((ColorShafted)game, "Sorry, your score submission has timed out", 
				Toast.LENGTH_LONG).show();	
		goToScreen(new HighScoreBufferScr(GameStats.localScoreRanking, GameStats.gameStyle, false, game));
	}
	
	public void responseSequence()
	{
		promptForHighScore();
		
		//show game results dialog and then follow users action wherever that takes you
		GameResultAction actionTaken = ((ColorShaftedPrompter)game.getPrompter()).showGameResults();
		switch(actionTaken)
		{
			case GO_TO_TITLE:
				goToScreen(new TitleScr((ColorShafted)game));
				break;
			case VIEW_HIGH_SCORES:
				goToScreen(new HighScoreScr(GameStats.localScoreRanking, GameStats.gameStyle, false, game));
				break;
			case PLAY_AGAIN:
				goToScreen(new RecycleGameScr(game));
				break;
			case SUBMIT_ONLINE:
				final HttpClientApp thisHttpClient = this;
				GamePreferences prefs = game.getPreferences();
				Log.d("COLORSHAFTED", "deviceId = " + game.getPreferences().getPref("deviceId", CSSettings.DEFAULT_DEVICE_ID));
				final int deviceId = game.getPreferences().getPref(CSSettings.KEY_DEVICE_ID, -1);
				//run our high score submission on the ui thread
				ColorShafted.context.runOnUiThread(new Thread()
				{
					public void run()
					{
						new SubmitScoreRequest(userName,
								GameStats.score, GameStats.difficulty, 
								GameStats.playTime,System.currentTimeMillis(),GameStats.gameStyle.getServerValue(), 
								deviceId, thisHttpClient)
							.execute();
					}
				});
				game.startLoadingDialog("Please Wait...", "Submitting Score", true);
				break;
		}
	}

	@Override
	public void showServerErrorMsg()
	{
		Toast.makeText((Context)game, "Sorry, there was a problem communicating with the server. Please ensure you have connection to the network. If this error persists, please email us at" +
				"whateversoftapps@gmail.com", Toast.LENGTH_LONG).show();
	}

	@Override
	public int getScreenCode()
	{
		return CSScreens.SCR_GAME_OVER;
	}
}
