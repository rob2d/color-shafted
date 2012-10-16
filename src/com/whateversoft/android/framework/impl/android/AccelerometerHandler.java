package com.whateversoft.android.framework.impl.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/** Copyright 2011 Robert Concepcion III */
public class AccelerometerHandler implements SensorEventListener
{
	float accelX, accelY, accelZ;
	
	public AccelerometerHandler(Context context)
	{
		//obtain the accelerometer device if available
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
		{
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);	//
			manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);	//add listener for accelerometer to listen at rate
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		//nothing to do here...
	}
	
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}
	
	public float getAccelX()
	{
		return accelX;
	}
	
	public float getAccelY()
	{
		return accelY;
	}
	
	public float getAccelZ()
	{
		return accelZ;
	}
}
