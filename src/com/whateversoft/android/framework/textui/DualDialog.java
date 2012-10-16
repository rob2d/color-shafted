package com.whateversoft.android.framework.textui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.android.framework.impl.android.SystemInfo;
import com.whateversoft.R;

public class DualDialog extends GameDialog
{
	TextView messageTV;
	Button	 button1,
			 button2;
	Typeface dialogFont;
	
	public DualDialog(final PrompterAndroid prompter, final Thread threadRunning, 
						final StringBuilder message, final StringBuilder btn1Txt, final StringBuilder btn2Txt)
	{
		super(prompter.context, R.style.TestStyle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dual_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		TextView messageTV = (TextView)findViewById(R.id.prompt_dual_text);
		messageTV.setTypeface(dialogFont);
		messageTV.setText(message);
		
		Button button1 = (Button)findViewById(R.id.prompt_dual_1_button);
		button1.setText(btn1Txt);
		button1.setTypeface(dialogFont);
		
		button1.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				prompter.returnedValue = 1;
				prompter.promptCompleted = true;
				//finish the dialog!
				try
				{
					dismiss();
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
		
		Button button2 = (Button)findViewById(R.id.prompt_dual_2_button);
		button2.setText(btn2Txt);
		button2.setTypeface(dialogFont);
		button2.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 2's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				prompter.returnedValue = 2;
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
		
		//enable buttons to be selected w DPAD if its a google TV
		if(SystemInfo.isGoogleTV())
		{
			button1.setFocusable(true);
			button2.setFocusable(true);
		}
	}
}
