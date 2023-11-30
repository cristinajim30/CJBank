package com.cjbank.client.account;

import com.cjbank.client.Client;

public abstract class Account {
	// 1.2.1 Creation of the account class
	protected String label;
	protected double balance;
	protected int accountNumber;
	protected Client client;

	// Static variable to know total accounts
	protected static int totalAccounts = 1;

	public Account(String label, Client client) {
		this.label = label;
		this.client = client;
		this.accountNumber = totalAccounts++;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double amount, String flowType) {
		switch (flowType.toLowerCase()) {
		case "transfer":
			// Modify balance accordingly
		case "credit":
			// Modify balance accordingly
		case "debit":
			// Modify balance accordingly
		default:
			this.balance = amount;
		}

	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Account [label=" + label + ", balance=" + balance + ", accountNumber=" + accountNumber + ", client="
				+ client + "]";
	}

}
