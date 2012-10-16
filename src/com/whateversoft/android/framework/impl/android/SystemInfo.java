package com.whateversoft.android.framework.impl.android;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class SystemInfo
{
	private static boolean isGoogleTV 	= false;
	private static boolean hasTouchInput = false;
	
	/** set global attributes which make it easy to test for certain device features */
	public static void initialize(Context context)
	{
		PackageManager packageManager = context.getPackageManager();
		hasTouchInput = packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
		isGoogleTV    = packageManager.hasSystemFeature("com.google.android.tv");
		
		FeatureInfo[] features = packageManager.getSystemAvailableFeatures();
		
		for(FeatureInfo feature : features)
			Log.d("SystemInfo", feature.toString());
		
		Log.d("SystemInfo", "isGoogleTV = " + isGoogleTV + ", hasTouchInput = " + hasTouchInput);
	}
	
	public static boolean isGoogleTV()
	{
		return isGoogleTV;
	}
	
	public static boolean hasTouchInput()
	{
		return hasTouchInput;
	}
}
