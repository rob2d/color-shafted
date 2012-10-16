package com.whateversoft.colorshafted.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class RenderView extends View
{
	public RenderView(Context context)
	{
		super(context);
	}
	
	protected void onDraw(Canvas canvas)
	{
		invalidate(); //tells the screen to be drawn again ASAP
	}
}
