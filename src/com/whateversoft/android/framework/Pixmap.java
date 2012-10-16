package com.whateversoft.android.framework;

import com.whateversoft.android.framework.Graphics.PixmapFormat;


/** Copyright 2011 Robert Concepcion III */
public interface Pixmap {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
	}