package com.whateversoft.android.framework.impl.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.whateversoft.android.framework.FileIO;

import android.content.res.AssetManager;
import android.os.Environment;

/** Copyright 2011 Robert Concepcion III */
/** Used to read and write files either from an external storage device or from our Assets manager*/
public class AndroidFileIO implements FileIO
{
	/** assets used to open the file to be read */
	AssetManager assets;
	/** stores the path of the external storage on the phone */
	String externalStoragePath;
	
	public AndroidFileIO(AssetManager a)
	{
		//asign assets and then assigns the external storage path for quick reference
		assets = a;
		externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	/** returns an input stream used for reading a file from the phone's external SD */
	@Override
	public InputStream readFile(String fileName) throws IOException
	{
		return new FileInputStream(externalStoragePath + fileName);
	}
	
	/** returns an input stream to read a file from an asset */
	@Override
	public InputStream readAsset(String fileName) throws IOException
	{
		return assets.open(fileName);
	}
	
	/** returns an output stream to write to a file from the phone's external SD*/
	@Override
	public OutputStream writeFile(String fileName) throws IOException
	{
		return new FileOutputStream(externalStoragePath + fileName);
	}
}
