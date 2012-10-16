package com.whateversoft.colorshafted.practice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FontTest extends Activity
{
	class RenderView extends View
	{
		Paint paint;
		Typeface font, font2;
		Rect bounds = new Rect();
		public RenderView(Context context)
		{
			super(context);
			paint = new Paint();
			font = Typeface.createFromAsset(context.getAssets(), "battlefont.ttf");
		}
		
		protected void onDraw(Canvas canvas)
		{
			paint.setColor(Color.YELLOW);
			paint.setTextSize(30);
			paint.setTypeface(font);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText("I am a fucking ninja!", canvas.getWidth() / 2, 50, paint);
			
			paint.setColor(Color.WHITE);
			paint.setTextSize(20);
			paint.getTextBounds("epic battle time", 0, 16, bounds);
			canvas.drawText("epic battle time", canvas.getWidth() - bounds.width(), 140, paint);
			invalidate();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new RenderView(this));
	}
}
