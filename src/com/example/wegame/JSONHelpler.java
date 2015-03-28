package com.example.wegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class JSONHelpler {
	public static JSONObject getJason(String urlString)
	{
		JSONObject jsonData = new JSONObject();
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setReadTimeout(10000);
			connection.setRequestMethod("GET");
//			connection.setDoOutput(true);
//			Log.d("","connection.getResponseCode()"+connection.getResponseCode());

			if (200 == connection.getResponseCode()) {
				InputStream instream = connection.getInputStream();
				byte[] data = new byte[1024];
				IOUtils.read(instream, data);
				String jsonStr = new String(data); 
//				Log.d("","获取到的json字符串："+jsonStr);

				jsonData = new JSONObject(jsonStr);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
	}
	public static JSONObject postJason(String urlString,StringBuffer params )
	{
		JSONObject jsonData = new JSONObject();
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setReadTimeout(10000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			byte[] bypes = params.toString().getBytes();
			connection.getOutputStream().write(bypes);// 输入参数
			Log.d("","connection.getResponseCode():"+connection.getResponseCode());

			if (200 == connection.getResponseCode()) {
				InputStream instream = connection.getInputStream();
				byte[] data = new byte[1024];
				IOUtils.read(instream, data);
				String jsonStr = new String(data); 
				Log.d("","获取到的json字符串："+jsonStr);
				jsonData = new JSONObject(jsonStr);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
	}
	public static String getLocalIpAddress()  
	{  
		try  
		{  
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
			{  
				NetworkInterface intf = en.nextElement();  
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
				{  
					InetAddress inetAddress = enumIpAddr.nextElement();  
					if (!inetAddress.isLoopbackAddress())  
					{  
						return inetAddress.getHostAddress().toString();  
					}  
				}  
			}  
		}  
		catch (SocketException ex)  
		{  
			Log.d("WifiPreference IpAddress", ex.toString());  
		}  
		return null;  
	}  
	public  static String GetNetIp()
	{
		URL infoUrl = null;
		InputStream inStream = null;
		try
		{
			//http://iframe.ip138.com/ic.asp
			//infoUrl = new URL("http://city.ip138.com/city0.asp");
			infoUrl = new URL("http://ip.3322.net/");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK)
			{ 
				inStream = httpConnection.getInputStream(); 
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) 
					strber.append(line + "\n");
				inStream.close();
				//从反馈的结果中提取出IP地址
				//	            int start = strber.indexOf("[");
				//	            int end = strber.indexOf("]", start + 1);
				//	            line = strber.substring(start + 1, end);
				return strber.toString(); 
			}
		}
		catch(MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//保存字符串
	public static void saveString(Context ctx, String key,String value) {
		SharedPreferences userPreferences = ctx.getSharedPreferences("UserInfo", 0);
		Editor editor = userPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	//读取字符串
	public static String getString(Context ctx, String key)
	{
		SharedPreferences userPreferences = ctx.getSharedPreferences("UserInfo", 0);
		return userPreferences.getString(key, "");
		
	}
//	设置登录状态
	public static void setLogin(Context ctx,boolean value)
	{
		SharedPreferences userPreferences = ctx.getSharedPreferences("UserInfo", 0);
		Editor editor = userPreferences.edit();
		editor.putBoolean("login", value);
		editor.commit();
	}
//	获取登录状态
	public static boolean getLogin(Context ctx)
	{
		SharedPreferences userPreferences = ctx.getSharedPreferences("UserInfo", 0);
		return userPreferences.getBoolean("login", false);
	}
	

}
