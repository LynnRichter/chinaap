package com.example.wegame;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistActivity extends Activity {

	private List<String> namelist = new ArrayList<String>(); 
	private List<String> IDlist = new ArrayList<String>();
	private ArrayAdapter<String> adapter;   
	private Spinner typeSpinner;
	private String tyepID;
	private EditText phoneText,pwdText,contactText,emailText,cardText;
	private boolean agree,eyeon;
	private ImageView agreeView,eyeView;
	private static final int REGIST_SUCCESS =0;
	private static final int REGIST_ERROR = 1;
	private static final int REGIST_EXC	= 2;
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_regist, null));
		UIFactory();
	}
	protected void UIFactory() {
		typeSpinner = (Spinner)findViewById(R.id.regist_type);
		phoneText = (EditText)findViewById(R.id.regist_phone);
		pwdText =  (EditText)findViewById(R.id.regist_pwd);
		eyeView = (ImageView)findViewById(R.id.regist_pwd_see);
		contactText = (EditText)findViewById(R.id.regist_contact);
		emailText = (EditText)findViewById(R.id.regist_mail);
		cardText =(EditText)findViewById(R.id.regist_card);
		agreeView =(ImageView)findViewById(R.id.regist_agres);

		setAgree(false);
		setEyeon(false);
		setTyepID("0");;
		namelist.add("请选择会员类型");
		namelist.add("其他");
		namelist.add("企事业");
		namelist.add("学校");
		namelist.add("酒店");
		namelist.add("供应商");
		IDlist.add("0");
		IDlist.add("5");
		IDlist.add("1");
		IDlist.add("2");
		IDlist.add("3");
		IDlist.add("4");
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namelist);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		//第四步：将适配器添加到下拉列表上    
		typeSpinner.setAdapter(adapter);
		typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{    
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
				// TODO Auto-generated method stub    

				//            	Toast toast = Toast.makeText(getApplicationContext(),
				//            			"您选择的是："+ adapter.getItem(arg2)+"& ID= "+IDlist.get(arg2), Toast.LENGTH_LONG);
				//				toast.setGravity(Gravity.CENTER, 0, 0);
				//				toast.show();
				setTyepID(IDlist.get(arg2)); 
				arg0.setVisibility(View.VISIBLE);    
			}    
			public void onNothingSelected(AdapterView<?> arg0) {    
				arg0.setVisibility(View.VISIBLE);    
			}

		});   
		ImageView backView = (ImageView)this.findViewById(R.id.regist_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		eyeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isEyeon()) {
					pwdText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					eyeView.setImageResource(R.drawable.ico_eye);
					setEyeon(false);
				}else {
					pwdText.setInputType(InputType.TYPE_CLASS_TEXT);
					setEyeon(true);
					eyeView.setImageResource(R.drawable.ico_eye_on);
				}


			}
		});

		agreeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isAgree()) {
					setAgree(false);
					agreeView.setImageResource(R.drawable.unselect);
				}else {
					setAgree(true);
					agreeView.setImageResource(R.drawable.select);
					Intent intent = new Intent();
					intent.setClass(RegistActivity.this, AgreementActivity.class);
					startActivity(intent);

				}
			}
		});
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case REGIST_SUCCESS:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.regist_success), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					Intent intent = new Intent();
					intent.setClass(RegistActivity.this, MainActivity.class);
					startActivity(intent);
					break;
				case REGIST_ERROR:
					toast = Toast.makeText(getApplicationContext(),
							(String)msg.obj, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				case REGIST_EXC:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.error_msg), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;

				default:
					break;
				}
			}
		};
		Button submitButton = (Button)findViewById(R.id.regist_submit);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkInput()) {
					Runnable threadRunnable = new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							StringBuffer parBuffer  = new StringBuffer();
							parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
							.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
							.append("userTypeID=").append(getTyepID()).append("&")
							.append("mobile=").append(phoneText.getText().toString()).append("&")
							.append("email=").append(emailText.getText().toString()).append("&")
							.append("password=").append(pwdText.getText().toString()).append("&")
							.append("company=").append(contactText.getText().toString()).append("&")
							.append("contact=").append(contactText.getText().toString()).append("&")
							.append("agree=").append("true");
							JSONObject retJsonObject = JSONHelpler.postJason(getString(R.string.URL_USERREGIST),parBuffer);
							try {
								String datasString = retJsonObject.getString("data");
								Log.d(getString(R.string.log_tag), "Data："+datasString);
								if (datasString.length() == 0) {
									msg.what = REGIST_ERROR;
									msg.obj = retJsonObject.getString("info");
								}else {
									JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_username),retJsonObject.getJSONObject("data").getString("userName"));
									JSONHelpler.saveString(getApplicationContext(), getString(R.string.key_userid),retJsonObject.getJSONObject("data").getString("userId"));
									JSONHelpler.setLogin(getApplicationContext(), true);
									msg.what = REGIST_SUCCESS;
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								msg.what = REGIST_EXC;
								
							}


							handler.sendMessage(msg);

						}
					};
					new Thread(threadRunnable).start();
				}


			}
		});




	}
	private boolean checkInput() {
		if (phoneText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.login_mobile_empty), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		if (pwdText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.login_pwd_empty), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		if (contactText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.regist_contact_tips), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		if (emailText.getText().toString().length() == 0) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.regist_mail_tips), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}
		if (!isAgree()) {
			Toast toast = Toast.makeText(getApplicationContext(),
					getString(R.string.regist_agree_tips), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return false;
		}


		return true;
	}
	public String getTyepID() {
		return tyepID;
	}
	public void setTyepID(String tyepID) {
		this.tyepID = tyepID;
	}
	public boolean isAgree() {
		return agree;
	}
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
	public boolean isEyeon() {
		return eyeon;
	}
	public void setEyeon(boolean eyeon) {
		this.eyeon = eyeon;
	}
}
