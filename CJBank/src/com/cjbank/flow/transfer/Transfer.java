package com.cjbank.flow.transfer;

import java.sql.Date;

import com.cjbank.flow.Flow;

//1.3.3 Creation of the Transfer class
public class Transfer extends Flow {
	private int accountNumberIssuer;

	public Transfer(String comment, int identifier, double amount, int targetAccountNumber, boolean effect, Date date,
			int accountNumberIssuer) {
		super(comment, identifier, amount, targetAccountNumber, effect, date);
		this.accountNumberIssuer = accountNumberIssuer;
	}

	public int getAccountNumberIssuer() {
		return accountNumberIssuer;
	}

	public void setAccountNumberIssuer(int accountNumberIssuer) {
		this.accountNumberIssuer = accountNumberIssuer;
	}

}
