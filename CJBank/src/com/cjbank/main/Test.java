package com.cjbank.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;

import com.cjbank.client.Client;
import com.cjbank.client.account.Account;
import com.cjbank.client.account.CurrentAccount;
import com.cjbank.client.account.SavingsAccount;
import com.cjbank.flow.Flow;
import com.cjbank.flow.credit.Credit;
import com.cjbank.flow.debit.Debit;
import com.cjbank.flow.transfer.Transfer;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

public class Test {

	public static void main(String[] args) {
		// 1.1.2 Creation of main class for tests

		/*
		 * use a LinkedHashSet to avoid repeated elements and to maintain the order in
		 * which they are inserted
		 */
		Collection<Client> clientsSet = new LinkedHashSet();

		clientsSet = loadClients();
		displayClients(clientsSet);

		// 1.2.3 Creation of the tablea account
		Collection<Account> accounts = loadAccounts(clientsSet);
		displayAccounts(accounts);

		// 1.3.1 Adaptation of the table of accounts

		// Create a Hashtable using a method
		Hashtable<Integer, Account> accountHashtable = createHashtable(accounts);

		// 1.3.4 Creation of the flow array
		// Create a Flow array using the method
		Collection<Flow> flows = loadFlows(accounts);

		// 1.3.5 Updating accounts
		Account.updateBalances(flows, accountHashtable);
		// display Hashtable with the update balances
		displayHashtable(accountHashtable);

		// 2.1 Json File of Flows
		Path jsonFilePath = Path.of("flows.json");
		saveFlowsToJson(jsonFilePath, flows);
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
		// System.out.println("antes return new Client");
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

	/* Method to display the Hashtable in ascending order based on the balance */
	private static void displayHashtable(Hashtable<Integer, Account> accountHashtable) {
		System.out.println("\n---------HASTABLE ACCOUNTS----------");
		accountHashtable.entrySet().stream()
				.sorted(Map.Entry.comparingByValue((a1, a2) -> Double.compare(a1.getBalance(), a2.getBalance())))
				.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue().toString()));
	}

	/***
	 * Method to load flows collection
	 * 
	 * @param accounts collection
	 * @return a list of flows
	 */
	private static Collection<Flow> loadFlows(Collection<Account> accounts) {
		Collection<Flow> flowList = new ArrayList<>();

		LocalDate currentDate = LocalDate.now();
		// Date of flows, 2 days after the current date
		LocalDate flowDate = currentDate.plus(Period.ofDays(2));
		// variable to increment the flow identifier
		int flowId = 0;

		// Debit of 50 euros from account 1
		flowList.add(new Debit("Debit of 50€", ++flowId, 50.0, 1, false, flowDate));

		// Credit of 100.50 euros on all current accounts
		for (Account account : accounts) {
			if (account instanceof CurrentAccount) {
				flowList.add(
						new Credit("Credit of 100.50€", ++flowId, 100.50, account.getAccountNumber(), true, flowDate));
			}
		}

		// Credit of 1500 euros on all savings accounts
		for (Account account : accounts) {
			if (account instanceof SavingsAccount) {
				flowList.add(
						new Credit("Credit of 1500€", ++flowId, 1500.0, account.getAccountNumber(), true, flowDate));
			}
		}

		// Transfer of 50 euros from account 1 to account 2
		flowList.add(new Transfer("Transfer of 50€", ++flowId, 50.0, 2, true, flowDate, 1));

		return flowList;
	}

	// REMEMBER REMOVE THIS DUMMY METHOD
	private static void displayFlows(Collection<Flow> flows) {
		for (Flow flow : flows) {
			System.out.println("flowClass: " + flow.getClass().getSimpleName());
			System.out.println("flowIdentifier: " + flow.getIdentifier());
			System.out.println("targetAccountNumber: " + flow.getTargetAccountNumber());
			System.out.println("flowAmmount: " + flow.getAmount());
			System.out.println("flowComment: " + flow.getComment());
			System.out.println("flowDate: " + flow.getDate());
			if (flow instanceof Transfer) {
				Transfer t = (Transfer) flow;
				System.out.println("Issuer: " + t.getAccountNumberIssuer());
			}
			System.out.println("-----------------");
		}
	}

	/***
	 * Method to save Flow array into a json file
	 * 
	 * @param jsonFilePath
	 * @param flows
	 */
	private static void saveFlowsToJson(Path jsonFilePath, Collection<Flow> flows) {

		try {
			// Verifica si el archivo ya existe
			boolean fileExists = Files.exists(jsonFilePath);

			// Construye un array JSON con los elementos de flowList
			JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
			// to format date in flows and append to jsonObjectBuilder
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			for (Flow flow : flows) {
				JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add("comment", flow.getComment())
						.add("identifier", flow.getIdentifier()).add("amount", flow.getAmount())
						.add("targetAccountNumber", flow.getTargetAccountNumber()).add("effect", flow.isEffect())
						.add("date", flow.getDate().format(dateFormatter));

				// Si es una transferencia, agrega AccountNumberIssuer al objeto JSON
				if (flow instanceof Transfer) {
					Transfer transfer = (Transfer) flow;
					jsonObjectBuilder.add("accountNumberIssuer", transfer.getAccountNumberIssuer());
				}

				jsonArrayBuilder.add(jsonObjectBuilder);
			}

			JsonArray jsonArray = jsonArrayBuilder.build();

			// If the file already exists, delete it before writing the new data.
			if (fileExists) {
				Files.delete(jsonFilePath);
			}

			// Write the Json array in the file
			Files.writeString(jsonFilePath, jsonArray.toString());

			System.out.println("Flows guardados en el archivo JSON.");

		} catch (IOException e) {
			// Maneja la excepción, por ejemplo, registra o lanza una excepción
			// personalizada
			e.printStackTrace();
		}

	}
}
