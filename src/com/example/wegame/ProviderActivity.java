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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProviderActivity extends Activity {
	private Spinner citySpinner;
	private List<String> citylist = new ArrayList<String>(); 
	private List<String> cityidlist = new ArrayList<String>();
	private ArrayAdapter<String> cityAdapter;   
	private Runnable cityRunnable = null;

	private Spinner typeSpinner;
	private List<String> typelist = new ArrayList<String>(); 
	private List<String> typeidlist = new ArrayList<String>();
	private ArrayAdapter<String> typeAdapter;   
	private Runnable typeRunnable = null;

	private static final int CITY_SUCCESS =0;
	private static final int CITY_ERROR = 1;
	private static final int TYPE_SUCCESS = 2;
	private static final int TYPE_ERROR = 3;
	private static final int DATA_SUCCESS = 4;
	private static final int DATA_ERROR = 5;
	private static final int EXCEPTION	= 6;

	private Handler dataHandler = null;
	private String tyepID,cityID,typeName,cityName;
	private ProviderAdapter listViewAdapter; 
	private ListView listView;
	private List<Map<String,String>> listItems;

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_provider, null));
		UIFactory();

	}
	private void UIFactory() {
		citySpinner = (Spinner)findViewById(R.id.provider_spinner_city);
		typeSpinner = (Spinner)findViewById(R.id.provider_spinner_category);
		final TextView city_typeTextView=(TextView)findViewById(R.id.provider_city_type);
		final TextView countTextView =(TextView)findViewById(R.id.provider_count);
		listView = (ListView)findViewById(R.id.provider_list);
		

		

		ImageView backView = (ImageView)this.findViewById(R.id.provider_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		ImageView searchView =(ImageView)findViewById(R.id.provider_search);
		searchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ProviderActivity.this, ProviderSearchActivity.class);
				startActivity(intent);
			}
		});

		final Handler cityHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case CITY_SUCCESS:
					cityAdapter = new ArrayAdapter<String>(ProviderActivity.this, android.R.layout.simple_spinner_item, citylist);
					cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
					//第四步：将适配器添加到下拉列表上    
					citySpinner.setAdapter(cityAdapter);
					citySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
					{    
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    

							setCityID(cityidlist.get(arg2)); 
							setCityName(cityAdapter.getItem(arg2));
							//							Toast toast = Toast.makeText(getApplicationContext(),
							//									"您选择的城市是："+ cityAdapter.getItem(arg2)+"& ID= "+cityidlist.get(arg2), Toast.LENGTH_LONG);
							//							toast.setGravity(Gravity.CENTER, 0, 0);
							//							toast.show();
							arg0.setVisibility(View.VISIBLE);   
							typelist.clear();
							typeidlist.clear();
							new Thread(typeRunnable).start();

						}    
						public void onNothingSelected(AdapterView<?> arg0) {    
							arg0.setVisibility(View.VISIBLE);    
						}

					});   

					break;
				case CITY_ERROR:
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

					break;
				}
			}
		};

		cityRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR));
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_CITYINFO)+"?"+parBuffer.toString());
				try {
					String datasString = retJsonObject.getString("data");
					//					Log.d(getString(R.string.log_tag), "cityData："+datasString);
					if (datasString.length() == 0) {
						msg.what = CITY_ERROR;
						msg.obj = retJsonObject.getString("info");
					}else {

						JSONArray jsonArray = retJsonObject.getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							citylist.add(item.getString("shortName"));
							cityidlist.add(item.getString("id"));
						}
						msg.what = CITY_SUCCESS;


					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = EXCEPTION;

				}


				cityHandler.sendMessage(msg);

			}
		};

		new Thread(cityRunnable).start();


		final Handler typeHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case TYPE_SUCCESS:
					typeAdapter = new ArrayAdapter<String>(ProviderActivity.this, android.R.layout.simple_spinner_item, typelist);
					typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
					//第四步：将适配器添加到下拉列表上    
					typeSpinner.setAdapter(typeAdapter);
					typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
					{    
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    

							setTyepID(typeidlist.get(arg2)); 
							setTypeName(typeAdapter.getItem(arg2));
							//							Toast toast = Toast.makeText(getApplicationContext(),
							//									"您选择的类别是："+ typeAdapter.getItem(arg2)+"& ID= "+typeidlist.get(arg2), Toast.LENGTH_LONG);
							//							toast.setGravity(Gravity.CENTER, 0, 0);
							//							toast.show();
							arg0.setVisibility(View.VISIBLE); 

						}    
						public void onNothingSelected(AdapterView<?> arg0) {    
							arg0.setVisibility(View.VISIBLE);    
						}

					});   
					setTyepID("1");
					startLoad();
					break;
				case TYPE_ERROR:
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
		typeRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
				.append("cityid=").append(getCityID());
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_TYPEINFO)+"?"+parBuffer.toString());
				try {
					String datasString = retJsonObject.getString("data");
					//					Log.d(getString(R.string.log_tag), "typeData："+datasString);
					if (datasString.length() == 0) {
						msg.what = TYPE_ERROR;
						msg.obj = retJsonObject.getString("info");
					}else {

						JSONArray jsonArray = retJsonObject.getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							typelist.add(item.getString("name"));
							typeidlist.add(item.getString("id"));
						}
						msg.what = TYPE_SUCCESS;

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = EXCEPTION;

				}


				typeHandler.sendMessage(msg);

			}
		};


		Button searchButton = (Button)findViewById(R.id.provider_button_start);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startLoad();
			}
		});

		dataHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = null;
				switch (msg.what) {
				case DATA_SUCCESS:
					city_typeTextView.setText(getCityName()+getTypeName()+"供应商有");
					countTextView.setText((String)msg.obj);
					listViewAdapter = new ProviderAdapter(ProviderActivity.this, getListItems()); //创建适配器   
			        listView.setAdapter(listViewAdapter);
			        listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							HashMap<String,String> map=(HashMap<String,String>)getListItems().get(arg2);   
							Intent intent = new Intent();
							intent.setClass(ProviderActivity.this, ProviderDetailActivity.class);
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


	}
	private void startLoad()
	{
		
		Runnable dataRunnable = new Runnable() {

			@Override
			public void run() {
				List<Map<String, String>> listitems = new ArrayList<Map<String, String>>();  
				setListItems(listitems);
				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
				.append("cityId=").append(getCityID()).append("&")
				.append("categoryId=").append(getTyepID());
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_PROVIDERLIST)+"?"+parBuffer.toString());
				try {
					String datasString = retJsonObject.getString("data");
					//					Log.d(getString(R.string.log_tag), "Request Data："+parBuffer.toString());
					//					Log.d(getString(R.string.log_tag), "ProviderData："+datasString);

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
							map.put("promise", item.getString("promise"));
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


				dataHandler.sendMessage(msg);
			}
		};
		new Thread(dataRunnable).start();
	}
	public String getTyepID() {
		return tyepID;
	}
	public void setTyepID(String tyepID) {
		this.tyepID = tyepID;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public List<Map<String,String>> getListItems() {
		return listItems;
	}
	public void setListItems(List<Map<String,String>> listItems) {
		this.listItems = listItems;
	}

}
