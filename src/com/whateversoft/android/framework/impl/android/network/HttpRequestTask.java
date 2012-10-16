package com.whateversoft.android.framework.impl.android.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;

import android.os.AsyncTask;
import android.util.Log;


/** Used to conveniently make HttpRequests with apps that implement the HttpClientApp interface */
public abstract class HttpRequestTask extends AsyncTask<String, Void, String>
{
	HttpClientApp callerApp;
	String TAG = "HttpRequests";
	/** a reference to an associated thread that tracks how long the HTTP Request is taking */
	HttpTimerTask timerTask;

	//DATA THAT DEFINES OUR URL REQUEST
	int			  		httpMethod;
	String		  		requestURL;
	List<NameValuePair> values = new ArrayList<NameValuePair>();
	long				serverTimeoutMs;
	
	//DATA Regarding the actual response
	long				requestTime;

	public static final int HTTP_GET  = 0,
							HTTP_POST = 1;
	
	public HttpRequestTask(HttpClientApp app, int methodUsed, long timeoutMs)
	{
		callerApp = app;
		httpMethod = methodUsed;
		serverTimeoutMs = timeoutMs;

		Log.d(TAG, "||~~~~~~ ( <HttpRequest> ) ~~~~~~||");
		Log.d(TAG, "* setting up an HttpRequest that should return a server code of " + getResponseCode());
	}
	
	@Override
	/** method where the http request task is actually performed */
	protected String doInBackground(String... params)
	{
		Log.d("HttpRequests", "HttpRequestTask has entered doInBackground()");
		//begin our HTTP request timer
		timerTask = new HttpTimerTask(this);
		timerTask.execute();
		
		//attach the URL to the request
		requestURL = params[0].toString();
		//attach the values to our post request
		for(int i = 1; i < params.length - 1; i += 2)
		{
			values.add(new BasicNameValuePair(params[i], params[i+1]));
			Log.d(TAG, "* attaching values: ( " 
						+ params[i] + ", " + params[i+1] + ") to the Http POST Request");
		}
		
		//give back the Http Response to onPostExcecute()!
		try { return makeHttpRequest(); } 
		catch (ClientProtocolException e) 
		{ Log.d(TAG, "ClientProtocolException occured!"); e.printStackTrace(); 
		  //callerApp.showServerErrorMsg(); 
		  return null;} 
		catch (IOException e)
		{ Log.d(TAG, "ClientProtocolException occured!"); e.printStackTrace(); 
		  //callerApp.showServerErrorMsg(); 
		  return null;}
	}
	
	public String makeHttpRequest() throws ClientProtocolException, IOException
	{
		Log.d("HttpRequests", "makeHttpRequest() has just been entered...");
		//create our response string
		StringBuffer responseStr = new StringBuffer();		
		
		//set up our response
		HttpResponse response = null;
		
		if(httpMethod == HTTP_POST)
		{
			HttpPost post =  new HttpPost(requestURL);		//retrieve Http post with address
			post.setEntity(new UrlEncodedFormEntity(values));	//attach parameters given
			response = HttpTools.client.execute(post);			//send our request
			Log.d(TAG, "executing a post request");
		}
		else if(httpMethod == HTTP_GET)
		{
			HttpGet get =  new HttpGet(requestURL);		//retrieve Http post with address
			HttpParams p = new BasicHttpParams();
			for(NameValuePair v : values)
				p.setParameter(v.getName(), v.getValue());
			get.setParams(p);
			response = HttpTools.client.execute(get);			//send our request
			Log.d(TAG, "executing a get request");
		}
		
		//stream in the response to a buffered reader
		BufferedReader rd = 
				new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
	
		// gather the response from the http server
		String line = null;
		while ((line = rd.readLine()) != null) responseStr.append(line);
		
		Log.d(TAG, "about to return " + responseStr + " to doInBackground()");
		timerTask.cancel(true);
		return responseStr.toString();
	}
	
	/** signal to the caller app that our HTTP request has timed out */
	@Override
	public void onCancelled()
	{
		super.onCancelled();
		Log.d("HttpRequests", "Cancel request has appropriately reached HttpRequestTask.java. " +
				"Now calling callerApp.onRequestTimeout(thisInstance)");
		callerApp.onRequestTimeout(this);
	}
	
	@Override
    protected void onPostExecute(String responseStr)
	{
		super.onPostExecute(responseStr);
		Log.d("HttpRequests", "HttpRequestTask is running an onPostExecute()...");
		//requestTime = timerTask.requestTime;		//store the amount of time it took to make this request
		//timerTask.cancel(true);						//stop our timer
		if(responseStr != null)
			callerApp.onServerResponse(this, getResponseCode(), responseStr.toString());
		else
			callerApp.onServerResponse(this, HttpClientApp.REQUEST_FAILED, null);
    }
	
	public abstract int getResponseCode();
	
	public int getRequestMethod()
	{
		return httpMethod;
	}
	
	
	public String getRequestURL()
	{
		return requestURL;
	}
	

	public List<NameValuePair> getAttachedData()
	{
		return values;
	}
	

	/** retrieve the String values written out to be attached to a URL in a GET request
	 *  or to be sent via an encoded byte array for a POST request */
	public String getDataTokenStr()
	{
		ArrayList<NameValuePair> values = (ArrayList) getAttachedData();
		String postData = new String();
		
		//attach all the values to the post data
		for(int i = 0; i < values.size(); i++)
		{
			postData += values.get(i).getName() + "=" + values.get(i).getValue();
			if(values.size() > 1 && i >= 0 && i < (values.size()-1))
				postData += "&";
		}
		return postData;
	}
	
	public long getRequestTimeMs()
	{
		return requestTime;
	}
}