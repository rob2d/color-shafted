package com.whateversoft.android.framework.impl.android.network;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

/** contains global access variables to help set up Http requests without redundant objects in an app */
public class HttpTools
{
	public static Gson gson = new GsonBuilder()
						.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
						.create();
	public static HttpClient client = new DefaultHttpClient();
	public static HTMLParser htmlParser = new HTMLParser();
}