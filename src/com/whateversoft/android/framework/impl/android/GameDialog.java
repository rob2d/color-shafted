package com.whateversoft.android.framework.impl.android;

import android.app.Dialog;
import android.content.Context;

import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.R;

public class GameDialog extends Dialog
{
	CanvasGame context;
	public GameDialog(CanvasGame context, int style)
	{
		super(context, style);
		this.context = context;
		context.pauseGame();
	}
	
	@Override
	public void onStop()
	{
		context.resumeGame();
		super.onStop();
	}
}
