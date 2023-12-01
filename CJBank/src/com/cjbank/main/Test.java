package com.cjbank.main;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;

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

		// 1.3.1 Adaptation of the table of accounts

		// Create a Hashtable using a method
		Hashtable<Integer, Account> accountHashtable = createHashtable(accounts);

		// Display the Hashtable in ascending order based on the balance
		displayHashtable(accountHashtable);
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
		System.out.println("antes return new Client");
		return new Client(name, firstName);
	}

	private static void displayClients(Collection<Client> clientsSet) {
		System.out.println("---------CLIENTS----------");
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

	private static void displayAccounts(Collection<Account> accounts) {
		System.out.println("\n---------ACCOUNTS----------");
		accounts.stream().forEach(account -> System.out.println(account.toString()));
	}

	private static Hashtable<Integer, Account> createHashtable(Collection<Account> accounts) {
		Hashtable<Integer, Account> accountHashtable = new Hashtable<>();

		for (Account account : accounts) {
			accountHashtable.put(account.getAccountNumber(), account);
		}

		return accountHashtable;
	}

	private static void displayHashtable(Hashtable<Integer, Account> accountHashtable) {
		System.out.println("\n---------HASTABLE ACCOUNTS----------");
		accountHashtable.entrySet().stream()
				.sorted(Map.Entry.comparingByValue((a1, a2) -> Double.compare(a1.getBalance(), a2.getBalance())))
				.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue().toString()));

	}

}
