package com.example.wegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class PriceSearchResultAcitivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceStateBundle) {
		super.onCreate(savedInstanceStateBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayoutInflater().inflate(R.layout.activity_main, null));

	}
}
