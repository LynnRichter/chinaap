package com.example.wegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {

	private EditText phoneText,pwdText;
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_login, null));
		UIFactory();

	}
	private void UIFactory() {
		ImageView backView = (ImageView)this.findViewById(R.id.login_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		//µã»÷µÇÂ¼
		Button sureButton = (Button)this.findViewById(R.id.login_login);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		//µã»÷×¢²á
		Button registButton = (Button)this.findViewById(R.id.login_regist);
		registButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
			}
		});
		
		phoneText = (EditText)this.findViewById(R.id.login_phone);
		pwdText = (EditText)this.findViewById(R.id.login_pwd);


	}
}
