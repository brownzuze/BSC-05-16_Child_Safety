package com.example.childsafetyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListOrganisationActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {
	
	public static final String TAG = "ListOrganisationActivity";
	
	public static final int REQUEST_CODE_ADD_ORGANISATION = 40;
	public static final String EXTRA_KEY_ADDED_ORGANISATION = "extra_key_added_organisation";
	
	private ListView mListviewOrganisation;
	private TextView mTxtEmptyListOrganisation;
	private ImageButton mBtnAddOrganisation;
	
	private ListOrganisationAdapter mAdapter;
	private List<Organisation> mListOrganisation;
	private OrganisationDAO mOrganisationDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_organisation);
		
		// initialize views
		initViews();
		
		// fill the listView
		mOrganisationDao = new OrganisationDAO(this);
		mListOrganisation = mOrganisationDao.getAllOrganisations();
		if(mListOrganisation != null && !mListOrganisation.isEmpty()) {
			mAdapter = new ListOrganisationAdapter(this, mListOrganisation);
			mListviewOrganisation.setAdapter(mAdapter);
		}
		else {
			mTxtEmptyListOrganisation.setVisibility(View.VISIBLE);
			mListviewOrganisation.setVisibility(View.GONE);
		}
	}

	private void initViews() {
		this.mListviewOrganisation = (ListView) findViewById(R.id.list_companies);
		this.mTxtEmptyListOrganisation = (TextView) findViewById(R.id.txt_empty_list_companies);
		this.mBtnAddOrganisation = (ImageButton) findViewById(R.id.btn_add_company);
		this.mListviewOrganisation.setOnItemClickListener(this);
		this.mListviewOrganisation.setOnItemLongClickListener(this);
		this.mBtnAddOrganisation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_company:
			Intent intent = new Intent(this, AddOrganisationActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_ORGANISATION);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_ADD_ORGANISATION) {
			if(resultCode == RESULT_OK) {
				// add the added Organisation to the listOrganisation and refresh the listView
				if(data != null) {
					Organisation createdOrganisation = (Organisation) data.getSerializableExtra(EXTRA_KEY_ADDED_ORGANISATION);
					if(createdOrganisation != null) {
						if(mListOrganisation == null)
							mListOrganisation = new ArrayList<Organisation>();
						mListOrganisation.add(createdOrganisation);
						
						if(mAdapter == null) {
							if(mListviewOrganisation.getVisibility() != View.VISIBLE) {
								mListviewOrganisation.setVisibility(View.VISIBLE);
								mTxtEmptyListOrganisation.setVisibility(View.GONE);
							}
								
							mAdapter = new ListOrganisationAdapter(this, mListOrganisation);
							mListviewOrganisation.setAdapter(mAdapter);
						}
						else {
							mAdapter.setItems(mListOrganisation);
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		}
		else 
			super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mOrganisationDao.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Organisation clickedOrganisation = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : "+ clickedOrganisation.getName());
		Intent intent = new Intent(this, ListChildActivity.class);
		intent.putExtra(ListChildActivity.EXTRA_SELECTED_ORGANISATION_ID, clickedOrganisation.getId());
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Organisation clickedOrganisation = mAdapter.getItem(position);
		Log.d(TAG, "longClickedItem : "+ clickedOrganisation.getName());
		showDeleteDialogConfirmation(clickedOrganisation);
		return true;
	}

	private void showDeleteDialogConfirmation(final Organisation clickedOrganisation) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete the \""+ clickedOrganisation.getName()+"\" organisation ?");
 
        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete the company and refresh the list
				if(mOrganisationDao != null) {
					mOrganisationDao.deleteOrganisation(clickedOrganisation);
					mListOrganisation.remove(clickedOrganisation);
					
					//refresh the listView
					if(mListOrganisation.isEmpty()) {
						mListviewOrganisation.setVisibility(View.GONE);
						mTxtEmptyListOrganisation.setVisibility(View.VISIBLE);
					}
					mAdapter.setItems(mListOrganisation);
					mAdapter.notifyDataSetChanged();
				}
				
				dialog.dismiss();
				Toast.makeText(ListOrganisationActivity.this, R.string.organisation_deleted_successfully, Toast.LENGTH_SHORT).show();
			}
		});
        
        // set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Dismiss the dialog
                dialog.dismiss();
			}
		});
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
	}
}
