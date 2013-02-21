package com.example.mycontactmanager;

public class RowItem
{
	private String _id;
	private String _imagePath;
	private String _name;
	private String _phone_number;
	
	public RowItem(String id, String imagePath, String name, String phone_number)
	{
		this._id = id;
		this._imagePath = imagePath;
		this._name = name;
		this._phone_number = phone_number;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getImagePath() {
		return _imagePath;
	}

	public void setImagePath(String imagePath) {
		this._imagePath = imagePath;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_phone_number() {
		return _phone_number;
	}

	public void set_phone_number(String _phone_number) {
		this._phone_number = _phone_number;
	}
}
