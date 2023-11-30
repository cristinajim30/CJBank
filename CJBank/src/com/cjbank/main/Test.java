package com.cjbank.main;

import java.util.Collection;
//import java.util.HashSet;
import java.util.LinkedHashSet;

import com.cjbank.client.Client;

public class Test {
	// 1.1.2 Creation of main class for tests

	public static void main(String[] args) {
		/*
		 * use a LinkedHashSet to avoid repeated elements and to maintain the order in
		 * which they are inserted
		 */
		Collection<Client> clientsSet = new LinkedHashSet();

		clientsSet = loadClients();
		displayClients(clientsSet);
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

}
