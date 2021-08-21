package com.example.childsafetyapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrganisationDAO {

	public static final String TAG = "OrganisationDAO";

	// Database fields
	private SQLiteDatabase mDatabase;
	private DBHelper mDbHelper;
	private Context mContext;
	private String[] mAllColumns = { DBHelper.COLUMN_ORGANISATION_ID,
			DBHelper.COLUMN_ORGANISATION_NAME, DBHelper.COLUMN_ORGANISATION_ADDRESS,
			DBHelper.COLUMN_ORGANISATION_WEBSITE,
			DBHelper.COLUMN_ORGANISATION_PHONE_NUMBER};

	public OrganisationDAO(Context context) {
		this.mContext = context;
		mDbHelper = new DBHelper(context);
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

	public Organisation createOrganisation(String name, String address, String website,
										   String phoneNumber) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_ORGANISATION_NAME, name);
		values.put(DBHelper.COLUMN_ORGANISATION_ADDRESS, address);
		values.put(DBHelper.COLUMN_ORGANISATION_WEBSITE, website);
		values.put(DBHelper.COLUMN_ORGANISATION_PHONE_NUMBER, phoneNumber);
		long insertId = mDatabase
				.insert(DBHelper.TABLE_ORGANISATIONS, null, values);
		Cursor cursor = mDatabase.query(DBHelper.TABLE_ORGANISATIONS, mAllColumns,
				DBHelper.COLUMN_ORGANISATION_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Organisation newOrganisation = cursorToOrganisation(cursor);
		cursor.close();
		return newOrganisation;
	}

	public void deleteOrganisation(Organisation organisation) {
		long id = organisation.getId();
		// delete all children of this organisation
		ChildDAO childDao = new ChildDAO(mContext);
		List<Child> listChildren = childDao.getChildOfOrganisation(id);
		if (listChildren != null && !listChildren.isEmpty()) {
			for (Child e : listChildren) {
				childDao.deleteChild(e);
			}
		}

		System.out.println("the deleted organisation has the id: " + id);
		mDatabase.delete(DBHelper.TABLE_ORGANISATIONS, DBHelper.COLUMN_ORGANISATION_ID
				+ " = " + id, null);
	}

	public List<Organisation> getAllOrganisations() {
		List<Organisation> listOrganisations = new ArrayList<Organisation>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_ORGANISATIONS, mAllColumns,
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Organisation organisation = cursorToOrganisation(cursor);
				listOrganisations.add(organisation);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
		}
		return listOrganisations;
	}

	public Organisation getOrganisationById(long id) {
		Cursor cursor = mDatabase.query(DBHelper.TABLE_ORGANISATIONS, mAllColumns,
				DBHelper.COLUMN_ORGANISATION_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		Organisation organisation = cursorToOrganisation(cursor);
		return organisation;
	}

	protected Organisation cursorToOrganisation(Cursor cursor) {
		Organisation organisation = new Organisation();
		organisation.setId(cursor.getLong(0));
		organisation.setName(cursor.getString(1));
		organisation.setAddress(cursor.getString(2));
		organisation.setWebsite(cursor.getString(3));
		organisation.setPhoneNumber(cursor.getString(4));
		return organisation;
	}

}
