package com.cjbank.client;

public class Client {
	// 1.1.1 Creation of the client class
	private String name;
	private String firstname;
	private int clientNumber;
	private static int totalClients = 0;

	public Client(String name, String firstname) {
		this.name = name;
		this.firstname = firstname;
		System.out.println("totalClients: " + totalClients);
		this.clientNumber = ++totalClients;
		System.out.println("totalClients dsp: " + totalClients);
		System.out.println("clientNumber dsp: " + totalClients);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}

	@Override
	public String toString() {
		return "Client [name= " + name + ", firstname= " + firstname + ", clientNumber= " + clientNumber + "]";
	}

}
