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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetail extends Activity {

	private String inList;
	private TextView opView;
	private static final int SUCCESS =0;
	private static final int ERROR = 1;
	private List<Map<String,String>> priceDataList;
	private PricesAdapter pricesAdapter;
	private  List<Map<String,String>> priceHistoryList;
	private PricesHistoryAdapter priceHistoryAdapter;
	private List<Map<String,String>> providerDataList;
	private ProviderAdapter providerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_product_detail, null));
		UIFactory();
	}
	private void UIFactory()
	{

		ImageView backView = (ImageView)this.findViewById(R.id.detail_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
		List<Map<String, String>> listitems = new ArrayList<Map<String, String>>(); 
		List<Map<String, String>> historyitems = new ArrayList<Map<String, String>>(); 
		List<Map<String, String>> provideritems = new ArrayList<Map<String, String>>(); 
		setPriceDataList(listitems);
		setPriceHistoryList(historyitems);
		setProviderDataList(provideritems);
		final HashMap<String,String> map=(HashMap<String,String>)getIntent().getSerializableExtra("Item");

		opView =(TextView)findViewById(R.id.detail_opList);
		setInList( map.get("isInUserPurchaseList"));



		TextView titleView =(TextView)findViewById(R.id.detail_title);
		titleView.setText(getIntent().getSerializableExtra("CityName")+map.get("productName")+"价格");

		TextView nameView =(TextView)findViewById(R.id.detail_name);
		nameView.setText(map.get("productName"));

		TextView aliasView =(TextView)findViewById(R.id.detail_aliasname);
		aliasView.setText(map.get("alias"));

		TextView specView = (TextView)findViewById(R.id.detail_spec);
		specView.setText(map.get("spec"));

		TextView rpriceView =(TextView)findViewById(R.id.detail_rprice);
		rpriceView.setText(map.get("rprice"));

		TextView priceIndex =(TextView)findViewById(R.id.detail_priceIndex);
		priceIndex .setText(map.get("priceIndex").equals(null) ?"0" :map.get("priceIndex"));

		TextView marketShort =(TextView)findViewById(R.id.detail_marketShort);
		marketShort.setText(map.get("marketShort"));

		TextView releaseDate =(TextView)findViewById(R.id.detail_releaseDate);
		releaseDate.setText(map.get("releaseDate"));

		TextView pricesHead = (TextView)findViewById(R.id.detail_market_prices_head);
		pricesHead.setText("各地"+map.get("productName")+"批发价格");

		TextView  linehead= (TextView)findViewById(R.id.detail_line_head);
		linehead.setText(getIntent().getSerializableExtra("CityName")+map.get("productName")+"价格走势");

		TextView priceHistoryTextView =(TextView)findViewById(R.id.detail_price_history_head);
		priceHistoryTextView.setText(getIntent().getSerializableExtra("CityName")+map.get("productName")+"历史价格");

		TextView providerHead =(TextView)findViewById(R.id.detail_provider_head);
		providerHead.setText(getIntent().getSerializableExtra("CityName")+map.get("productName")+"供应商推荐");

		final NoScrollListView pricesListView =(NoScrollListView)findViewById(R.id.detail_prices_list);
		final NoScrollListView priceHistoryView =(NoScrollListView)findViewById(R.id.detail_price_history_list);
		final NoScrollListView providerView =(NoScrollListView)findViewById(R.id.detail_price_provider_list);
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{

				Intent intent  = new Intent();
				intent.setClass(ProductDetail.this, LoginActivity.class);
				startActivity(intent);
			}
		};
		final Handler listHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				Toast toast = Toast.makeText(getApplicationContext(),
						(String)msg.obj, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				switch (msg.arg2) {
				case 0:
					setInList("false");
					break;
				case 1:
					setInList("true");
					break;

				default:
					break;
				}


			}
		};


		opView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!JSONHelpler.getLogin(getApplicationContext())) {
					Toast toast = Toast.makeText(getApplicationContext(),
							getString(R.string.list_tips), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					Runnable waitRunnable  =new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							try {
								Thread.sleep(4000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							msg.what = 0;
							handler.sendMessage(msg);

						}
					};
					new Thread(waitRunnable).start();
				}else {
					Runnable addList = new Runnable() {
						@Override
						public void run() {
							Message msg = new Message();
							StringBuffer parBuffer  = new StringBuffer();
							parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
							.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
							.append("productid=").append(map.get("productId")).append("&")
							.append("userid=").append(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_userid))).append("&")
							.append("marketoid=").append(map.get("marketoid"));

							if (getInList().equals("false")) {
								//添加到采购清单



								JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_ADDLIST)+"?"+parBuffer.toString());
								try {
									String datasString = retJsonObject.getString("data");
									if (datasString.length() == 0) {
										msg.obj=retJsonObject.getString("info");
									}else{
										msg.obj = datasString;

									}

									//												Log.d(getString(R.string.log_tag), "RequestData："+parBuffer.toString());
									//												Log.d(getString(R.string.log_tag), "addListData："+datasString);





								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
								msg.arg2 =1;



							}else
							{
								//从采购清单删除

								JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_DELLIST)+"?"+parBuffer.toString());
								try {
									String datasString = retJsonObject.getString("data");
									if (datasString.length() == 0) {
										msg.obj=retJsonObject.getString("info");
									}else{
										msg.obj = datasString;
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
								msg.arg2 = 0;



							}

							listHandler.sendMessage(msg);

						}
					};
					new Thread(addList).start();
				}

			}
		});

		final Handler pricesHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.arg1) {
				case SUCCESS:
					pricesAdapter = new PricesAdapter(ProductDetail.this, getPriceDataList()); //创建适配器  
					pricesListView.setAdapter(pricesAdapter);

					break;
				case ERROR:

					break;

				default:
					break;
				}
			}
		};


		Runnable pricesRunnable = new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
				.append("productId=").append(map.get("productId")).append("&")
				.append("date=").append(Long.toString(System.currentTimeMillis())).append("&")
				.append("userid=").append(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_userid)));
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_PRICEAREA)+"?"+parBuffer.toString());

				try {
					String datasString = retJsonObject.getString("data");
					if (datasString.length() == 0) {
						msg.what = ERROR;
						msg.obj = retJsonObject.getString("info");
					}else {

						JSONArray jsonArray = retJsonObject.getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("city", item.getString("city"));
							map.put("releasePrice", item.getString("releasePrice"));
							map.put("releaseDate", item.getString("releaseDate"));
							map.put("market", item.getString("market"));

							getPriceDataList().add(map);
						}
						msg.what = SUCCESS;


					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = ERROR;

				}
				pricesHandler.sendMessage(msg);
			}
		};
		new Thread(pricesRunnable).start();

		final Handler historyHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.arg1) {
				case SUCCESS:
					priceHistoryAdapter = new PricesHistoryAdapter(ProductDetail.this, getPriceHistoryList()); //创建适配器  
					priceHistoryView.setAdapter(priceHistoryAdapter);

					break;
				case ERROR:

					break;

				default:
					break;
				}
			}
		};

		Runnable historyRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
				.append("productid=").append(map.get("productId")).append("&")
				.append("cityid=").append(getIntent().getSerializableExtra("CityID")).append("&")
				.append("userid=").append(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_userid)));
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_PRICEHISTORY)+"?"+parBuffer.toString());

				try {
					String datasString = retJsonObject.getString("data");
					//					Log.d(getString(R.string.log_tag), "priceHistoryParameters："+parBuffer.toString());
					//
					//					Log.d(getString(R.string.log_tag), "pirceHistoryData："+datasString);
					if (datasString.length() == 0) {
						msg.what = ERROR;
						msg.obj = retJsonObject.getString("info");
					}else {

						JSONArray jsonArray = retJsonObject.getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("rprice", item.getString("rprice"));
							map.put("gatherDateStr", item.getString("gatherDateStr"));
							map.put("marketShort", item.getString("marketShort"));

							getPriceHistoryList().add(map);
						}
						msg.what = SUCCESS;


					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = ERROR;

				}
				historyHandler.sendMessage(msg);

			}
		};
		new Thread(historyRunnable).start();
		
		final Handler providerHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.arg1) {
				case SUCCESS:
					providerAdapter = new ProviderAdapter(ProductDetail.this, getProviderDataList()); //创建适配器  
					providerView.setAdapter(providerAdapter);

					break;
				case ERROR:

					break;

				default:
					break;
				}
			}
		};
		
		Runnable dataRunnable = new Runnable() {

			@Override
			public void run() {

				Message msg = new Message();
				StringBuffer parBuffer  = new StringBuffer();
				parBuffer.append("server_str=").append(getString(R.string.SERVER_STR)).append("&")
				.append("client_str=").append(getString(R.string.CLIENT_STR)).append("&")
				.append("categoryoId=").append(map.get("categoryoid"));
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_2PROVIDER)+"?"+parBuffer.toString());
				try {
					String datasString = retJsonObject.getString("data");
					Log.d(getString(R.string.log_tag), "Request Data："+parBuffer.toString());
					Log.d(getString(R.string.log_tag), "ProviderData："+datasString);

					if (datasString.length() == 0) {
						msg.what = ERROR;
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
							getProviderDataList().add(map);
						}

						msg.what = SUCCESS;

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = ERROR;

				}


				providerHandler.sendMessage(msg);
			}
		};
		new Thread(dataRunnable).start();

	}
	public String getInList() {
		return inList;
	}
	public void setInList(String inList) {
		this.inList = inList;
		if (this.inList.equals("false")) {

			opView.setBackgroundResource(R.drawable.main_price_start_bg);

			opView.setTextColor(android.graphics.Color.WHITE);
			opView.setText("加入清单");
		}else
		{
			opView.setBackgroundResource(R.drawable.del_list_bg);
			opView.setTextColor(R.drawable.color_white);
			opView.setText("移除清单");
		}
	}

	public List<Map<String,String>> getPriceDataList() {
		return priceDataList;
	}
	public void setPriceDataList(List<Map<String,String>> priceDataList) {
		this.priceDataList = priceDataList;
	}
	public List<Map<String,String>> getPriceHistoryList() {
		return priceHistoryList;
	}
	public void setPriceHistoryList(List<Map<String,String>> priceHistoryList) {
		this.priceHistoryList = priceHistoryList;
	}
	public List<Map<String,String>> getProviderDataList() {
		return providerDataList;
	}
	public void setProviderDataList(List<Map<String,String>> providerDataList) {
		this.providerDataList = providerDataList;
	}



}
