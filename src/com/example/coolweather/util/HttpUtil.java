package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpConnection;

//------负责http请求-------
public class HttpUtil {
	
	public static void sendHttpRequset(final String address,final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream inputStream=connection.getInputStream();
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder response=new StringBuilder();
					String lineStr;
					while (bufferedReader.readLine()!=null) {
						lineStr=bufferedReader.readLine();
						response.append(lineStr);	
					}
					if(listener!=null){
						//回调onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					if (listener!=null) {
						//回调onError()方法
						listener.onError(e);
					}
				}finally{
					if (connection!=null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	
	
}
