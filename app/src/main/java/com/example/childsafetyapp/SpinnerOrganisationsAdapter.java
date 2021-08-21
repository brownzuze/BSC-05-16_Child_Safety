package com.example.childsafetyapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerOrganisationsAdapter extends BaseAdapter {
	
	public static final String TAG = "SpinnerOrganisationsAdapter";
	
	private List<Organisation> mItems;
	private LayoutInflater mInflater;
	
	public SpinnerOrganisationsAdapter(Context context, List<Organisation> listCompanies) {
		this.setItems(listCompanies);
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
			v = mInflater.inflate(R.layout.spinner_item_organisation, parent, false);
			holder = new ViewHolder();
			holder.txtOrganisationName = (TextView) v.findViewById(R.id.txt_organisation_name);
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
			holder.txtWebsite.setText(currentItem.getWebsite());
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
	}
}
