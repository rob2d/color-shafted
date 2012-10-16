package com.whateversoft.colorshafted.practice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LifeCycleTest extends Activity
{
	StringBuilder builder = new StringBuilder();
	TextView textView;
	
	//method to log text and then append it to the text shown on the activity screen!
	private void log(String text)
	{
		Log.d("LifeCycleTest", text);
		builder.append(text);
		builder.append('\n');
		textView.setText(builder.toString());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		textView = new TextView(this);	//create the text view that we'll use to show things on
		textView.setText(builder.toString());	//make the text shown as the string we're appending to
		setContentView(textView);	//set the view this activity to the textview object
		log("created");				//log that the activity is running "onCreate()"
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		log("resumed");			//log that the activity is running "onResume()"
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		log("paused");			//log that the activity is running "onPause()"
		
		if(isFinishing())
		{
			log("finishing");	//log that the activity is running "isFinishing()"
		}
	}
}
