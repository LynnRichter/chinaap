package com.example.wegame;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText phoneText,pwdText;
	private static final int LOGIN_SUCCESS =0;
	private static final int LOGIN_ERROR = 1;
	private static final int LOGIN_EXC	= 2;


	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_login, null));
		UIFactory();

	}
	private void UIFactory() {

		phoneText = (EditText)this.findViewById(R.id.login_phone);
		pwdText = (EditText)this.findViewById(R.id.login_pwd);

		ImageView backView = (ImageView)this.findViewById(R.id.login_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		//点击登录

		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case LOGIN_SUCCESS:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.login_success), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					finish();
					break;
				case LOGIN_ERROR:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.login_error), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				case LOGIN_EXC:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.error_msg), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				default:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.error_msg), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				}
			}
		};

		Button sureButton = (Button)this.findViewById(R.id.login_login);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!CheckInput()) {
					return;
				}

				Runnable threadRunnable = new Runnable() {

					@Override
					public void run() {
						Message msg = new Message();
						StringBuffer parBuffer  = new StringBuffer();
						parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&").append("client_str=").append(getString(R.string.CLIENT_STR)).append("&").append("mobile=").append(phoneText.getText().toString()).append("&").append("password=").append(pwdText.getText().toString());
						JSONObject retJsonObject = JSONHelpler.postJason(getString(R.string.URL_LOGIN),parBuffer);
						//						Log.d(getString(R.string.log_tag), "登录接口获取数据："+ipJsonObject.toString());
						try {
							String datasString = retJsonObject.getString("data");
							Log.d(getString(R.string.log_tag), "Data："+datasString);
							if (datasString.length() == 0) {
								msg.what = LOGIN_ERROR;
							}else {
								JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_username),retJsonObject.getJSONObject("data").getString("userName"));
								JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_userid),retJsonObject.getJSONObject("data").getString("userId"));
								JSONHelpler.setLogin(getApplicationContext(), true);
								msg.what = LOGIN_SUCCESS;
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							msg.what = LOGIN_EXC;
						}


						handler.sendMessage(msg);

					}
				};
				new Thread(threadRunnable).start();

			}
		});
		//点击注册
		Button registButton = (Button)this.findViewById(R.id.login_regist);
		registButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
			}
		});




	}
	private Boolean CheckInput() {
		if (phoneText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.login_mobile_empty), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		if (pwdText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.login_pwd_empty), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		return true;

	}
}
