package com.whateversoft.colorshafted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DebugStarter extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ScrollView   mainView   = new ScrollView(this);
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		//---------ZOOM OPTION ROW----------------------//
		//set up row
		LinearLayout optZoomIntroRow = new LinearLayout(this);
		optZoomIntroRow.setOrientation(LinearLayout.HORIZONTAL);
		//set up components
		TextView	 optZoomIntroTxt = new TextView(this);
		optZoomIntroTxt.setText(new StringBuilder("Enable Zoom-Out Intro Effect"));
		CheckBox	 optZoomIntroCB  = new CheckBox(this);
		optZoomIntroCB.setChecked(true);
		//add components to view...
		optZoomIntroRow.addView(optZoomIntroTxt);
		optZoomIntroRow.addView(optZoomIntroCB);
		//-----------------------------------------------//
		
		//---------TEXT OPTION ROW----------------------//
		//set up row
		LinearLayout optTextIntroRow = new LinearLayout(this);
		optTextIntroRow.setOrientation(LinearLayout.HORIZONTAL);
		//set up components
		TextView	 optTextIntroTxt = new TextView(this);
		optTextIntroTxt.setText(new StringBuilder("Display Scrolling Intro Text"));
		CheckBox	 optTextIntroCB  = new CheckBox(this);
		optTextIntroCB.setChecked(true);
		optTextIntroCB.setGravity(Gravity.RIGHT);
		//add components to view..
		optTextIntroRow.addView(optTextIntroTxt);
		optTextIntroRow.addView(optTextIntroCB);
		//-----------------------------------------------//
		
		//---------START APP BUTTON----------------------//
		Button startBtn = new Button(this);
		startBtn.setText("START APP!");
		ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		startBtn.setGravity(Gravity.CENTER);
		startBtn.setLayoutParams(btnParams);
		startBtn.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent i = new Intent(DebugStarter.this, ColorShafted.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					}
				});
		//-----------------------------------------------//
		
		//--------SET UP MAIN VIEW-----------------------//
		mainLayout.addView(optTextIntroRow);
		mainLayout.addView(optZoomIntroRow);
		mainLayout.addView(startBtn);
		mainView.addView(mainLayout);
		//-----------------------------------------------//
		
		//assign our view that was set up to the current activity's overall view
		setContentView(mainView);
	}
}
