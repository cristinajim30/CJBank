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

		loadClients(clientsSet);
		displayClients(clientsSet);

	}

	private static void loadClients(Collection<Client> clientsSet) {
		// create a temporal Array to save newClients array.
		Collection<Client> tempSet = new LinkedHashSet();

		// create 10 clients
		for (int i = 0; i < 10; i++) {
			tempSet = generateClient(clientsSet.size() + 1);
			for (Client client : tempSet) {
				clientsSet.add(client);
			}
		}

	}

	private static Collection<Client> generateClient(int clientNumber) {
		Collection<Client> newClient = new LinkedHashSet();

		String name = "name" + clientNumber;
		String firstName = "firstname" + clientNumber;

		newClient.add(new Client(name, firstName));
		return newClient;
	}

	private static void displayClients(Collection<Client> clientsSet) {
		clientsSet.stream().forEach(client -> System.out.println(client.toString()));

	}

}
