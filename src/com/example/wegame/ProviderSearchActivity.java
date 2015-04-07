package com.example.wegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProviderSearchActivity extends Activity {
	private ProviderAdapter listViewAdapter; 
	private ListView listView;
	private List<Map<String,String>> listItems;
	private static final int DATA_SUCCESS = 4;
	private static final int DATA_ERROR = 5;
	private static final int EXCEPTION	= 6;
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_provider_search, null));
		UIFactory();

	}
	private void UIFactory() {
		ImageView backView = (ImageView)this.findViewById(R.id.provider_search_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		listView =(ListView)findViewById(R.id.provider_search_list);
		
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case DATA_SUCCESS:
					listViewAdapter = new ProviderAdapter(ProviderSearchActivity.this, getListItems()); //创建适配器   
			        listView.setAdapter(listViewAdapter);
			        listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							HashMap<String,String> map=(HashMap<String,String>)getListItems().get(arg2);   
							Intent intent = new Intent();
							intent.setClass(ProviderSearchActivity.this, ProviderDetailActivity.class);
							intent.putExtra("Item", map);
							startActivity(intent);
							
				            
							
						}
					});
					break;
				case DATA_ERROR:
					toast = Toast.makeText(getApplicationContext(),
							(String)msg.obj, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				case EXCEPTION:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.error_msg), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;

				default:
					toast = Toast.makeText(getApplicationContext(),
							getString(R.string.error_msg), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				}
			}
		};	
	 final EditText searchText = (EditText)findViewById(R.id.provider_search_input);
	 searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
	
		@Override
		public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {  
				Log.d(getString(R.string.log_tag), "点击了确认按钮");
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
//				开启线程，搜索数据
				Runnable dataRunnable = new Runnable() {

					@Override
					public void run() {
						List<Map<String, String>> listitems = new ArrayList<Map<String, String>>();  
						setListItems(listitems);
						Message msg = new Message();
						StringBuffer parBuffer  = new StringBuffer();
						parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
						.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
						.append("providerName=").append(searchText.getText().toString());
						JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_PROVIDERSEARCH)+"?"+parBuffer.toString());
						try {
							String datasString = retJsonObject.getString("data");
//							Log.d(getString(R.string.log_tag), "Request Data："+parBuffer.toString());
//							Log.d(getString(R.string.log_tag), "ProviderData："+datasString);

							if (datasString.length() == 0) {
								msg.what = DATA_ERROR;
								msg.obj = retJsonObject.getString("info");
							}else {

								JSONArray jsonArray = retJsonObject.getJSONArray("data");
								for (int i=0;i<jsonArray.length();i++) {
									JSONObject item = jsonArray.getJSONObject(i);
									Map<String, String> map = new HashMap<String, String>();
									map.put("imageurl", item.getString("imageurl"));
									map.put("providerName", item.getString("providerName"));
									map.put("mainProduct", item.getString("mainProduct"));
									map.put("contact", item.getString("contact"));
									map.put("contactNumber", item.getString("contactNumber"));
									map.put("companyAddress", item.getString("companyAddress"));
									map.put("introduction", item.getString("introduction"));
									map.put("promise", item.getString("promise").replace("&ldquo;", "\"").replace("&rdquo;", "\""));
									getListItems().add(map);
								}
								
								msg.what = DATA_SUCCESS;
								msg.obj = retJsonObject.getString("total");

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							msg.what = EXCEPTION;

						}


						handler.sendMessage(msg);
					}
				};
				new Thread(dataRunnable).start();
            }
			return false;
		}
	});
		
	}
	public List<Map<String,String>> getListItems() {
		return listItems;
	}
	public void setListItems(List<Map<String,String>> listItems) {
		this.listItems = listItems;
	}
}
