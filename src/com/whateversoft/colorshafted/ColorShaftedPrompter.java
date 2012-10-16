package com.whateversoft.colorshafted;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.colorshafted.ColorShafted.GameMode;
import com.whateversoft.colorshafted.constants.CSSettings;

public class ColorShaftedPrompter extends PrompterAndroid
{
	public enum GameResultAction { GO_TO_TITLE, VIEW_HIGH_SCORES, PLAY_AGAIN, SUBMIT_ONLINE };
	
	public GameResultAction resultScreenSelection;
	
	public ColorShaftedPrompter(CanvasGame c)
	{
		super(c);
	}
	
	public String promptForHighScore()
	{
		//ONLY GIVE PROMPT IF THE SETTING FOR HIGH SCORE PROMPTING IS ENABLED!
		if(context.getPreferences().getPref(CSSettings.KEY_HIGHSCORE_PROMPT, CSSettings.DEFAULT_HIGHSCORE_PROMPT))
		{
			//pause music if applicable
			if(context.getMusic() != null)
				context.getMusic().activityPauseCycle(true);
			
			promptCompleted = false;
			
			final Dialog prompt = 
					new HighScoreDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread());
			
			Thread t = new Thread()
			{
				@Override
				public void run()
				{
					Looper.prepare();
					prompt.show();
					Looper.loop();
					Looper.myLooper().quit();
				}
			};
			t.start();					//start prompt thread
			promptCompleted = false;
			
			synchronized(Thread.currentThread())	//pause UI thread till player calls it with button on prompt
			{
				try
				{
					Thread.currentThread().wait();
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			prompt.dismiss();
			
			//resume music if applicable
			if(context.getMusic() != null)
				context.getMusic().activityPauseCycle(false);
			return userInput;
		}
		else 
		{
			String defaultEntry = context.getPreferences().getPref(CSSettings.KEY_HIGHSCORE_NAME, "DEVICE OWNER");
			return(!defaultEntry.equals("") ? defaultEntry : "DEVICE OWNER");
		}
	}
	
	public GameResultAction showGameResults()
	{
		//pause music if applicable
		if(context.getMusic() != null)
			context.getMusic().activityPauseCycle(true);
		
		promptCompleted = false;
		
		final Dialog prompt = new GameResultsDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(), GameMode.ARCADE);
		
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				prompt.show();
				Looper.loop();
				Looper.myLooper().quit();
			}
		};
		t.start();					//start prompt thread
		promptCompleted = false;
		
		synchronized(Thread.currentThread())	//pause UI thread till player calls it with button on prompt
		{
			try
			{
				Thread.currentThread().wait();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		prompt.dismiss();
		
		//resume music if applicable
		if(context.getMusic() != null)
			context.getMusic().activityPauseCycle(false);
		
		//return the selection made from the game results dialog prompt
		return resultScreenSelection;
	}
	
	public void showTutorialDialog(StringBuilder titleMsg, StringBuilder contentMsg, StringBuilder okTxt)
	{
				promptCompleted = false;
				
				final Dialog prompt = new TutorialDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(), 
						titleMsg, contentMsg, okTxt);
				
				Thread t = new Thread()
				{
					@Override
					public void run()
					{
						Looper.prepare();
						prompt.show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				};
				t.start();					//start prompt thread
				promptCompleted = false;
				
				synchronized(Thread.currentThread())	//pause UI thread till player calls it with button on prompt
				{
					try
					{
						Thread.currentThread().wait();
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				prompt.dismiss();
				//return the selection made from the game results dialog prompt
				return;
	}
	
	/** show a dialog message and then go to the google play store to download color shafted right after pressing OK! */
	public void showMsgGoToStore(StringBuilder msg)
	{
		this.showMsg(msg);
		context.game.launchWebsite("https://play.google.com/store/apps/details?id=com.whateversoft&feature=" +
				"search_result#?t=W251bGwsMSwyLDEsImNvbS53aGF0ZXZlcnNvZnQiXQ..");
	}
	
	/** launch standard credits dialog for Color Shafted */
	public void showCredzDialog()
	{
				promptCompleted = false;
				
				StringBuilder titleMsg = new StringBuilder("CREDITS");
				StringBuilder contentMsg = new StringBuilder(
							"Designer & Application Programmer\n" + 
							"\tRobert Concepcion III\n\n" +
							
							"Planning\n" +
							"\tRobert Concepcion III\n" + 
							"\tChristopher Hoyos\n\n" +
							
							"Graphics and Animation\n" +
							"\tChristopher Hoyos\n\n" +
							
							"Color Explode Animation\n" + 
							"\tEdward Coyle\n\n" +
							
							"Sound Effects\n" +
							"\tRobert Concepcion III\n\n" +	
							"Music\n" +
							"\tRobert Concepcion III\n" +
							"\tRenee Dizzy Morales\n\n" +
							
							"Special Thanks\n" +
							"\tRael Oliviera\n" +
							"\tLauren Kaluund\n" +
							"\tAdam Chew\n\n" +
							
							"\t... And You For Playing!\n\n");
				
				StringBuilder okTxt = new StringBuilder("BACK");
				final Dialog prompt = new CreditsDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread());
				
				Thread t = new Thread()
				{
					@Override
					public void run()
					{
						Looper.prepare();
						prompt.show();
						Looper.loop();
						Looper.myLooper().quit();
					}
				};
				t.start();					//start prompt thread
				promptCompleted = false;
				
				synchronized(Thread.currentThread())	//pause UI thread till player calls it with button on prompt
				{
					try
					{
						Thread.currentThread().wait();
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				prompt.dismiss();
				//return the selection made from the game results dialog prompt
				return;
	}
	
	/** apply a specified font to all components within an Android View(ViewGroup) */
	public static void applyFonts(final View v, Typeface fontToSet)
	{
	    try {
	        if (v instanceof ViewGroup) {
	            ViewGroup vg = (ViewGroup) v;
	            for (int i = 0; i < vg.getChildCount(); i++) {
	                View child = vg.getChildAt(i);
	                applyFonts(child, fontToSet);
	            }
	        } else if (v instanceof TextView) {
	            ((TextView)v).setTypeface(fontToSet);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // ignore
	    }
	}
}
