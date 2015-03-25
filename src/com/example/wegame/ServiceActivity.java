package com.example.wegame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_service, null));
		UIFactory();
	}
	protected void UIFactory() {
		ImageView backView = (ImageView)this.findViewById(R.id.service_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});

		TextView dailTextView = (TextView)this.findViewById(R.id.service_dail);
		dailTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:4001146668"));  
				startActivity(intent);  
			}
		});
	}
}
