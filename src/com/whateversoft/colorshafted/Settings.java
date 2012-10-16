package com.whateversoft.colorshafted;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.R;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener
{		
	Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() 
	{
	    public boolean onPreferenceChange(Preference preference, Object newValue) {       
	        return true;
	    }
	};
	
	public void onCreate(Bundle state)
	{
		super.onCreate(state);
		this.addPreferencesFromResource(R.layout.settings);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
	{
		if(key.equals("bombInputMethod"))
		{
			ListPreference shakeAxisPref = (ListPreference)this.findPreference("shakeAxis");
			shakeAxisPref.setEnabled(preferences.getString("bombInputMethod", "ONSCREEN_BUTTON").equals("SHAKE_SCREEN"));
		}
		
		if(key.equals(CSSettings.KEY_ENABLE_GFX_ANTIALIAS))
		{
			ColorShafted.context.getGraphics().setAntiAlias(ColorShafted.context.getPreferences().getPref(
					CSSettings.KEY_ENABLE_GFX_ANTIALIAS, CSSettings.DEFAULT_ENABLE_GFX_ANTIALIAS));
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		//make sure anti alias setting is applied to painter for title screen
		ColorShafted.context.getGraphics().setAntiAlias(ColorShafted.context.getPreferences().getPref(
				CSSettings.KEY_ENABLE_GFX_ANTIALIAS, CSSettings.DEFAULT_ENABLE_GFX_ANTIALIAS));
		
		ColorShafted.context.getMusic().setTrackAsEnabled(0, ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_A, 
				 CSSettings.DEFAULT_PLAY_TRACK_A));
		ColorShafted.context.getMusic().setTrackAsEnabled(1, ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_B, 
				 CSSettings.DEFAULT_PLAY_TRACK_B));
		ColorShafted.context.getMusic().setTrackAsEnabled(2, ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_C,
				 CSSettings.DEFAULT_PLAY_TRACK_C));
		ColorShafted.context.getMusic().setTrackAsEnabled(3, ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_D,
				 CSSettings.DEFAULT_PLAY_TRACK_D));
		
		if(!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_A, CSSettings.DEFAULT_PLAY_TRACK_A) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_B, CSSettings.DEFAULT_PLAY_TRACK_B) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_C, CSSettings.DEFAULT_PLAY_TRACK_C) &&
				!ColorShafted.context.getPreferences().getPref(CSSettings.KEY_PLAY_TRACK_D, CSSettings.DEFAULT_PLAY_TRACK_D))
				ColorShafted.context.getPreferences().setPref(CSSettings.KEY_ENABLE_MUS, false);
	}
	
}
