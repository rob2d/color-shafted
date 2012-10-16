package com.whateversoft.colorshafted;

import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.ColorShaftedPrompter.GameResultAction;
import com.whateversoft.colorshafted.game.GameStats;
import com.whateversoft.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameResultsDialog extends Dialog
{
	Typeface dialogFont;
	
	public GameResultsDialog(final PrompterAndroid prompter, final Thread threadRunning, GameMode gameMode)
	{
		super(prompter.context, R.style.TestStyle);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game_results_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		TextView gameModeTxt = (TextView)findViewById(R.id.gameres_mode_text);
		gameModeTxt.setText(new StringBuilder(gameMode == GameMode.ARCADE ? "ARCADE MODE - GAME OVER" : "SURVIVAL MODE - GAME OVER"));
		gameModeTxt.setTypeface(dialogFont);
		
		TextView gameResultsTxt = (TextView)findViewById(R.id.gameres_game_results_txt);
		gameResultsTxt.setTypeface(dialogFont);
		
		TextView scoreDescTxt = (TextView)findViewById(R.id.gameres_score_desc);
		scoreDescTxt.setTypeface(dialogFont);
		TextView scoreValueTxt = (TextView)findViewById(R.id.gameres_score_value);
		scoreValueTxt.setTypeface(dialogFont);
		scoreValueTxt.setText(GameStats.score + "");
		
		TextView levelDescTxt = (TextView)findViewById(R.id.gameres_level_desc);
		levelDescTxt.setTypeface(dialogFont);
		TextView levelValueTxt = (TextView)findViewById(R.id.gameres_level_value);
		levelValueTxt.setTypeface(dialogFont);
		levelValueTxt.setText(GameStats.difficulty + "");
		
		TextView timeDescTxt = (TextView)findViewById(R.id.gameres_time_desc);
		timeDescTxt.setTypeface(dialogFont);
		TextView timeValueTxt = (TextView)findViewById(R.id.gameres_time_value);
		timeValueTxt.setTypeface(dialogFont);
		timeValueTxt.setText(GameStats.getFormattedPlayTime());
		
		TextView rankingDescTxt = (TextView)findViewById(R.id.gameres_ranking_desc);
		rankingDescTxt.setTypeface(dialogFont);
		TextView rankingValueTxt = (TextView)findViewById(R.id.gameres_ranking_value);
		rankingValueTxt.setTypeface(dialogFont);
		rankingValueTxt.setText(GameStats.rankingSummary);
		
		TextView maxComboDescTxt = (TextView)findViewById(R.id.gameres_maxcombo_desc);
		maxComboDescTxt.setTypeface(dialogFont);
		TextView maxComboValueTxt = (TextView)findViewById(R.id.gameres_maxcombo_value);
		maxComboValueTxt.setTypeface(dialogFont);
		maxComboValueTxt.setText("" + GameStats.maxCombo);
		
		TextView bombsUsedDescTxt = (TextView)findViewById(R.id.gameres_bombsused_desc);
		bombsUsedDescTxt.setTypeface(dialogFont);
		TextView bombsUsedValueTxt = (TextView)findViewById(R.id.gameres_bombsused_value);
		bombsUsedValueTxt.setTypeface(dialogFont);	
		bombsUsedValueTxt.setText("" + GameStats.bombsUsed);
		
		TextView evadeDescTxt = (TextView)findViewById(R.id.gameres_evadepercent_desc);
		evadeDescTxt.setTypeface(dialogFont);
		TextView evadeValueTxt = (TextView)findViewById(R.id.gameres_evadepercent_value);
		evadeValueTxt.setTypeface(dialogFont);	
		evadeValueTxt.setText("" + Math.round((float)GameStats.enemiesEvaded/(float)GameStats.enemiesHit * (float)100));
		
		Button playAgainButton = (Button)findViewById(R.id.gameres_again_button);
		playAgainButton.setTypeface(dialogFont);
		playAgainButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				((ColorShaftedPrompter)prompter).resultScreenSelection = GameResultAction.PLAY_AGAIN;
				prompter.promptCompleted = true;
				//finish the dialog!
				try
				{
					this.finalize();
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				//let the android ui thread know that things have finished
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
		
		Button submitOLButton = (Button)findViewById(R.id.gameres_online_button);
		submitOLButton.setTypeface(dialogFont);
		submitOLButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				((ColorShaftedPrompter)prompter).resultScreenSelection = GameResultAction.SUBMIT_ONLINE;
				prompter.promptCompleted = true;
				//finish the dialog!
				try
				{
					this.finalize();
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				//let the android ui thread know that things have finished
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
		
		Button titleScreenButton = (Button)findViewById(R.id.gameres_title_button);
		titleScreenButton.setTypeface(dialogFont);
		titleScreenButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				((ColorShaftedPrompter)prompter).resultScreenSelection = GameResultAction.GO_TO_TITLE;
				prompter.promptCompleted = true;
				//finish the dialog!
				try
				{
					this.finalize();
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				//let the android ui thread know that things have finished
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
		
		Button highScoresButton = (Button)findViewById(R.id.gameres_hs_button);
		highScoresButton.setTypeface(dialogFont);
		highScoresButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				((ColorShaftedPrompter)prompter).resultScreenSelection = GameResultAction.VIEW_HIGH_SCORES;
				prompter.promptCompleted = true;
				//finish the dialog!
				try
				{
					this.finalize();
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				//let the android ui thread know that things have finished
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
	}
}
