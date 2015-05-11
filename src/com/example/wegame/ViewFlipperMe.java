package com.example.wegame;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ViewFlipperMe extends Activity {
	private ViewFlipper viewFlipper = null;
	private LayoutInflater mInflater;
	private EditText phoneText,pwdText;
	private static final int LOGIN_SUCCESS =0;
	private static final int LOGIN_ERROR = 1;
	private static final int LOGIN_EXC	= 2;
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_me);  
  
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        viewFlipper = (ViewFlipper) this.findViewById(R.id.me_flipper);  
        setView();
        
        
    }  
	private void setView() {  
		View vipView = (View) mInflater.inflate(R.layout.activity_vip, null); 
		viewFlipper.addView(vipView,0);
		VipUiFactory(vipView);
		
		View loginView = (View) mInflater.inflate(R.layout.activity_login, null); 
		viewFlipper.addView(loginView,1);
		loginUIFactory(loginView);
//		Log.d(getString(R.string.log_tag), "总共有几个界面"+viewFlipper.getChildCount());
    } 
	private void showView()
	{
		if (JSONHelpler.getLogin(getApplicationContext())) {
			Log.d(getString(R.string.log_tag), "我登陆了");
			viewFlipper.setDisplayedChild(0);
		}else {
			Log.d(getString(R.string.log_tag), "我没有登陆，所以");
			viewFlipper.setDisplayedChild(1);
		}
	}
	private void VipUiFactory(View vipView)
	{
		ImageView backView = (ImageView)vipView.findViewById(R.id.VIP_back);
		backView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		TextView userTextView = (TextView)vipView.findViewById(R.id.vip_username);
		userTextView.setText(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_username)));
//		切换帐号
		TextView exchangeTextView = (TextView)vipView.findViewById(R.id.vip_exchange);
		exchangeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(ViewFlipperMe.this)
				.setTitle("提示")
				.setMessage("确认切换帐号？")
				.setPositiveButton("是",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent();
						intent.setClass(ViewFlipperMe.this, LoginActivity.class);
						startActivity(intent);
						
					}
				})
				.setNegativeButton("否", null)
				.show();
			}
		});
//		会员服务
		RelativeLayout serviceLayout = (RelativeLayout)vipView.findViewById(R.id.vip_service);
		serviceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ViewFlipperMe.this, ServiceActivity.class);
				startActivity(intent);				
			}
		});
//		关于中农
		RelativeLayout aboutLayout = (RelativeLayout)vipView.findViewById(R.id.vip_about);
		aboutLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ViewFlipperMe.this, InfoActivity.class);
				startActivity(intent);				
			}
		});
//		退出登录
		RelativeLayout logoutLayout = (RelativeLayout)vipView.findViewById(R.id.vip_logout);
		logoutLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(ViewFlipperMe.this)
				.setTitle("提示")
				.setMessage("确认退出帐号？")
				.setPositiveButton("是",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						loginOut();
						finish();
					}
				})
				.setNegativeButton("否", null)
				.show();
			}
		});
	}
	private void loginOut() {
		JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_username),"");
		JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_userid),"");
		JSONHelpler.setLogin(getApplicationContext(), false);
	}
	private void loginUIFactory(View viewLogin)
	{
		phoneText = (EditText)viewLogin.findViewById(R.id.login_phone);
		pwdText = (EditText)viewLogin.findViewById(R.id.login_pwd);

		ImageView backView = (ImageView)viewLogin.findViewById(R.id.login_back);
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

		Button sureButton = (Button)viewLogin.findViewById(R.id.login_login);
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
		Button registButton = (Button)viewLogin.findViewById(R.id.login_regist);
		registButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ViewFlipperMe.this, RegistActivity.class);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showView();
	}
}
