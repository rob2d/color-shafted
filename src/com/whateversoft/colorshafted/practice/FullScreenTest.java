package com.whateversoft.colorshafted.practice;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class FullScreenTest extends SingleTouchTest
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//request for no title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//eliminate the notification bar as well
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//set up the rest of the content for the view
		super.onCreate(savedInstanceState);	
	}

}
