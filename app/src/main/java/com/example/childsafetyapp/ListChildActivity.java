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

public class ListChildActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

	public static final String TAG = "ListEmployeesActivity";

	public static final int REQUEST_CODE_ADD_Children = 40;
	public static final String EXTRA_ADDED_EMPLOYEE = "extra_key_added_employee";
	public static final String EXTRA_SELECTED_ORGANISATION_ID = "extra_key_selected_company_id";

	private ListView mListviewChildren;
	private TextView mTxtEmptyListChildren;
private ImageButton mBtnAddChildren;

	private ListChildAdapter mAdapter;
	private List<Child> mListChildren;
	private ChildDAO mChildDao;

	private long mOrganisationId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_child);

		// initialize views
		initViews();

		// get the organisation id from extras
		mChildDao = new ChildDAO(this);
		Intent intent  = getIntent();
		if(intent != null) {
			this.mOrganisationId = intent.getLongExtra(EXTRA_SELECTED_ORGANISATION_ID, -1);
		}

		if(mOrganisationId != -1) {
			mListChildren = mChildDao.getChildOfOrganisation(mOrganisationId);
			// fill the listView
			if(mListChildren != null && !mListChildren.isEmpty()) {
				mAdapter = new ListChildAdapter(this, mListChildren);
				mListviewChildren.setAdapter(mAdapter);
			}
			else {
				mTxtEmptyListChildren.setVisibility(View.VISIBLE);
				mListviewChildren.setVisibility(View.GONE);
			}
		}
	}

	private void initViews() {
		this.mListviewChildren = (ListView) findViewById(R.id.list_children);
		this.mTxtEmptyListChildren = (TextView) findViewById(R.id.txt_empty_list_children);
		this.mBtnAddChildren = (ImageButton) findViewById(R.id.btn_add_children);
		this.mListviewChildren.setOnItemClickListener(this);
		this.mListviewChildren.setOnItemLongClickListener(this);
		this.mBtnAddChildren.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_children:
			Intent intent = new Intent(this, AddChildActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_Children);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_ADD_Children) {
			if(resultCode == RESULT_OK) {
				//refresh the listView
				if(mListChildren == null || !mListChildren.isEmpty()) {
					mListChildren = new ArrayList<Child>();
				}

				if(mChildDao == null)
					mChildDao = new ChildDAO(this);
				mListChildren = mChildDao.getChildOfOrganisation(mOrganisationId);
				if(mAdapter == null) {
					mAdapter = new ListChildAdapter(this, mListChildren);
					mListviewChildren.setAdapter(mAdapter);
					if(mListviewChildren.getVisibility() != View.VISIBLE) {
						mTxtEmptyListChildren.setVisibility(View.GONE);
						mListviewChildren.setVisibility(View.VISIBLE);
					}
				}
				else {
					mAdapter.setItems(mListChildren);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
		else 
			super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mChildDao.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Child clickedChild = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : "+ clickedChild.getFirstName()+" "+ clickedChild.getLastName());
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Child clickedChild = mAdapter.getItem(position);
		Log.d(TAG, "longClickedItem : "+ clickedChild.getFirstName()+" "+ clickedChild.getLastName());
		
		showDeleteDialogConfirmation(clickedChild);
		return true;
	}
	
	private void showDeleteDialogConfirmation(final Child child) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
        alertDialogBuilder.setTitle("Delete");
		alertDialogBuilder
				.setMessage("Are you sure you want to delete the child \""
						+ child.getFirstName() + " "
						+ child.getLastName() + "\"");
 
        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete the child and refresh the list
				if(mChildDao != null) {
					mChildDao.deleteChild(child);
					
					//refresh the listView
					mListChildren.remove(child);
					if(mListChildren.isEmpty()) {
						mListviewChildren.setVisibility(View.GONE);
						mTxtEmptyListChildren.setVisibility(View.VISIBLE);
					}

					mAdapter.setItems(mListChildren);
					mAdapter.notifyDataSetChanged();
				}
				
				dialog.dismiss();
				Toast.makeText(ListChildActivity.this, R.string.child_deleted_successfully, Toast.LENGTH_SHORT).show();

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
