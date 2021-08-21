package com.example.childsafetyapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListOrganisationAdapter extends BaseAdapter {
	
	public static final String TAG = "ListOrganisationAdapter";
	
	private List<Organisation> mItems;
	private LayoutInflater mInflater;
	
	public ListOrganisationAdapter(Context context, List<Organisation> listOrganisation) {
		this.setItems(listOrganisation);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public Organisation getItem(int position) {
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
			v = mInflater.inflate(R.layout.list_item_organisation, parent, false);
			holder = new ViewHolder();
			holder.txtOrganisationName = (TextView) v.findViewById(R.id.txt_organisation_name);
			holder.txtAddress = (TextView) v.findViewById(R.id.txt_address);
			holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
			holder.txtWebsite = (TextView) v.findViewById(R.id.txt_website);
			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// fill row data
		Organisation currentItem = getItem(position);
		if(currentItem != null) {
			holder.txtOrganisationName.setText(currentItem.getName());
			holder.txtAddress.setText(currentItem.getAddress());
			holder.txtWebsite.setText(currentItem.getWebsite());
			holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
		}
		
		return v;
	}
	
	public List<Organisation> getItems() {
		return mItems;
	}

	public void setItems(List<Organisation> mItems) {
		this.mItems = mItems;
	}

	class ViewHolder {
		TextView txtOrganisationName;
		TextView txtWebsite;
		TextView txtPhoneNumber;
		TextView txtAddress;
	}

}
