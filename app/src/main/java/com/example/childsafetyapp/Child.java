package com.example.childsafetyapp;

import java.io.Serializable;

public class Child implements Serializable {

	public static final String TAG = "Child";
	private static final long serialVersionUID = -7406082437623008161L;

	private long mId;
	private String mFirstName;
	private String mLastName;
	private String mAddress;
	private String mPhoneNumber;
	private String mEmail;
	private String mSalary;
	private Organisation mOrganisation;

	public Child() {
		
	}

	public Child(String firstName, String lastName, String address, String phoneNumber, String email, String salary) {
		this.mFirstName = firstName;
		this.mLastName = lastName;
		this.mAddress = address;
		this.mPhoneNumber = phoneNumber;
		this.mEmail = email;
		this.mSalary = salary;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getSalary() {
		return mSalary;
	}

	public void setDate(String mSalary) {
		this.mSalary = mSalary;
	}

	public Organisation getOrganisation() {
		return mOrganisation;
	}

	public void setOrganisation(Organisation mOrganisation) {
		this.mOrganisation = mOrganisation;
	}
}
