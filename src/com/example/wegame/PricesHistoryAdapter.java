package com.example.wegame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PricesHistoryAdapter extends BaseAdapter {

	private List<Map<String, String>> listItems;    //商品信息集合   
	private LayoutInflater listContainer;           //视图容器   
	public final class ListItemView{                //自定义控件集合       
		public TextView city;
		public TextView releasePrice;
		public TextView releaseDate;
		public TextView market;
	
	}     


	public PricesHistoryAdapter(Context context, List<Map<String, String>> listItems) {   
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
		this.listItems = listItems;   
		//		hasChecked = new boolean[getCount()];   
	}   
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
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
	public View getView(int index, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();    
			//获取list_item布局文件的视图   
			convertView= listContainer.inflate(R.layout.item_price_history, null);
			listItemView.releaseDate = (TextView)convertView.findViewById(R.id.price_history_releaseDate);
			listItemView.releasePrice = (TextView)convertView.findViewById(R.id.price_history_rprice);
			listItemView.market = (TextView)convertView.findViewById(R.id.price_history_market);
			if (index%2 != 0) {
				//						scrollView1.setBackgroundResource(R.drawable.color_grayback);
				//						holder.name_text.setBackgroundResource(R.drawable.color_grayback);
				convertView.setBackgroundResource(R.drawable.color_grayback);
			}
			convertView.setTag(listItemView);
		}else {   
			listItemView = (ListItemView)convertView.getTag();   
		}

	
		listItemView.releaseDate.setText(listItems.get(index).get("gatherDateStr"));
		listItemView.releasePrice.setText(listItems.get(index).get("rprice"));
		listItemView.market.setText(listItems.get(index).get("marketShort"));
		
		return convertView;
	}

}
