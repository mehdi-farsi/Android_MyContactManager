package com.example.mycontactmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class DetailsActivity extends Activity
{
	private ImageButton _call;
	private ImageButton _sms;
	private ImageButton _icon;
	private EditText _name;
	private EditText _phoneNumber;
	private String _id;
	private String _imagePath = "";
	private OnClickListener _callOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Uri tel = Uri.parse("tel:" + _phoneNumber.getText().toString());
			Intent composer = new Intent(Intent.ACTION_DIAL, tel);
			startActivity(composer);
		}
	};
	
	private OnClickListener _smsOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Uri sms = Uri.parse("sms:" + _phoneNumber.getText().toString());
			Intent composer = new Intent(Intent.ACTION_VIEW, sms);
			startActivity(composer);
		}
	};
	
	private OnClickListener _iconOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
		    galleryintent.setType("image/*");
		    startActivityForResult(galleryintent, 1);
		}
	};
	private OnClickListener _saveOnClick = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details);
		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;
		String name = extras.getString("name");
		String phoneNumber = extras.getString("phone_number");
		Typeface typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ArchitectsDaughter.ttf");
		this._imagePath = extras.getString("image_path");
		this._id = extras.getString("id");
		_call = (ImageButton)findViewById(R.id.details_call);
		_sms = (ImageButton)findViewById(R.id.details_sms);
		_icon = (ImageButton)findViewById(R.id.details_icon);
		_name = (EditText)findViewById(R.id.details_name);
		_phoneNumber = (EditText)findViewById(R.id.details_phone_number);
		Button save = (Button)findViewById(R.id.detail_save);
		
		_name.setTypeface(typeFace);
		_phoneNumber.setTypeface(typeFace);
		if (name != null && phoneNumber != null)
		{
			_icon.setImageURI(Uri.parse(this._imagePath));
			_name.setText(name);
			_phoneNumber.setText(phoneNumber);
		}
		
		this._icon.setOnClickListener(_iconOnClick);
		this._call.setOnClickListener(_callOnClick);
		this._sms.setOnClickListener(_smsOnClick);
		save.setOnClickListener(_saveOnClick);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == 1 && resultCode == RESULT_OK)
		{
			Uri uri = intent.getData();
			_icon.setImageURI(uri);
			this._imagePath = getRealPathFromURI(uri);
			Toast.makeText(getApplicationContext(), getRealPathFromURI(uri), Toast.LENGTH_SHORT).show();
		}
	}

	private String getRealPathFromURI(Uri contentUri)
	{
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	@Override
	public void finish()
	{
		Intent data = new Intent();
		data.putExtra("image_path", this._imagePath);
		data.putExtra("name", this._name.getText().toString());
		data.putExtra("phone_number", _phoneNumber.getText().toString());
		data.putExtra("id", this._id);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
