package com.example.childsafetyapp;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddChildActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	public static final String TAG = "AddChildActivity";
	DatePickerDialog datePickerDialog;
	EditText datePicker;
	private EditText mTxtFirstName;
	private EditText mTxtLastName;
	private EditText mTxtAddress;
	private EditText mTxtPhoneNumber;
	private EditText mTxtEmail;
	private EditText mTxtDate;
	private Spinner mSpinnerCompany;
	private Button mBtnAdd;

	private OrganisationDAO mOrganisationDao;
	private ChildDAO mChildDao;
	
	private Organisation mSelectedOrganisation;
	private SpinnerOrganisationsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_child);

		initDatePicker();
		datePicker= findViewById(R.id.txt_datepiker);

		initViews();

		this.mOrganisationDao = new OrganisationDAO(this);
		this.mChildDao = new ChildDAO(this);
		
		//fill the spinner with organisation
		List<Organisation> listCompanies = mOrganisationDao.getAllOrganisations();
		if(listCompanies != null) {
			mAdapter = new SpinnerOrganisationsAdapter(this, listCompanies);
			mSpinnerCompany.setAdapter(mAdapter);
			mSpinnerCompany.setOnItemSelectedListener(this);
		}
	}

	private void initDatePicker() {
		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				month = month+1;
				String date = makeDateString(day, month, year);
				datePicker.setText(date);
			}
		};

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		int style= AlertDialog.THEME_HOLO_LIGHT;
		datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
	}

	private String makeDateString(int day, int month, int year){

		return  getMonthFormat(month) + " " + day + " " + year;
	}
	private String getMonthFormat(int month) {

		if (month ==1)
			return "JAN";
		if (month ==2)
			return "FEB";
		if (month ==3)
			return "MAR";
		if (month ==4)
			return "APR";
		if (month ==5)
			return "MAY";
		if (month ==6)
			return "JUN";
		if (month ==7)
			return "JUL";
		if (month ==8)
			return "AUG";
		if (month ==9)
			return "SEP";
		if (month ==10)
			return "OCT";
		if (month ==11)
			return "NOV";
		if (month ==12)
			return "DEC";
		return "JAN";
	}
	public void openDatePickers(View view){

		datePickerDialog.show();

	}

	private void initViews() {
		this.mTxtFirstName = (EditText) findViewById(R.id.txt_first_name);
		this.mTxtLastName = (EditText) findViewById(R.id.txt_last_name);
		this.mTxtAddress = (EditText) findViewById(R.id.txt_address);
		this.mTxtPhoneNumber = (EditText) findViewById(R.id.txt_phone_number);
		this.mTxtEmail = (EditText) findViewById(R.id.txt_email);
		this.mTxtDate = (EditText) findViewById(R.id.txt_datepiker);
		this.mSpinnerCompany = (Spinner) findViewById(R.id.spinner_companies);
		this.mBtnAdd = (Button) findViewById(R.id.btn_add);

		this.mBtnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			Editable firstName = mTxtFirstName.getText();
			Editable lastName = mTxtLastName.getText();
			Editable address = mTxtAddress.getText();
			Editable phoneNumber = mTxtPhoneNumber.getText();
			Editable email = mTxtEmail.getText();
			Editable date = mTxtDate.getText();
			mSelectedOrganisation = (Organisation) mSpinnerCompany.getSelectedItem();
			if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
					&& !TextUtils.isEmpty(address) && !TextUtils.isEmpty(date)
					&& !TextUtils.isEmpty(email) && mSelectedOrganisation != null
					&& !TextUtils.isEmpty(phoneNumber)) {
				// add the organisation to database
				Child createdChild = mChildDao.createEmploye(firstName.toString(), lastName.toString(), address.toString(), email.toString(), phoneNumber.toString(), date.toString(), mSelectedOrganisation.getId());
				
				Log.d(TAG, "added child : "+ createdChild.getFirstName()+" "+ createdChild.getLastName());
				setResult(RESULT_OK);
				finish();
			}
			else {
				Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mOrganisationDao.close();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mSelectedOrganisation = mAdapter.getItem(position);
		Log.d(TAG, "selectedOrganisation : "+ mSelectedOrganisation.getName());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
