package com.example.wegame;



import android.R.integer;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabMainActivity<T> extends TabActivity {
	private TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_host);

		tabHost=this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;


		intent=new Intent().setClass(this, PriceActivity.class);
		spec=tabHost.newTabSpec(getString(R.string.main_price)).setIndicator(getString(R.string.main_price)).setContent(intent);
		tabHost.addTab(spec);

		intent=new Intent().setClass(this,ProviderActivity.class);
		spec=tabHost.newTabSpec(getString(R.string.main_provider)).setIndicator(getString(R.string.main_provider)).setContent(intent);
		tabHost.addTab(spec);

		intent=new Intent().setClass(this, ListActivity.class);
		spec=tabHost.newTabSpec(getString(R.string.main_list)).setIndicator(getString(R.string.main_list)).setContent(intent);
		tabHost.addTab(spec);


		//		ÅÐ¶ÏÊÇ·ñµÇÂ¼¹ýÕÊºÅ
		if (JSONHelpler.getLogin(getApplicationContext())) {
			intent=new Intent().setClass(this, VIPActivity.class);

		}
		else {
			intent=new Intent().setClass(this, LoginActivity.class);

		}
		spec=tabHost.newTabSpec(getString(R.string.me)).setIndicator(getString(R.string.me)).setContent(intent);
		tabHost.addTab(spec);

		RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
		tabHost.setCurrentTab(Integer.parseInt((String) getIntent().getSerializableExtra("id")));

		RadioButton providerButton = (RadioButton)findViewById(R.id.main_tab_provider);
		providerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Activity currentActivity = getCurrentActivity();
				if (currentActivity instanceof ProviderActivity) {
				           ((ProviderActivity) currentActivity).defaultLoad();;
				       }
				
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_tab_price:
					tabHost.setCurrentTabByTag(getString(R.string.main_price));
					break;
				case R.id.main_tab_provider:
					tabHost.setCurrentTabByTag(getString(R.string.main_provider));
					
					break;
				case R.id.main_tab_list:
					tabHost.setCurrentTabByTag(getString(R.string.main_list));
					break;
				case R.id.main_tab_vip:
					tabHost.setCurrentTabByTag(getString(R.string.me));
					break;
				default:
					break;
				}
			}
			
		});
		RadioButton checkButton = null;
		Log.d(getString(R.string.log_tag), "µã»÷´«ÖÁ£º"+(String) getIntent().getSerializableExtra("id"));
		switch (Integer.parseInt((String) getIntent().getSerializableExtra("id"))) {
		case 0:
			checkButton =(RadioButton)findViewById(R.id.main_tab_price);
			break;
		case 1:
			checkButton =(RadioButton)findViewById(R.id.main_tab_provider);
			
			break;
		case 2:
			checkButton =(RadioButton)findViewById(R.id.main_tab_list);

			break;
		case 3:
			checkButton =(RadioButton)findViewById(R.id.main_tab_vip);

			break;

		default:
			break;
		}
		checkButton.setChecked(true);
		
		

	}
	
}
