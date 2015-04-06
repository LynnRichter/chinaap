package com.example.wegame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wegame.LHScrollView.OnScrollChangedListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PriceSearchResultActivity extends Activity{
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
	private static final int DATA_EMPTY = 7;
	private static final int EXCEPTION	= 6;

	private Handler dataHandler = null;
	private String tyepID,cityID,typeName,cityName,dateString,timestamp,sortIndex;
	private PriceAdapter listViewAdapter; 
	private ListView listView;
	private List<Map<String,String>> listItems;
	private RelativeLayout headView;
	private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_price_search, null));
		UIFactory();
	}
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			//当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) headView
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}
	private void UIFactory()
	{

		citySpinner = (Spinner)findViewById(R.id.price_search_spinner_city);
		typeSpinner = (Spinner)findViewById(R.id.price_search_spinner_category);
		headView =(RelativeLayout)findViewById(R.id.price_search_head);
		headView.setFocusable(true);
		headView.setClickable(true);
		headView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		listView =(ListView)findViewById(R.id.price_search_content_list);
		listView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		editText =(EditText)findViewById(R.id.price_search_input);
		Intent intent =getIntent();
		editText.setText(intent.getStringExtra("InputKey"));
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				startLoad();
				return false;
			}
		});


		final TextView dateView =(TextView)findViewById(R.id.price_search_btn_date);
		SimpleDateFormat formatter = new SimpleDateFormat   ("yyyy-MM-dd");     
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String str = formatter.format(curDate);
		dateView.setText(str);
		setTimestamp(Long.toString(System.currentTimeMillis()));
		//		Log.d(getString(R.string.log_tag), "Timestamp："+getTimestamp());
		dateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Calendar calendar = Calendar.getInstance();
				// TODO Auto-generated method stub
				DatePickerDialog dialog = new DatePickerDialog(PriceSearchResultActivity.this, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int month, int day) {
						try {
							Log.d(getString(R.string.log_tag), ""+year+"-"+month+"-"+day);
							Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(""+year+"-"+(month+1)+"-"+(day));
							setTimestamp(Long.toString(tDate.getTime()));
							Log.d(getString(R.string.log_tag), "Timestamp："+Long.toString(tDate.getTime()));
							dateView.setText(""+year+"-"+(month+1)+"-"+(day));


						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				},calendar.get(Calendar.YEAR), calendar
				.get(Calendar.MONTH), calendar
				.get(Calendar.DAY_OF_MONTH));
				dialog.show();

			}
		});

		ImageView backView = (ImageView)this.findViewById(R.id.price_search_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
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
					cityAdapter = new ArrayAdapter<String>(PriceSearchResultActivity.this, android.R.layout.simple_spinner_item, citylist);
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
					typeAdapter = new ArrayAdapter<String>(PriceSearchResultActivity.this, android.R.layout.simple_spinner_item, typelist);
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
		final Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{

				Intent intent  = new Intent();
				intent.setClass(PriceSearchResultActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		};
		Button searchButton = (Button)findViewById(R.id.price_search_button_start);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
					Date toData = new SimpleDateFormat("yyyy-MM-dd").parse(dateView.getText().toString());
					Log.d(getString(R.string.log_tag), "相隔天数："+ JSONHelpler.getGapCount(curDate,toData));

					if (JSONHelpler.getGapCount(curDate,toData) >= -90 && !JSONHelpler.getLogin(getApplicationContext())) {
						Toast toast = Toast.makeText(getApplicationContext(),
								getString(R.string.price_search_tips), Toast.LENGTH_LONG);
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
						//						Thread.sleep(3000);
						//						Intent intent = new Intent();
						//						intent.setClass(PriceActivity.this, LoginActivity.class);
						//						startActivity(intent);

					}
					else{
						startLoad();
					}
				} 
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				//				catch (InterruptedException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}


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

					listViewAdapter = new PriceAdapter(PriceSearchResultActivity.this,R.layout.item_price); //创建适配器   
					listView.setAdapter(listViewAdapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							//							HashMap<String,String> map=(HashMap<String,String>)getListItems().get(arg2);   
							//							Intent intent = new Intent();
							//							intent.setClass(PriceActivity.this, ProviderDetailActivity.class);
							//							intent.putExtra("Item", map);
							//							startActivity(intent);
							Log.d(getString(R.string.log_tag), "你选择了第："+arg2+"行");




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
				.append("productCategoryid=").append(getTyepID()).append("&")
				.append("productname=").append(editText.getText().toString()).append("&")
				.append("userid=").append(JSONHelpler.getString(getApplicationContext(), getString(R.string.key_userid)));
				JSONObject retJsonObject = JSONHelpler.getJason(getString(R.string.URL_FINDPRICE)+"?"+parBuffer.toString());
				try {
					String datasString = retJsonObject.getString("data");
					Log.d(getString(R.string.log_tag), "Request Data："+parBuffer.toString());
					Log.d(getString(R.string.log_tag), "PriceData："+datasString);

					if (datasString.length() == 0) {
						msg.what = DATA_ERROR;
						msg.obj = retJsonObject.getString("info");
					}else {

						JSONArray jsonArray = retJsonObject.getJSONArray("data");
						for (int i=0;i<jsonArray.length();i++) {
							JSONObject item = jsonArray.getJSONObject(i);
							Map<String, String> map = new HashMap<String, String>();
							map.put("productName", item.getString("productName"));
							map.put("spec", item.getString("spec"));
							map.put("rprice", item.getString("rprice"));
							map.put("unit", item.getString("unit"));
							map.put("priceIndex", item.getString("priceIndex"));
							map.put("marketShort", item.getString("marketShort"));
							map.put("releaseDate", item.getString("releaseDate"));
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
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp.substring(0, 10);
	}


	public class PriceAdapter extends BaseAdapter {
		public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();

		int id_row_layout;
		LayoutInflater mInflater;

		public PriceAdapter(Context context, int id_row_layout) {
			super();
			this.id_row_layout = id_row_layout;
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return getListItems().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			ViewHolder holder = null;
			final int index =position;
			if (convertView == null) {
				synchronized (PriceSearchResultActivity.this) {
					convertView = mInflater.inflate(id_row_layout, null);
					holder = new ViewHolder();

					LHScrollView scrollView1 = (LHScrollView) convertView
							.findViewById(R.id.horizontalScrollView1);

					holder.scrollView = scrollView1;


					LHScrollView headSrcrollView = (LHScrollView) headView
							.findViewById(R.id.horizontalScrollView1);
					headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
							scrollView1));
					RelativeLayout parent =(RelativeLayout)convertView.findViewById(R.id.price_item_parent);
					holder.name_text =(TextView) convertView.findViewById(R.id.item_price_name);
					holder.spec_text =(TextView) convertView.findViewById(R.id.item_price_spec);
					holder.rprice_text =(TextView) convertView.findViewById(R.id.item_price_rprice);
					holder.unit_text =(TextView) convertView.findViewById(R.id.item_price_unit);
					holder.priceIndex_text =(TextView) convertView.findViewById(R.id.item_price_priceIndex);
					holder.marketShort_text =(TextView) convertView.findViewById(R.id.item_price_marketShort);
					holder.releaseDate_text =(TextView) convertView.findViewById(R.id.item_price_releaseDate);
					holder.list_text =(TextView) convertView.findViewById(R.id.item_price_list);

					holder.list_text.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							Log.d(getString(R.string.log_tag), "你点击了第："+index+"行的按钮");

						}
					});
					convertView.setTag(holder);
					mHolderList.add(holder);
					if (position%2 != 0) {
						//						scrollView1.setBackgroundResource(R.drawable.color_grayback);
						//						holder.name_text.setBackgroundResource(R.drawable.color_grayback);
						parent.setBackgroundResource(R.drawable.color_grayback);
					}
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name_text.setText(getListItems().get(position).get("productName"));
			holder.spec_text.setText(getListItems().get(position).get("spec"));
			holder.rprice_text.setText(" ￥"+getListItems().get(position).get("rprice"));
			holder.unit_text.setText(getListItems().get(position).get("unit"));
			holder.priceIndex_text.setText(getListItems().get(position).get("priceIndex").endsWith("null") ? "0" :getListItems().get(position).get("priceIndex"));
			holder.marketShort_text.setText(getListItems().get(position).get("marketShort"));
			holder.releaseDate_text.setText(getListItems().get(position).get("releaseDate"));

			return convertView;
		}

		class OnScrollChangedListenerImp implements OnScrollChangedListener {

			LHScrollView mScrollViewArg;

			public OnScrollChangedListenerImp(LHScrollView scrollViewar) {
				mScrollViewArg = scrollViewar;
			}

			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				mScrollViewArg.smoothScrollTo(l, t);
			}
		};

		class ViewHolder {
			TextView name_text;
			TextView spec_text;
			TextView rprice_text;
			TextView unit_text;
			TextView priceIndex_text;
			TextView marketShort_text;
			TextView releaseDate_text;
			TextView list_text;

			HorizontalScrollView scrollView;
		}
	}// end class 
	//	@Override
	//	public boolean dispatchTouchEvent(MotionEvent event) {
	//
	//	return super.dispatchTouchEvent(event);
	//
	//
	//	}
}
