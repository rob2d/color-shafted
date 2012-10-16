package com.whateversoft.colorshafted;


import com.whateversoft.android.framework.impl.android.GameDialog;
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

public class TutorialDialog extends GameDialog
{
	Typeface dialogFont;
	
	public TutorialDialog(final PrompterAndroid prompter, final Thread threadRunning, StringBuilder msgHeader, StringBuilder content, StringBuilder okBtnTxt)
	{
		super(prompter.context, R.style.WireframeGreenSemiTrans);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tutorial_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);					//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		TextView msgHeaderTxt = (TextView)findViewById(R.id.tutorial_title_txt);
		msgHeaderTxt.setTypeface(dialogFont);
		msgHeaderTxt.setText(msgHeader);
		
		TextView contentTxt = (TextView)findViewById(R.id.tutorial_desc_txt);
		contentTxt.setTypeface(dialogFont);
		contentTxt.setText(content);
		
		Button okButton = (Button)findViewById(R.id.tutorial_ok_btn);
		okButton.setTypeface(dialogFont);
		okButton.setText(okBtnTxt);
		okButton.setOnClickListener(new View.OnClickListener()
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
	}
}
