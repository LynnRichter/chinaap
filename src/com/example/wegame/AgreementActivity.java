package com.example.wegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class AgreementActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_agreement, null));
		UIFactory();
	}
	protected void UIFactory() {
		WebView webView=(WebView)findViewById(R.id.agree_webview);
		webView.loadUrl("file:///android_asset/chinaap.html");
		TextView backView = (TextView)this.findViewById(R.id.agree_back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
	}
}
