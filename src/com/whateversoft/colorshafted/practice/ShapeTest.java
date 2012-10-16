package com.whateversoft.colorshafted.practice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ShapeTest extends Activity
{
	Paint paint;
	class RenderView extends View
	{
		public RenderView(Context context)
		{
			super(context);
			paint = new Paint();
		}
		
		protected void onDraw(Canvas canvas)
		{
			//fill the screen with the color white
			canvas.drawRGB(255, 255, 255);
			
			//draw a red diagonal line across the screen
			paint.setColor(Color.RED);
			canvas.drawLine(0, 0, canvas.getWidth() - 1, canvas.getHeight() - 1, paint);
			
			//draw an opaque circle in the center of the screen in green
			paint.setStyle(Style.FILL);
			paint.setColor(0xff00ff00);
			canvas.drawCircle(canvas.getWidth() /2, canvas.getHeight() / 2, 40, paint);
			
			//draw a transparent blue filled rectangle at (100,100) to (200, 200)
			paint.setStyle(Style.FILL);
			paint.setColor(0x770000ff);
			canvas.drawRect(100, 100, 200, 200, paint);
			//redraw quickly
			invalidate();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//set to fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//set the display to our renderview class
		setContentView(new RenderView(this));
	}
}
