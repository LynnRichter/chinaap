package com.example.wegame;



import android.R.integer;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabMainActivity extends TabActivity {
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
        
     
//		�ж��Ƿ��¼���ʺ�
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
    

    }
}