package com.example.wegame;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class ListButton extends Button {

	public ListButton(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}

	public ListButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//	    switch (event.getAction()) {
		//	    case MotionEvent.ACTION_DOWN:
		//	      performClick();
		//	      break;
		//
		//	    default:
		//	      break;
		//	    }
		Log.i("pdwy","ListButton dispatchTouchEvent");

		return false;
	}
}
