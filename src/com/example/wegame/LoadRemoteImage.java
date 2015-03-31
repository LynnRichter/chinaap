package com.example.wegame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * 遠端圖片類
 * */
public class LoadRemoteImage {

	private ExecutorService pool; //線程池
	
	private final int MESSAGE_OK = 1; //遠端圖片獲取成功消息
	private final int MESSAGE_ERROR = -1; //遠端圖片獲取錯誤消息
	
	private ImageBuffer imageBuffer; //圖片緩存
	
	
	/**
	 * 構造函數
	 * 執行初始化操作
	 * */
	public LoadRemoteImage() {
		pool = Executors.newCachedThreadPool();
		imageBuffer = new ImageBuffer();
	}
	
	/**
	 * 設置遠端圖片事件監聽器
	 * @param url 圖像URL地址
	 * @param listener 遠端圖片監聽器
	 * */
	public void setRemoteImageListener(final String url, final OnRemoteImageListener listener) {
		
		/*
		 * 遠端圖片消息處理Handler
		 * */
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int what = msg.what;
				switch(what) {
				case MESSAGE_OK : //成功 
					listener.onRemoteImage((Bitmap) msg.obj); //調用onRemoteImage回調方法
					break;
				case MESSAGE_ERROR : //錯誤
					listener.onError((String) msg.obj); ////調用onError回調方法
					break;
				}
			}
			
		};
		
		/*
		 * 向線程池中添加新任務
		 * 下載給定URL地址圖片
		 * */
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					Bitmap image = null;
					/*
					 * 如果圖片緩存中沒有該圖片，則下載放入緩存中
					 * */
					if((image = imageBuffer.get(url)) == null) {
						byte[] resource = httpRequestByteArray(url); //HTTP請求圖片字節數據
						image = optimizeBitmap(resource, 100, 100); //獲得優化的圖像
						imageBuffer.put(url, image);
					}
					handler.sendMessage(handler.obtainMessage(MESSAGE_OK, image)); //遠端圖像下載成功
				} catch (Exception e) {
					/*
					 * 異常處理
					 * */
					handler.sendMessage(handler.obtainMessage(MESSAGE_ERROR, e.getMessage()));
					return;
				}
			}
		});
	}
	
	/** 
     * 使用HTTP GET方式請求 
     * @param url URL地址 
     * @return HttpEntiry對象 
	 * @throws Exception 
     * */  
    private byte[] httpRequestByteArray(String url) throws Exception {  
        byte[] result = null;  
        HttpGet httpGet = new HttpGet(url);  
        HttpClient httpClient = new DefaultHttpClient();  
        HttpResponse httpResponse;  
        httpResponse = httpClient.execute(httpGet);  
        int httpStatusCode = httpResponse.getStatusLine().getStatusCode();  
        /* 
         * 判斷HTTP狀態碼是否為200 
         * */  
        if(httpStatusCode == HttpStatus.SC_OK) {  
            result = EntityUtils.toByteArray(httpResponse.getEntity());  
        } else {
        	throw new Exception("HTTP: "+httpStatusCode);
        }
        return result;  
    }
	
    private Bitmap optimizeBitmap(byte[] resource, int maxWidth, int maxHeight) {  
        Bitmap result = null;  
        int length = resource.length;  
        BitmapFactory.Options options = new BitmapFactory.Options();      
        options.inJustDecodeBounds = true;  
        result = BitmapFactory.decodeByteArray(resource, 0, length, options);  
        int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);  
        int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);  
        if(widthRatio > 1 || heightRatio > 1) {  
            if(widthRatio > heightRatio) {  
                options.inSampleSize = widthRatio;  
            } else {  
                options.inSampleSize = heightRatio;  
            }  
        }  
        options.inJustDecodeBounds = false;  
        result = BitmapFactory.decodeByteArray(resource, 0, length, options);  
        return result;  
    }
    
    /**
     * 遠端圖片監聽器
     * */
	public interface OnRemoteImageListener {
		
		/**
		 * 遠端圖片處理
		 * @param image 位圖圖片
		 * */
		void onRemoteImage(Bitmap image);
		
		/**
		 * 錯誤處理
		 * @param error 錯誤信息
		 * */
		void onError(String error);
		
	}
	
}
