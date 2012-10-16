package com.whateversoft.colorshafted.practice;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class AccelerometerTest extends Activity implements SensorEventListener
{
	TextView textView;
	StringBuilder builder = new StringBuilder();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		textView = new TextView(this);
		setContentView(textView);
		
		SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0)
		{
			textView.setText("No accelerometer installed");
		}
		else
		{
			Sensor accelerometer = (manager.getSensorList(Sensor.TYPE_ACCELEROMETER)).get(0);	
			if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME))
				textView.setText("Couldn't register sensor listener");
		}
				
	}
	
	public void onSensorChanged(SensorEvent e)
	{
		builder.setLength(0);
		builder.append("x: ");
		builder.append(e.values[0]);

		builder.append("y: ");
		builder.append(e.values[1]);

		builder.append("z: ");
		builder.append(e.values[2]);
		textView.setText(builder.toString());
	}
	
	public void onAccuracyChanged(Sensor s, int i)
	{
		//nothing to do here
	}
}
