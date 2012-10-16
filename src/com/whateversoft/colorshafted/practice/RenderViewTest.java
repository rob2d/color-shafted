package com.whateversoft.colorshafted.practice;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class RenderViewTest extends Activity
{
	class RenderView extends View
	{
		Random rand = new Random();
		public RenderView(Context context)
		{
			super(context);
		}
		
		protected void onDraw(Canvas canvas)
		{
			//simulate drawing grey static onto the canvas!
			int brightness = rand.nextInt(256);
			canvas.drawRGB(brightness, brightness, brightness);
			Paint color = new Paint();
			brightness += 50;
			color.setARGB(255, brightness, brightness, brightness);
			for(int i = 0; i < 5000; i++)
			{
				canvas.drawPoint(rand.nextInt(canvas.getWidth()), rand.nextInt(canvas.getHeight()), color);
			}
			invalidate();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//request no title bar in the activity window
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		
		//request for no Android notifications bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	
		
		setContentView(new RenderView(this));
	}
}
