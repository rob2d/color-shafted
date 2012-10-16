package com.whateversoft.android.framework.textui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.R;

/** the bundle associated with the intent has to have "promptMsg" to show what the prompt message will be when passed! 
 * */
public class ConfirmDialog extends GameDialog
{
	TextView messageTV;
	Typeface dialogFont;
	
	public ConfirmDialog(final PrompterAndroid prompter, final Thread threadRunning, final StringBuilder message)
	{
		super(prompter.context, R.style.TestStyle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.confirm_dialog);
		this.setOwnerActivity(prompter.context);
		
		setCancelable(false);

		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		TextView messageTV = (TextView)findViewById(R.id.prompt_confirm_text);

		messageTV.setTypeface(dialogFont);
		messageTV.setText(message);
		
		Button okButton = (Button)findViewById(R.id.prompt_confirm_ok_button);
		okButton.setTypeface(dialogFont);
		okButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE THE OK BUTTON BEHAVIOR */
			@Override
			public void onClick(View v)
			{
				Intent returnIntent = new Intent();
				prompter.promptCompleted = true;
				
				synchronized(threadRunning)
				{
					threadRunning.notify();
				}
			}
		});
	}

	/** do not allow escaping with back key! */
	@Override
	public void onBackPressed() 
	{
	   return;
	}

}
