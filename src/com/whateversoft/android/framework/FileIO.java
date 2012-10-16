package com.whateversoft.android.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Copyright 2011 Robert Concepcion III */
public interface FileIO
{
	public InputStream readAsset(String fileName) throws IOException;
	public InputStream readFile(String fileName) throws IOException;
	public OutputStream writeFile(String fileName) throws IOException;
}
