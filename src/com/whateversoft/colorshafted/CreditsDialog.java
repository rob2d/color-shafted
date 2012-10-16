package com.whateversoft.colorshafted;

import android.app.Dialog;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.colorshafted.ColorShaftedPrompter.GameResultAction;
import com.whateversoft.R;

public class CreditsDialog extends GameDialog
{
	Typeface dialogFont;
	
	public CreditsDialog(final PrompterAndroid prompter, final Thread threadRunning)
	{
		super(prompter.context, R.style.WireframeGreenSemiTrans);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.credits_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");

		ColorShaftedPrompter.applyFonts(findViewById(R.id.credits_dialog), dialogFont);
		Button okButton = (Button)findViewById(R.id.credits_ok_btn);
		okButton.setText(new StringBuilder("BACK"));
		okButton.setOnClickListener(new View.OnClickListener()
		{
			/* DEFINE BUTTON 1's BEHAVIOR */
			@Override
			public void onClick(View v)
			{
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
