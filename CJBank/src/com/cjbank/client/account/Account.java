package com.cjbank.client.account;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Predicate;

import com.cjbank.client.Client;
import com.cjbank.flow.Flow;
import com.cjbank.flow.credit.Credit;
import com.cjbank.flow.debit.Debit;
import com.cjbank.flow.transfer.Transfer;

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

	// 1.3.5 Updating accounts: Modify the balance setter method
	public void setBalance(double amount, Flow flowType) {
		if (flowType instanceof Credit) {
			// credit -> add the amount to the balance
			this.balance += amount;
		} else if (flowType instanceof Debit) {
			// debit -> subtract the amount from the balance
			this.balance -= amount;
		} else if (flowType instanceof Transfer) {
			Transfer transfer = (Transfer) flowType;
			if (this.accountNumber == transfer.getTargetAccountNumber()) {
				/*
				 * If the account number is equal to the target account number, add the amount
				 * to the balance
				 */
				this.balance += amount;
			} else if (this.accountNumber == transfer.getAccountNumberIssuer()) {
				/*
				 * If the account number is equal to the issuer account number, subtract the
				 * amount from the balance
				 */
				this.balance -= amount;
			}
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
		return "Account [accountNumber=" + accountNumber + ", label=" + label + ", balance=" + balance + ", client="
				+ client + "]";
	}

	// 1.3.5 Updating accounts: Method to update balances
	public static void updateBalances(Collection<Flow> flows, Hashtable<Integer, Account> accounts) {
		// Iterate the flows and update the corresponding accounts
		flows.forEach(flow -> {
			Account targetAccount = accounts.get(flow.getTargetAccountNumber());
			targetAccount.setBalance(flow.getAmount(), flow);

			// if is a transfer, update the issuer account's balance
			if (flow instanceof Transfer) {
				Transfer transfer = (Transfer) flow;
				Account issuerAccount = accounts.get(transfer.getAccountNumberIssuer());
				issuerAccount.setBalance(flow.getAmount(), flow);
			}
		});

		/*
		 * Check for accounts with negative balances using Optional and Predicate
		 * function
		 */
		// create a predicate that filters out accounts with negative balances
		Predicate<Account> hasNegativeBalance = account -> account.getBalance() < 0;

		// converts the accounts collection values into a stream filtering by predicate
		Optional<Account> accountWithNegativeBalance = accounts.values().stream().filter(hasNegativeBalance)
				.findFirst();

		/*
		 * check if there is an account with negative balance and display a message for
		 * that account
		 */
		accountWithNegativeBalance.ifPresent(account -> System.out.println(
				"Account " + account.getAccountNumber() + " has a negative balance: " + account.getBalance() + "€"));
	}

}
