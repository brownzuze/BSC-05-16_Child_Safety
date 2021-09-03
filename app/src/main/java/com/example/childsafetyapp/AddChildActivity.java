package com.example.childsafetyapp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddChildActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private static final String url="http://192.168.137.58/childsync/db_insert.php";

	public static final String TAG = "AddChildActivity";
	DatePickerDialog datePickerDialog;
	EditText datePicker;
	private EditText mTxtFirstName;
	private EditText mTxtLastName;
	private EditText mTxtGuardian;
	private EditText mTxtPhysicalAddress;
	private EditText mTxtPhoneNumber;
	private EditText mTxtDate;
	private Spinner mSpinnerOrganisation;
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
			mSpinnerOrganisation.setAdapter(mAdapter);
			mSpinnerOrganisation.setOnItemSelectedListener(this);
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
		this.mTxtGuardian = (EditText) findViewById(R.id.txt_guardian);
		this.mTxtPhysicalAddress = (EditText) findViewById(R.id.txt_physical_address);
		this.mTxtPhoneNumber = (EditText) findViewById(R.id.txt_phone_number);
		this.mTxtDate = (EditText) findViewById(R.id.txt_datepiker);
		this.mSpinnerOrganisation = (Spinner) findViewById(R.id.spinner_companies);
		this.mBtnAdd = (Button) findViewById(R.id.btn_add);

		this.mBtnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			/*Editable firstName = mTxtFirstName.getText();
			Editable lastName = mTxtLastName.getText();
			Editable guardian = mTxtGuardian.getText();
			Editable physicalAddress = mTxtPhysicalAddress.getText();
			Editable phoneNumber = mTxtPhoneNumber.getText();
			Editable date = mTxtDate.getText();
			mSelectedOrganisation = (Organisation) mSpinnerOrganisation.getSelectedItem();*/

			if(checkNetworkConnection()) {

				insertdata();
			}
			else {
				//Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
				savetolocalstorage();
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


	private void insertdata()
	{


		final String firstname=mTxtFirstName.getText().toString().trim();
		final String lastname=mTxtLastName.getText().toString().trim();
		final String guardian=mTxtGuardian.getText().toString().trim();
		final String physicaladdress=mTxtPhysicalAddress.getText().toString().trim();
		final String phonenumber=mTxtPhoneNumber.getText().toString().trim();
		final String date=mTxtDate.getText().toString().trim();
		mSelectedOrganisation = (Organisation) mSpinnerOrganisation.getSelectedItem();
		final String organisation= mSelectedOrganisation.getName();






		StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response)
			{

				mTxtFirstName.setText("");
				mTxtLastName.setText("");
				mTxtGuardian.setText("");
				mTxtPhysicalAddress.setText("");
				mTxtPhoneNumber.setText("");
				mTxtDate.setText("");

				Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

				try {
					JSONObject jsonObject = new JSONObject(response);
					  String Response = jsonObject.getString("response");
					  if(Response.equals("OK"))
					  {
						  savetolocalstorage();
					  }
					  else
					  {
					  	savetolocalstorage();
					  }
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				savetolocalstorage();
				//Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String,String> param=new HashMap<String,String>();
				param.put("t1",firstname);
				param.put("t2",lastname);
				param.put("t3",guardian);
				param.put("t4",physicaladdress);
				param.put("t5",phonenumber);
				param.put("t6",date);
				param.put("t7",organisation);




				return param;
			}
		};


		RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
		queue.add(request);

	}
	private void savetolocalstorage(){
		Editable firstName = mTxtFirstName.getText();
		Editable lastName = mTxtLastName.getText();
		Editable guardian = mTxtGuardian.getText();
		Editable physicalAddress = mTxtPhysicalAddress.getText();
		Editable phoneNumber = mTxtPhoneNumber.getText();
		Editable date = mTxtDate.getText();
		mSelectedOrganisation = (Organisation) mSpinnerOrganisation.getSelectedItem();
		if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
				&& !TextUtils.isEmpty(guardian) && !TextUtils.isEmpty(date)
				&& !TextUtils.isEmpty(phoneNumber) && mSelectedOrganisation != null
				&& !TextUtils.isEmpty(physicalAddress)) {
			// add the organisation to database
			Child createdChild = mChildDao.createChild(firstName.toString(), lastName.toString(), guardian.toString(), phoneNumber.toString(), physicalAddress.toString(), date.toString(), mSelectedOrganisation.getId());

			Log.d(TAG, "added child : "+ createdChild.getFirstName()+" "+ createdChild.getLastName());
			setResult(RESULT_OK);
			finish();
		}
	}
	public boolean checkNetworkConnection(){
		ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return  (networkInfo!= null && networkInfo.isConnected());
	}
}
