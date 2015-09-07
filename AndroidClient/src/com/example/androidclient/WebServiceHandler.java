package com.example.androidclient;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;

public class WebServiceHandler {
	
	public WebServiceHandler() {
		
	}
	public String getJSONData(String url){
		String jsonstr="";
		try {
			HttpGet httppost = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			jsonstr = EntityUtils.toString(entity);
			return jsonstr;
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
