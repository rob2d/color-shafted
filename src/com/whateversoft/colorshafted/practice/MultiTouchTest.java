package com.whateversoft.colorshafted.practice;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MultiTouchTest extends Activity implements OnTouchListener
{
	StringBuilder builder = new StringBuilder();
	TextView textView;
	float[] x = new float[10];
	float[] y = new float[10];
	boolean[] touched = new boolean[10];
	
	private void updateTextView()
	{
		builder.setLength(0);
		for(int i = 0; i < 10; i++)
		{
			builder.append(touched[i]);
			builder.append(", ");
			builder.append(x[i]);
			builder.append(", ");
			builder.append(y[i]);
			builder.append("\n");
		}
		textView.setText(builder.toString());
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		textView = new TextView(this);
		textView.setText("Touch and drag (multiple fingers supported)!");
		textView.setOnTouchListener(this);
		setContentView(textView);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		int action = e.getAction() & MotionEvent.ACTION_MASK;
		/** Keeps track of the pointerIndex, so we can call getX(index), getY(index) and get the pointer id. To retrieve, we perform an AND bitwise op with the action retrieved, and then bitshift right POINTER_ID(index) # of bits */
		int pointerIndex = (e.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) 
		>> MotionEvent.ACTION_POINTER_ID_SHIFT;
		int pointerId = e.getPointerId(pointerIndex);
		
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			touched[pointerId] = true;
			x[pointerId] = (int) e.getX(pointerIndex);
			y[pointerId] = (int) e.getY(pointerIndex);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			touched[pointerId] = false;
			x[pointerId] = (int) e.getX(pointerIndex);
			y[pointerId] = (int) e.getY(pointerIndex);
			break;
		case MotionEvent.ACTION_MOVE:
			int pointerCount = e.getPointerCount();
			for(int i = 0; i < pointerCount; i++)
			{
				pointerIndex = i;
				pointerId = e.getPointerId(pointerIndex);
				x[pointerId] = (int) e.getX(pointerIndex);
				y[pointerId] = (int) e.getY(pointerIndex);
			}
			break;
		}
		updateTextView();
		return true;
	}
}
