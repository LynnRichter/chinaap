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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
					Log.d(getString(R.string.log_tag), "获取到了IP地址:"+JSONHelpler.GetNetIp());
					StringBuffer parBuffer  = new StringBuffer();
					parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&").append("client_str=").append(getString(R.string.CLIENT_STR)).append("&").append("ip=").append(JSONHelpler.GetNetIp());
					JSONObject ipJsonObject = JSONHelpler.getJason(getString(R.string.URL_IPINFO)+"?"+parBuffer);
					Log.d(getString(R.string.log_tag), "当前所在城市："+ipJsonObject.getJSONObject("data").getString("city"));
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
//点击搜索按钮
		ImageView searchBtn =(ImageView)this.findViewById(R.id.main_search_btn);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		final EditText editText =(EditText)findViewById(R.id.main_search);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				Intent intent = new Intent();
				intent.putExtra("InputKey", editText.getText().toString());
				intent.setClass(MainActivity.this, PriceSearchResultActivity.class);
				startActivity(intent);
				return false;
			}
		});
//		点击查价格
		RelativeLayout priceLayout =(RelativeLayout)findViewById(R.id.main_price);
		priceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, TabMainActivity.class);
				intent.putExtra("id", "0");
				startActivity(intent);
			}
		});
//		点击找供应商
		RelativeLayout providerLayout =(RelativeLayout)findViewById(R.id.main_provider);
		providerLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, TabMainActivity.class);
				intent.putExtra("id", "1");
				startActivity(intent);
			}
		});
//		点击关于中农按钮
		RelativeLayout aboutBtn = (RelativeLayout)this.findViewById(R.id.main_about);
		aboutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, InfoActivity.class);
				startActivity(intent);
			}
		});
//		点击会员服务
		RelativeLayout serviceLayout = (RelativeLayout)this.findViewById(R.id.main_service);
		serviceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ServiceActivity.class);
				startActivity(intent);
			}
		});
//		点击注册登录
		RelativeLayout loginLayout = (RelativeLayout)this.findViewById(R.id.main_regist);
		loginLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, TabMainActivity.class);
				intent.putExtra("id", "3");
				startActivity(intent);			
			}
		});
		RelativeLayout listLayout =(RelativeLayout)this.findViewById(R.id.main_list);
		listLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =new Intent();
				intent.setClass(MainActivity.this, TabMainActivity.class);
				intent.putExtra("id", "2");
				startActivity(intent);
			}
		});
	}
}
