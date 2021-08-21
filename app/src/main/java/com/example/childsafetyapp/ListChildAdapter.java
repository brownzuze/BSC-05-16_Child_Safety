package com.example.childsafetyapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListChildAdapter extends BaseAdapter {
	
	public static final String TAG = "ListChildrenAdapter";
	
	private List<Child> mItems;
	private LayoutInflater mInflater;
	
	public ListChildAdapter(Context context, List<Child> listOrganisations) {
		this.setItems(listOrganisations);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public Child getItem(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
	}

	@Override
	public long getItemId(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if(v == null) {
			v = mInflater.inflate(R.layout.list_item_child, parent, false);
			holder = new ViewHolder();
			holder.txtChildName = (TextView) v.findViewById(R.id.txt_employee_name);
			holder.txtOrganisationName = (TextView) v.findViewById(R.id.txt_organisation_name);
			holder.txtAddress = (TextView) v.findViewById(R.id.txt_address);
			holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
			holder.txtEmail = (TextView) v.findViewById(R.id.txt_email);
			holder.txtDate = (TextView) v.findViewById(R.id.txt_datepiker);
			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// fill row data
		Child currentItem = getItem(position);
		if(currentItem != null) {
			holder.txtChildName.setText(currentItem.getFirstName()+" "+currentItem.getLastName());
			holder.txtAddress.setText(currentItem.getAddress());
			holder.txtEmail.setText(currentItem.getEmail());
			holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
			holder.txtDate.setText(currentItem.getDate());
			holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
			holder.txtOrganisationName.setText(currentItem.getOrganisation().getName());
		}
		
		return v;
	}
	
	public List<Child> getItems() {
		return mItems;
	}

	public void setItems(List<Child> mItems) {
		this.mItems = mItems;
	}

	class ViewHolder {
		TextView txtChildName;
		TextView txtEmail;
		TextView txtPhoneNumber;
		TextView txtAddress;
		TextView txtOrganisationName;
		TextView txtDate;
	}

}
