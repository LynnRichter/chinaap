package com.example.wegame;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ProviderDetailActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_provider_detail, null));
		UIFactory();

	}
	private void UIFactory() {
		
		ImageView backView = (ImageView)this.findViewById(R.id.provider_detail_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		final HashMap<String,String> map=(HashMap<String,String>)getIntent().getSerializableExtra("Item");
		
		TextView titleView = (TextView)findViewById(R.id.provider_detail_title);
		titleView.setText(map.get("providerName"));
		
		TextView mainView = (TextView)findViewById(R.id.provider_detail_mainp);
		mainView.setText(map.get("mainProduct"));
		
		TextView contactView =(TextView)findViewById(R.id.provider_detail_contact);
		contactView.setText(map.get("contact"));
		
		TextView phoneView =(TextView)findViewById(R.id.provider_detail_phone);
		phoneView.setText(map.get("contactNumber"));
		phoneView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+map.get("contactNumber")));  
				startActivity(intent);  
			}
		});

		TextView addressView = (TextView)findViewById(R.id.provider_detail_address);
		addressView.setText(map.get("companyAddress"));
		
		TextView introView =(TextView)findViewById(R.id.provider_detail_intro);
		introView.setText(map.get("introduction"));
		
		TextView promiseView =(TextView)findViewById(R.id.provider_detail_promise);
		promiseView.setText(map.get("promise"));
		
		
	}
}
