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

public class ProviderAdapter extends BaseAdapter {

	private List<Map<String, String>> listItems;    //商品信息集合   
	private LayoutInflater listContainer;           //视图容器   
	private LoadRemoteImage remoteImage;
	public final class ListItemView{                //自定义控件集合     
		public ImageView image;     
		public TextView name;
		public TextView mainProduct;
		public TextView contact;
		public TextView phone;
		public TextView address;

	}     


	public ProviderAdapter(Context context, List<Map<String, String>> listItems) {   
		listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
		this.listItems = listItems;   
		remoteImage = new LoadRemoteImage();  
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
			convertView= listContainer.inflate(R.layout.item_provider, null);
			listItemView.image = (ImageView)convertView.findViewById(R.id.provider_item_image);
			listItemView.name = (TextView)convertView.findViewById(R.id.provider_item_name);
			listItemView.mainProduct = (TextView)convertView.findViewById(R.id.provider_item_main);
			listItemView.contact = (TextView)convertView.findViewById(R.id.provider_item_contact);
			listItemView.phone = (TextView)convertView.findViewById(R.id.provider_item_phone);
			listItemView.address = (TextView)convertView.findViewById(R.id.provider_item_address);
			convertView.setTag(listItemView);
		}else {   
			listItemView = (ListItemView)convertView.getTag();   
		}

		listItemView.name.setText(listItems.get(index).get("providerName"));
		listItemView.mainProduct.setText(listItems.get(index).get("mainProduct"));
		listItemView.contact.setText(listItems.get(index).get("contact"));
		listItemView.phone.setText(listItems.get(index).get("contactNumber"));
		listItemView.address.setText(listItems.get(index).get("companyAddress"));
		final ImageView icon = (ImageView)convertView.findViewById(R.id.provider_item_image); 
		//得到可用的图片  
//		Bitmap bitmap =JSONHelpler. getHttpBitmap("http://www.chinaap.com/data/provider"+listItems.get(index).get("imageurl"));
		remoteImage.setRemoteImageListener("http://www.chinaap.com/data/provider"+listItems.get(index).get("imageurl"), new LoadRemoteImage.OnRemoteImageListener() {  
            
            @Override  
            public void onError(String error) {  
            	icon.setImageResource(R.drawable.demo) ;
            }  

            @Override  
            public void onRemoteImage(Bitmap image) {  
                icon.setImageBitmap(image);  

            }  
        });  
		
		 



		return convertView;
	}

}
