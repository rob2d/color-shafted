package com.whateversoft.android.framework;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.Shape;

/** Copyright 2011 Robert Concepcion III */
public interface Graphics {
	public static enum PixmapFormat { ARGB8888, ARGB4444, RGB565 }
	public Pixmap newPixmap(String fileName, PixmapFormat format);
	public void clear(int color);
	public void drawPixel(int x, int y, int color);
	public void drawLine(int x, int y, int x2, int y2, int color);
	public void drawRect(int x, int y, int width, int height, int color, boolean fill);
	public void drawRect(Rect r, int color, boolean fill);
	public void drawShape(Path shape);
	public void drawText(String text, int x, int y, int c, int s, Typeface t, Paint.Align a);
	public void drawText(StringBuffer text, int x, int y, int c, int s, Typeface t, Paint.Align a);
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
	int srcWidth, int srcHeight);
	
	public void drawPixmapAlpha(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight, int alpha);
	
	public void drawPixmap(Pixmap pixmap, int x, int y, int aPX, int aPY);
	public void drawPixmapAlpha(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY, int alpha);
	
	public void drawPixmapRotated(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY, int angle);
	public void drawPixmapRotatedAlpha(Pixmap pixmap, int x, int y, int actionPointX, int actionPointY, int angle, int alpha);
	
	public int getWidth();
	public int getHeight();
	public void drawPixmapScaled(Pixmap pixmap, int x, int y, int actionPointX,
												int actionPointY, float scale);
	public void setBitmapBuffer(Bitmap bitmap);
	
	public void setAntiAlias(boolean antiAliasing);
	}
