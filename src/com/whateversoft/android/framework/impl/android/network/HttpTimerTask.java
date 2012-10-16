package com.whateversoft.android.framework.impl.android.network;

import android.os.AsyncTask;
import android.util.Log;

/** Thread used to track how long an HTTPRequest has been taking. 
 *  If it takes too long, we send a message to the calling HttpRequest task to cancel itself
 *  (which then calls the associated calling app to display that there was a problem) */
public class HttpTimerTask extends AsyncTask<Void, Void, Void>
{
	long serverTimeoutMs = 10000;	//JUST A BANDAID FOR THIS PROJECT. BAD PRACTICE ._. change
																//for any future projects using the Network API
	long requestTime;
	
	HttpRequestTask callerRequest;
	
	public HttpTimerTask(HttpRequestTask caller)
	{
		callerRequest = caller;
		Log.d("HttpRequests", "set an HttpTimer task based on the caller requester to timeout of " +caller.serverTimeoutMs);
		serverTimeoutMs = caller.serverTimeoutMs;
	}
	@Override
	protected Void doInBackground(Void... params)
	{
		Log.d("HttpRequests", "Started a timer task with a request timeout of " + serverTimeoutMs);
		long startRequestTime = System.currentTimeMillis();
		while(true)
		{
			//get the time elapsed on this thread
			requestTime = System.currentTimeMillis() - startRequestTime;
			if(!isCancelled() && requestTime > serverTimeoutMs)
			{	
				callerRequest.cancel(true);
				break;
			}
		}
		return null;
	}
	
	@Override
	public void onCancelled()
	{
		super.onCancelled();
	}
}
