package com.whateversoft.android.framework;

import android.content.res.AssetFileDescriptor;

/** Used to instantiate sound effects into memory<br>
 * Copyright 2011 Robert Concepcion III */
public interface Audio
{
	public AssetFileDescriptor newMusic(String fileName);
	public Sound newSound(String fileName);
}
