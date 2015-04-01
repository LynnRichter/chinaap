package com.example.wegame;

import java.net.Socket;
import java.nio.Buffer;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.wegame.R;
import com.example.wegame.R.string;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView locationTextView;
	private static final int GETCITY = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));
		uiFactory();

	}
	private void uiFactory() {
		locationTextView = (TextView)this.findViewById(R.id.main_city);
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what) {
				case 0:
					locationTextView.setText((String) msg.obj);
					break;

				default:
					break;
				}
			}
		};

		Runnable threadRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					Log.d(getString(R.string.log_tag), "��ȡ����IP��ַ:"+JSONHelpler.GetNetIp());
					StringBuffer parBuffer  = new StringBuffer();
					parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&").append("client_str=").append(getString(R.string.CLIENT_STR)).append("&").append("ip=").append(JSONHelpler.GetNetIp());
					JSONObject ipJsonObject = JSONHelpler.getJason(getString(R.string.URL_IPINFO)+"?"+parBuffer);
					Log.d(getString(R.string.log_tag), "��ǰ���ڳ��У�"+ipJsonObject.getJSONObject("data").getString("city"));
					Message msg = new Message();
					msg.what = GETCITY;
					msg.obj = ipJsonObject.getJSONObject("data").getString("city");
					handler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		new Thread(threadRunnable).start();
//���������ť
		ImageView searchBtn =(ImageView)this.findViewById(R.id.main_search_btn);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d(getString(R.string.log_tag), "I clicked the search btn");
			}
		});
//		�����۸�
		RelativeLayout priceLayout =(RelativeLayout)findViewById(R.id.main_price);
		priceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, PriceActivity.class);
				startActivity(intent);
			}
		});
//		����ҹ�Ӧ��
		RelativeLayout providerLayout =(RelativeLayout)findViewById(R.id.main_provider);
		providerLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, ProviderActivity.class);
				startActivity(intent);
			}
		});
//		���������ũ��ť
		RelativeLayout aboutBtn = (RelativeLayout)this.findViewById(R.id.main_about);
		aboutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, InfoActivity.class);
				startActivity(intent);
			}
		});
//		�����Ա����
		RelativeLayout serviceLayout = (RelativeLayout)this.findViewById(R.id.main_service);
		serviceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ServiceActivity.class);
				startActivity(intent);
			}
		});
//		���ע���¼
		RelativeLayout loginLayout = (RelativeLayout)this.findViewById(R.id.main_regist);
		loginLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
//				�ж��Ƿ��¼���ʺ�
				if (JSONHelpler.getLogin(getApplicationContext())) {
					intent.setClass(MainActivity.this, VIPActivity.class);
				}
				else {
					intent.setClass(MainActivity.this, LoginActivity.class);
				}
				startActivity(intent);				
			}
		});
		
	}
}
