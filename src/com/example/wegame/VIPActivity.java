package com.example.wegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VIPActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_vip, null));
		UIFactory();
	}
	protected void UIFactory() {
		ImageView backView = (ImageView)this.findViewById(R.id.VIP_back);
		backView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		TextView userTextView = (TextView)findViewById(R.id.vip_username);
		userTextView.setText(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_username)));
//		切换帐号
		TextView exchangeTextView = (TextView)findViewById(R.id.vip_exchange);
		exchangeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
//		会员服务
		RelativeLayout serviceLayout = (RelativeLayout)findViewById(R.id.vip_service);
		serviceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(VIPActivity.this, ServiceActivity.class);
				startActivity(intent);				
			}
		});
//		关于中农
		RelativeLayout aboutLayout = (RelativeLayout)findViewById(R.id.vip_about);
		aboutLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(VIPActivity.this, InfoActivity.class);
				startActivity(intent);				
			}
		});
//		退出登录
		RelativeLayout logoutLayout = (RelativeLayout)findViewById(R.id.vip_logout);
		logoutLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
							
			}
		});
	}
}
