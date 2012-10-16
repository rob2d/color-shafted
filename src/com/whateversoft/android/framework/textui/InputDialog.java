package com.whateversoft.android.framework.textui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.R;

public class InputDialog extends GameDialog
{
	Typeface dialogFont;
	
	public InputDialog(final PrompterAndroid prompter, final Thread threadRunning, 
						final StringBuilder message)
	{
		super(prompter.context, R.style.TestStyle);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.input_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		TextView messageTV = (TextView)findViewById(R.id.prompt_input_text);
		messageTV.setTypeface(dialogFont);
		messageTV.setText(message);
		
		final EditText userInput = (EditText)findViewById(R.id.prompt_input_edittxt);
		userInput.setBackgroundColor(Color.BLACK);
		userInput.setTextColor(Color.rgb(0, 255, 0));
		
		Button okButton = (Button)findViewById(R.id.prompt_input_ok_button);
		okButton.setTypeface(dialogFont);
		okButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				prompter.userInput = userInput.getText().toString();
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
	}
}
