package com.example.mycontactmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ListContact extends Activity
{
	private EditText _searchBar;
	private ListView _contactList;
	public List<RowItem> _contactItems; 
	private CustomListViewAdapter _adapter;
	private SharedPreferences _prefs;
	private SharedPreferences.Editor _edit;
	private int _currentPosition;
	private Map<String, ?> _contacts;
	private OnItemClickListener _contactListOnItemClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
            _currentPosition = position;
            startDetailsActivity(position, 1);
		}
	};

	private TextWatcher watcher = new TextWatcher() {
		
		@SuppressLint("DefaultLocale")
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			StringBuilder imagePath = new StringBuilder(""), name = new StringBuilder(""), phone = new StringBuilder("");
			_contactItems.clear();
		   RowItem element;
		   String tmp1, tmp2 = s.toString();
		   tmp2 = tmp2.toLowerCase(Locale.getDefault());
			 for (String key : _contacts.keySet())
		        {
			    	getContactInfo(key, imagePath, name, phone);
			    	tmp1 = name.substring(0, s.length()).toLowerCase(Locale.getDefault());
			    	if ( tmp1.compareTo(tmp2) == 0)
			    	{
			    		element = new RowItem(key, imagePath.toString(), name.toString(), phone.toString());
			    		_contactItems.add(element);
			    	}
		        }
			   _adapter.notifyDataSetChanged();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	};
	
	@Override
  	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list_contact);
		
		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		
	    this._searchBar = new EditText(this);
		this._contactList = (ListView)findViewById(R.id.contact_list);
		this._contactItems= new ArrayList<RowItem>();
	    this._prefs = this.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
	    this._edit = this._prefs.edit();
	    this._contacts = this._prefs.getAll();
	    RelativeLayout rl = (RelativeLayout)findViewById(R.id.list_view);
	    Resources r = getResources();
	    int pixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
	    																															pixels);

	    Typeface typeFace = Typeface.createFromAsset(this.getAssets(),"fonts/ArchitectsDaughter.ttf");
	    pixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
	    params.topMargin = height - pixels;
	    this._searchBar.setCursorVisible(false);
	    this._searchBar.setTypeface(typeFace);
	    this._searchBar.setHint(R.string.search);
	    this._searchBar.setHintTextColor(0xFFFFFFFF);
	    this._searchBar.setTextColor(0xFFFFFFFF);
	    this._searchBar.setBackgroundColor(0xFF000000);
	    rl.addView(_searchBar, params);

	    RowItem element;
	    StringBuilder imagePath = new StringBuilder(""), name = new StringBuilder(""), phone = new StringBuilder("");

	    for (String s : this._contacts.keySet())
        {
	    	this.getContactInfo(s, imagePath, name, phone);
        	element = new RowItem(s, imagePath.toString(), name.toString(), phone.toString());
        	this._contactItems.add(element);
        }
        this._edit.commit();
		this._adapter = new CustomListViewAdapter(this, R.layout.list_item, this._contactItems);

		this._searchBar.addTextChangedListener(watcher);
		this._contactList.setAdapter(this._adapter);
		this._contactList.setOnItemClickListener(this._contactListOnItemClick);
		registerForContextMenu(this._contactList);
	}
	
	private void getContactInfo(String infos, StringBuilder imagePath, StringBuilder name, StringBuilder phoneNumber)
	{
		String[] contact;
		
		contact = this._contacts.get(infos).toString().split("\n");
    	if (contact.length == 1)
    		imagePath.replace(0, imagePath.length(), contact[0]);
    	else if (contact.length == 2)
    	{
    		imagePath.replace(0, imagePath.length(), contact[0]);
    		name.replace(0, name.length(), contact[1]);
    	}
    	else if (contact.length == 3)
    	{
    		imagePath.replace(0, imagePath.length(), contact[0]);
    		name.replace(0, name.length(), contact[1]);
    		phoneNumber.replace(0, phoneNumber.length(), contact[2]);
    	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_contact, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
	      switch (item.getItemId())
	      {
	         case R.id.new_contact:
	            Intent intent = new Intent(ListContact.this, DetailsActivity.class);
	            intent.putExtra("id", "");
	            intent.putExtra("image_path", "");
	            intent.putExtra("name", "");
	            intent.putExtra("phone_number", "");
	           	startActivityForResult(intent, 2);
	            return true;
	         case R.id.settings:
	             return true;
	      }
	      return false;
	}

	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, view, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo cmi = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId())
		{
			case R.id.call:
				Uri tel = Uri.parse("tel:" + this._contactItems.get(cmi.position).get_phone_number());
				Intent composer = new Intent(Intent.ACTION_DIAL, tel);
				startActivity(composer);
				return (true);
			case R.id.edit:
				startDetailsActivity(cmi.position, 1);
				return (true);
			case R.id.remove:
				displayAlertDialog(cmi.position);
				return (true);
		}
		return (false);
	}

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent)
   {
	   RowItem item;
	   if (requestCode == 1 && resultCode == RESULT_OK)
	   {
		   item = this._contactItems.get(this._currentPosition);
		   String res = intent.getExtras().getString("image_path") + "\n" + intent.getExtras().getString("name") + "\n" + intent.getExtras().getString("phone_number");
		   _edit.putString(intent.getExtras().getString("id"), res).commit();
		   
		   item.setImagePath(intent.getExtras().getString("image_path"));
		   item.set_name(intent.getExtras().getString("name"));
		   item.set_phone_number(intent.getExtras().getString("phone_number"));
		   this._adapter.notifyDataSetChanged();
	   }
	   else if (requestCode == 2 && resultCode == RESULT_OK)
	   {
		   String id =  UUID.randomUUID().toString();
		   String data = intent.getExtras().getString("image_path") + "\n" + intent.getExtras().getString("name") + "\n" + intent.getExtras().getString("phone_number");
		   _edit.putString(id, data).commit();
		   item = new RowItem(id, intent.getExtras().getString("image_path"),
				   								intent.getExtras().getString("name"),
				   								intent.getExtras().getString("phone_number"));
		   this._contactItems.add(item);
	   }
	   this._contacts = this._prefs.getAll();
	   this._adapter.notifyDataSetChanged();
   }

   private void displayAlertDialog(final int position)
   {
	   AlertDialog.Builder adb = new AlertDialog.Builder(this);

	   adb.setCancelable(false);
	   adb.setTitle(R.string.app_name);
	   adb.setMessage(R.string.adb);
	   adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		   @Override
			public void onClick(DialogInterface dialog, int which) {

			   RowItem elem = _contactItems.get(position);
			   for (String s : _contacts.keySet())
		        {
			    	if (s.compareTo(elem.get_id()) == 0)
			    	{
			    		_edit.remove(s).commit();
			    		break;
			    	}
		        }
			   _contactItems.remove(position);
			   _adapter.notifyDataSetChanged();
		   }
	   });
	   adb.show();
   }

   private void startDetailsActivity(int position, int returnCode)
   {
	   Intent intent = new Intent(ListContact.this, DetailsActivity.class);
       RowItem item= _contactItems.get(position);
       intent.putExtra("id", item.get_id());
       intent.putExtra("image_path", item.getImagePath());
       intent.putExtra("name", item.get_name());
       intent.putExtra("phone_number", item.get_phone_number());
      	startActivityForResult(intent, returnCode);
   }
}
