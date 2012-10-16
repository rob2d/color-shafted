package com.whateversoft.android.framework;

public class ScreenInfo
{
	public static int 		 virtualWidth  = -1,
							 virtualHeight = -1;
	public static VirtualResolution virtualRes = VirtualResolution.R800X480;
	
	public enum VirtualResolution { R800X480, R720X480, R680X480 };
	
}
