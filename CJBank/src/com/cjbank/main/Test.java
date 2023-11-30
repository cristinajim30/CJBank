package com.cjbank.main;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.cjbank.client.Client;
import com.cjbank.client.account.Account;
import com.cjbank.client.account.CurrentAccount;
import com.cjbank.client.account.SavingsAccount;

public class Test {

	public static void main(String[] args) {
		// 1.1.2 Creation of main class for tests

		// use a LinkedHashSet to avoid repeated elements and to maintain the order in
		// which they are inserted
		Collection<Client> clientsSet = new LinkedHashSet();

		clientsSet = loadClients();
		displayClients(clientsSet);

		// 1.2.3 Creation of the tablea account
		Collection<Account> accounts = loadAccounts(clientsSet);
		displayAccounts(accounts);
	}

	private static Collection<Client> loadClients() {
		// create a temporal Array to save newClients array.
		Collection<Client> tempSet = new LinkedHashSet();

		// create 10 clients
		for (int i = 0; i < 10; i++) {
			tempSet.add(generateClient(i + 1));
		}

		return tempSet;
	}

	private static Client generateClient(int clientNumber) {
		String name = "name" + clientNumber;
		String firstName = "firstname" + clientNumber;

		return new Client(name, firstName);
	}

	private static void displayClients(Collection<Client> clientsSet) {
		clientsSet.stream().forEach(client -> System.out.println(client.toString()));

	}

	private static Collection<Account> loadAccounts(Collection<Client> clientsSet) {
		Collection<Account> accounts = new LinkedHashSet();

		for (Client client : clientsSet) {
			// Generate a saving account with zero balance
			SavingsAccount savingsAccount = new SavingsAccount("Savings", client);
			accounts.add(savingsAccount);

			// Generate a current account with zero balance
			CurrentAccount currentAccount = new CurrentAccount("Current", client);
			accounts.add(currentAccount);
		}

		return accounts;
	}

	public static void displayAccounts(Collection<Account> accounts) {
		accounts.stream().forEach(account -> System.out.println(account.toString()));
	}

}
