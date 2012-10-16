package com.whateversoft.android.framework.impl.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.whateversoft.android.framework.GamePreferences;

/** handles an android application's preferences; includes methods to save and load different types of values
 *  from the preference manager */

public class AndroidGamePreferences implements GamePreferences
{
	Context context;
	
	public AndroidGamePreferences(CanvasGame game)
	{
		context = game;
	}
	
	@Override
	public int getPref(String key, int defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//if the key didn't exist before, assign it within the preference file
		if(!prefs.contains(key))
		{
			Log.d("COLORSHAFTED", "The preference " + key + " did not exist when being set and was assigned " + defaultValue);
			setPref(key, defaultValue);
		}
		
		int settingValue = prefs.getInt(key, defaultValue);
		
		return settingValue;
	}	
	
	@Override
	public long getPref(String key, long defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//if the key didn't exist before, assign it within the preference file
		if(!prefs.contains(key))
		{
			Log.d("COLORSHAFTED", "The preference " + key + " did not exist when being set and was assigned " + defaultValue);
			setPref(key, defaultValue);
		}
		
		long settingValue = prefs.getLong(key, defaultValue);
		
		return settingValue;
	}
	
	@Override
	public boolean getPref(String key, boolean defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//if the key didn't exist before, assign it within the preference file
		if(!prefs.contains(key))
		{
			Log.d("COLORSHAFTED", "The preference " + key + " did not exist when being set and was assigned " + defaultValue);
			setPref(key, defaultValue);
			
		}
		
		boolean settingValue = prefs.getBoolean(key, defaultValue);
		
		return settingValue;
	}
	
	@Override
	public String getPref(String key, String defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);	
		//if the key didn't exist, assign it to the preference file
		if(!prefs.contains(key))
		{
			Log.d("COLORSHAFTED", "The preference " + key + " did not exist when being set and was assigned " + defaultValue);
			setPref(key, defaultValue);
		}
		
		String settingValue = prefs.getString(key, defaultValue);
		
		return settingValue;
	}
	
	@Override
	public void setPref(String key, int value)
	{
		Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		prefEditor.putInt(key, value);
		prefEditor.commit();
	}
	
	@Override
	public void setPref(String key, long value)
	{
		Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		prefEditor.putLong(key, value);
		prefEditor.commit();	
	}
	
	@Override
	public void setPref(String key, boolean value)
	{
		Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		prefEditor.putBoolean(key, value);
		prefEditor.commit();
	}
	
	@Override
	public void setPref(String key, String value)
	{
		Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		prefEditor.putString(key, value);
		prefEditor.commit();
	}
}
