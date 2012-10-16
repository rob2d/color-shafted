package com.whateversoft.android.framework.textui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.R;

public class TripleDialog extends GameDialog
{
	TextView messageTV;
	Button	 button1,
			 button2;
	
	Typeface dialogFont;
	
	public TripleDialog(final PrompterAndroid prompter, final Thread threadRunning, 
						final StringBuilder message, 
						final StringBuilder btn1Txt, final StringBuilder btn2Txt, final StringBuilder btn3Txt)
	{
		super(prompter.context, R.style.TestStyle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.triple_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		TextView messageTV = (TextView)findViewById(R.id.prompt_triple_text);
		messageTV.setTypeface(dialogFont);
		messageTV.setText(message);
		
		Button button1 = (Button)findViewById(R.id.prompt_triple_1_button);
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
				dismiss();

				//let the android ui thread know that things have finished
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
		
		Button button2 = (Button)findViewById(R.id.prompt_triple_2_button);
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
		
		Button button3 = (Button)findViewById(R.id.prompt_triple_3_button);
		button3.setText(btn3Txt);
		button3.setTypeface(dialogFont);
		button3.setOnClickListener(new View.OnClickListener()
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
	}
}

