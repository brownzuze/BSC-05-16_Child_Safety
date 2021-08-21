package com.example.childsafetyapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ChildDAO {

	public static final String TAG = "ChildDAO";

	private Context mContext;

	// Database fields
	private SQLiteDatabase mDatabase;
	private DBHelper mDbHelper;
	private String[] mAllColumns = { DBHelper.COLUMN_CHILD_ID,
			DBHelper.COLUMN_CHILD_FIRST_NAME,
			DBHelper.COLUMN_CHILD_LAST_NAME, DBHelper.COLUMN_CHILD_ADDRESS,
			DBHelper.COLUMN_CHILD_EMAIL,
			DBHelper.COLUMN_CHILD_PHONE_NUMBER,
			DBHelper.COLUMN_CHILD_SALARY, DBHelper.COLUMN_CHILD_COMPANY_ID};

	public ChildDAO(Context context) {
		mDbHelper = new DBHelper(context);
		this.mContext = context;
		// open the database
		try {
			open();
		} catch (SQLException e) {
			Log.e(TAG, "SQLException on openning database " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		mDatabase = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public Child createChild(String firstName, String lastName,
							 String address, String email, String phoneNumber, String date,
							 long organisationId) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_CHILD_FIRST_NAME, firstName);
		values.put(DBHelper.COLUMN_CHILD_LAST_NAME, lastName);
		values.put(DBHelper.COLUMN_CHILD_ADDRESS, address);
		values.put(DBHelper.COLUMN_CHILD_EMAIL, email);
		values.put(DBHelper.COLUMN_CHILD_PHONE_NUMBER, phoneNumber);
		values.put(DBHelper.COLUMN_CHILD_SALARY, date);
		values.put(DBHelper.COLUMN_CHILD_COMPANY_ID, organisationId);
		long insertId = mDatabase
				.insert(DBHelper.TABLE_CHILDREN, null, values);
		Cursor cursor = mDatabase.query(DBHelper.TABLE_CHILDREN, mAllColumns,
				DBHelper.COLUMN_CHILD_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Child newChild = cursorToChild(cursor);
		cursor.close();
		return newChild;
	}

	public void deleteChild(Child child) {
		long id = child.getId();
		System.out.println("the deleted child has the id: " + id);
		mDatabase.delete(DBHelper.TABLE_CHILDREN, DBHelper.COLUMN_CHILD_ID
				+ " = " + id, null);
	}

	public List<Child> getAllChildren() {
		List<Child> listChildren = new ArrayList<Child>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_CHILDREN, mAllColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Child child = cursorToChild(cursor);
			listChildren.add(child);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listChildren;
	}

	public List<Child> getChildOfOrganisation(long organisationId) {
		List<Child> listChildren = new ArrayList<Child>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_CHILDREN, mAllColumns,
				DBHelper.COLUMN_ORGANISATION_ID + " = ?",
				new String[] { String.valueOf(organisationId) }, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Child child = cursorToChild(cursor);
			listChildren.add(child);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listChildren;
	}

	private Child cursorToChild(Cursor cursor) {
		Child child = new Child();
		child.setId(cursor.getLong(0));
		child.setFirstName(cursor.getString(1));
		child.setLastName(cursor.getString(2));
		child.setAddress(cursor.getString(3));
		child.setEmail(cursor.getString(4));
		child.setPhoneNumber(cursor.getString(5));
		child.setDate(cursor.getString(6));

		// get The organisation by id
		long organisationId = cursor.getLong(7);
		OrganisationDAO dao = new OrganisationDAO(mContext);
		Organisation organisation = dao.getOrganisationById(organisationId);
		if (organisation != null)
			child.setOrganisation(organisation);

		return child;
	}

}
