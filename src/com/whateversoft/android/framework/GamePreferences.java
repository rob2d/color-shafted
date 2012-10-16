package com.whateversoft.android.framework;

public interface GamePreferences
{
	public int getPref(String optionKey, int defaultValue);
	public long getPref(String optionKey, long defaultValue);
	public boolean getPref(String optionKey, boolean defaultValue);
	public String getPref(String optionKey, String defaultValue);
	public void setPref(String optionKey, int value);
	public void setPref(String optionKey, long value);
	public void setPref(String optionKey, boolean value);
	public void setPref(String optionKey, String value);
}
