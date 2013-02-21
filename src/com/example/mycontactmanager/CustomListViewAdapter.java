package com.example.mycontactmanager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<RowItem>
{
	private LayoutInflater _inflater;
	private Context _context;
	
	public CustomListViewAdapter(Context context, int ressource, List<RowItem> data)
	{
		super(context, ressource, data);
		this._context = context;
		this._inflater = (LayoutInflater)this._context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder
	{
		ImageView icon;
		TextView name;
		TextView phoneNumber;
	}

	public View getView(int index, View convertView, ViewGroup Parent)
	{
		ViewHolder holder =null;
		RowItem item = getItem(index);

		if (convertView == null)
		{
			convertView = this._inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.phoneNumber = (TextView)convertView.findViewById(R.id.phone_number);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}

		Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/ArchitectsDaughter.ttf");
		holder.name.setTypeface(typeFace);
		holder.phoneNumber.setTypeface(typeFace);
		if (item != null)
		{
			holder.icon.setImageURI(Uri.parse(item.getImagePath()));
			holder.name.setText(item.get_name());
			holder.phoneNumber.setText(item.get_phone_number());
		}
		return (convertView);
	}
}
