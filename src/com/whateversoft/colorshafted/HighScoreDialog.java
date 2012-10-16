package com.whateversoft.colorshafted;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.whateversoft.android.framework.impl.android.GameDialog;
import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.R;

public class HighScoreDialog extends GameDialog implements TextWatcher
{
	Typeface dialogFont;
	final Button okButton;
	final CheckBox useDefaultName;
	final EditText userInput;
	final PrompterAndroid prompter;
	final Thread		  threadRunning;
	
	public HighScoreDialog(final PrompterAndroid prompter, final Thread threadRunning)
	{
		super(prompter.context, R.style.TestStyle);
		
		this.prompter = prompter;
		this.threadRunning = threadRunning;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.highscore_dialog);
		setOwnerActivity(prompter.context);		//attach prompt to the game window
		setCancelable(false);						//disable prompts exits
		
		dialogFont = Typeface.createFromAsset(prompter.context.getAssets(), "fonts/sfintellivised.ttf");
		
		final TextView enterNameTV = (TextView)findViewById(R.id.prompt_highscore_entername_text);
		enterNameTV.setTypeface(dialogFont);
		
		final TextView defaultNameTV = ((TextView)findViewById(R.id.prompt_highscore_usedefaultname));
		defaultNameTV.setTypeface(dialogFont);
		
		userInput = (EditText)findViewById(R.id.prompt_highscore_nameedit);
		userInput.setBackgroundResource(R.drawable.wireframe_green_trans);
		userInput.setTextColor(Color.rgb(0, 255, 0));
		userInput.setTypeface(dialogFont);
		userInput.setText(new StringBuilder(((PrompterAndroid)prompter).context.getPreferences().getPref(CSSettings.KEY_HIGHSCORE_NAME, "")));
		userInput.addTextChangedListener(this);
		
		userInput.setOnKeyListener(new View.OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER))
		        {
		        	//REGISTER THE OKAY AND RETURN TO GAME!
		        	//------------------------------------------------------
		    		prompter.userInput = userInput.getText().toString();
					prompter.promptCompleted = true;
					
					//set the prompt again preference off or not
					if(!useDefaultName.isChecked())
					{
						prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, true);
						prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_NAME, userInput.getText().toString());
					}
					else
					{
						prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, false);
					}
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
				//-----------------------------------------------------------------------
					return true;	//consume the key event
		        }
		        return false;		//otherwise nothing was registered, do not consume the key event from the UI and return
			}
		});
		
		
		useDefaultName = (CheckBox)findViewById(R.id.prompt_highscore_cb_dontaskagain);
		useDefaultName.setChecked(!prompter.context.getPreferences().getPref(CSSettings.KEY_HIGHSCORE_PROMPT, 
															 CSSettings.DEFAULT_HIGHSCORE_PROMPT));
		
		okButton = (Button)findViewById(R.id.prompt_highscore_next_button);
		okButton.setTypeface(dialogFont);
		okButton.setEnabled(false);			//SUBMIT BUTTON IS NOT AVAILABLE AT THE START OF THE DIALOG!(user must give name first :b)
		okButton.setTextColor(Color.rgb(30, 30, 30));
		okButton.setBackgroundResource(R.drawable.wireframe_disabled_trans);
		
		okButton.setOnClickListener(new NextBtnListener());
		enableDisableSubmitButton();
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{	
		enableDisableSubmitButton();
	}
	
	public void enableDisableSubmitButton()
	{
		if(userInput.length() > 0)
		{
			okButton.setEnabled(true);
			okButton.setTextColor(Color.rgb(0,200,0));
			okButton.setBackgroundResource(R.drawable.wireframe_green_trans);
		}
		else
		{
			okButton.setEnabled(false);
			okButton.setTextColor(Color.rgb(30, 30, 30));
			okButton.setBackgroundResource(R.drawable.wireframe_disabled_trans);
		}
	}
	
	class NextBtnListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if(okButton.isClickable())
			{
				prompter.userInput = userInput.getText().toString();
				prompter.promptCompleted = true;
				
				//set the prompt again preference off or not
				if(!useDefaultName.isChecked())
				{
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, true);
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_NAME, userInput.getText().toString());
				}
				else
				{
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, false);
				}
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
		}
	}
	
	class SubmitOLBtnListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if(okButton.isClickable())
			{
				prompter.userInput = userInput.getText().toString();
				prompter.promptCompleted = true;
				
				//set the prompt again preference off or not
				if(!useDefaultName.isChecked())
				{
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, true);
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_NAME, userInput.getText().toString());
				}
				else
				{
					prompter.context.getPreferences().setPref(CSSettings.KEY_HIGHSCORE_PROMPT, false);
				}
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
		}
	}
}

