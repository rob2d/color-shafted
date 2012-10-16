package com.whateversoft.android.framework.textui;

import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.EditText;

/** Prompt dialog for android applications*/
public class PrompterAndroid implements Prompter
{
	/** whether a prompt was given */
	public boolean promptGiven;
	/** whether the user has given input from a given prompt */
	public boolean promptCompleted;
	
	/** whether a prompt is being shown */
	public boolean promptIsShown;
	/** what a user has entered for a prompt */
	public String userInput = null;
	/** what a user has entered a prompt's title */
	public String promptTitle;
	/** message to be displayed during a prompt */
	public String promptMessage;	
	/** used if we want to return a value from a method depending on a button clicked */
	public int returnedValue;
	/** reference to a dialog that may be being shown at a given moment(set to either the dialog or null if not available) */
	Dialog displayedDialog;	

	public static final int RESULT_OK = 1;
	
	public static final int CONFIRM_PROMPT = 1,
							  INPUT_PROMPT = 2,
							  DUAL_PROMPT  = 3,
							 TRIPLE_PROMPT = 4;
	
	/** keeps track of the application to launch the prompter from */
	public final CanvasGame context;
	
	public PrompterAndroid(CanvasGame c)
	{
		super();
		context = c;
	}

	public String showInputPrompt(StringBuilder message)
	{
		//pause music if applicable
			if(context.getMusic() != null)
				context.getMusic().activityPauseCycle(true);
			
			promptCompleted = false;
			
			final Dialog prompt = new InputDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(), message);
			
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
	
	@Override
	public void showMsg(final StringBuilder promptMsg)
	{	
		//pause music if applicable
		if(context.getMusic() != null)
			context.getMusic().activityPauseCycle(true);
		
		promptCompleted = false;
		
		final Dialog prompt = new ConfirmDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(), new StringBuilder(promptMsg));
		context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
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
	}

	/** prompt user between 2 choices. if the first is chosen, true is returned. otherwise, false is returned */
	@Override
	public int showDualOption(StringBuilder promptMsg,
			StringBuilder b1T, StringBuilder b2T)
	{
		//pause music if applicable
		if(context.getMusic() != null)
			context.getMusic().activityPauseCycle(true);
		
		final EditText inputTxt = new EditText(context);
		promptCompleted = false;
		
		final Dialog prompt = new DualDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(),
												promptMsg, b1T, b2T);
		
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
		
		return returnedValue;
	}

	@Override
	public int showTripleOption(StringBuilder promptMsg,
			StringBuilder b1T, StringBuilder b2T, StringBuilder b3T)
	{
		//pause music if applicable
		if(context.getMusic() != null)
			context.getMusic().activityPauseCycle(true);
		
		final EditText inputTxt = new EditText(context);
		promptCompleted = false;
		
		final Dialog prompt = new TripleDialog((PrompterAndroid)context.getPrompter(), Thread.currentThread(),
												promptMsg, b1T, b2T, b3T);
		
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
		
		return returnedValue;
	}
	
	/** called from the owning Android Activity class in order to catch dialog results */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bundle extras = (data.getExtras() != null ? data.getExtras() : null);
		if(extras != null)
		{
			if(extras.getInt("promptType") == CONFIRM_PROMPT)
				promptCompleted = true;
			if(extras.getInt("promptType") == INPUT_PROMPT)
			{
				userInput = extras.getString("userInput");
				promptCompleted = true;
			}
			if(extras.getInt("promptType") == TRIPLE_PROMPT)
			{
				returnedValue = extras.getInt("promptResult");
				Log.d("COLORSHAFTED", "returned a result from a triple option dialog: " + returnedValue);
			}
		}
	}

	public Dialog getDisplayedDialog()
	{
		return (displayedDialog != null) ? displayedDialog : null;
	}
	
	public int getReturnedValue()
	{
		return returnedValue;
	}
}
