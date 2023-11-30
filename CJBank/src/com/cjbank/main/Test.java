package com.cjbank.main;

import java.util.Arrays;

import com.cjbank.client.Client;

public class Test {
	// 1.1.2 Creation of main class for tests

	public static void main(String[] args) {
		Client[] clientsArray = new Client[10];

		loadClients(clientsArray);
		displayClients(clientsArray);

	}

	private static void loadClients(Client[] clientsArray) {
		for (int i = 0; i < clientsArray.length; i++) {
			clientsArray[i] = generateClient(i + 1);
		}
	}

	private static Client generateClient(int clientNumber) {
		String name = "name" + clientNumber;
		String firstName = "firstname" + clientNumber;
		return new Client(name, firstName);
	}

	private static void displayClients(Client[] clientsArray) {
		Arrays.stream(clientsArray).forEach(System.out::println);

	}

}