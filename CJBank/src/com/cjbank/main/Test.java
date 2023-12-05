package com.cjbank.main;

import java.io.IOException;
import java.nio.file.FileSystems;
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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

public class Test extends com.cjbank.Commons {
	// create a logger object for logging
	public static final Logger logger = Logger.getLogger(LOGGER_NAME);

	public static void main(String[] args) {
		try {
			FileHandler fh = new FileHandler(FICH_LOG, true);
			SimpleFormatter formato = new SimpleFormatter();
			logger.addHandler(fh);
			fh.setFormatter(formato);
		} catch (SecurityException | IOException e) {
			logger.log(Level.WARNING, "Exception creating Logger object: ", e);
			e.printStackTrace();
		}

		// 1.1.2 Creation of main class for tests

		/*
		 * use a LinkedHashSet to avoid repeated elements and to maintain the order in
		 * which they are inserted
		 */
		Collection<Client> clientsSet;

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
		saveFlowsToJson(FileSystems.getDefault().getPath(FILE_DIRECTORY, FILE_JSON), flows);

		// 2.2 XML file of account
		writeAccountsToXML(FileSystems.getDefault().getPath(FILE_DIRECTORY, FILE_XML), accounts);
		logger.setLevel(Level.FINE);
		logger.log(Level.FINE, "-----The process has been successfully completed-----");
	}

	private static Collection<Client> loadClients() {
		// create a temporal Array to save newClients array.
		Collection<Client> tempSet = new LinkedHashSet<>();

		// create 10 clients
		for (int i = 0; i < 10; i++) {
			tempSet.add(generateClient(i + 1));
		}
		logger.setLevel(Level.FINE);
		logger.log(Level.FINE, "Clients created");
		return tempSet;
	}

	private static Client generateClient(int clientNumber) {
		String name = "name" + clientNumber;
		String firstName = "firstname" + clientNumber;
		logger.log(Level.INFO, "name {0}, firstname {0}", clientNumber);
		return new Client(name, firstName);
	}

	private static void displayClients(Collection<Client> clientsSet) {
		System.out.println("---------CLIENTS----------");
		clientsSet.stream().forEach(client -> System.out.println(client.toString()));

	}

	private static Collection<Account> loadAccounts(Collection<Client> clientsSet) {
		Collection<Account> accounts = new LinkedHashSet<>();

		for (Client client : clientsSet) {
			// Generate a saving account with zero balance
			SavingsAccount savingsAccount = new SavingsAccount("Savings", client);
			accounts.add(savingsAccount);

			// Generate a current account with zero balance
			CurrentAccount currentAccount = new CurrentAccount("Current", client);
			accounts.add(currentAccount);
		}
		logger.log(Level.INFO, "accounts LinkHashSet created: {0}", accounts);
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
		logger.log(Level.INFO, "account Hashtable created");
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
		logger.log(Level.INFO, "flowDate for Flows: {0}", flowDate);
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

		logger.log(Level.INFO, "flowList created: {0}", flowList);
		return flowList;
	}

	/***
	 * Method to save Flow array into a json file
	 * 
	 * @param jsonFilePath
	 * @param flows
	 */
	private static void saveFlowsToJson(Path jsonFilePath, Collection<Flow> flows) {

		// Build a JSON array with the elements of flowList
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		// date format in flows and append to jsonObjectBuilder
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

		for (Flow flow : flows) {
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add(FLOW_COMMENT, flow.getComment())
					.add(FLOW_IDENTIFIER, flow.getIdentifier()).add(FLOW_AMOUNT, flow.getAmount())
					.add(FLOW_TARGETACCOUNTNUMBER, flow.getTargetAccountNumber()).add(FLOW_EFFECT, flow.isEffect())
					.add(FLOW_DATE, flow.getDate().format(dateFormatter));

			// If it is a transfer, add AccountNumberIssuer to JSON object
			if (flow instanceof Transfer) {
				Transfer transfer = (Transfer) flow;
				jsonObjectBuilder.add(FLOW_ACCOUNTNUMBERISSUER, transfer.getAccountNumberIssuer());
			}
			// add our ObjectBuilder into the ArrayBuilder
			jsonArrayBuilder.add(jsonObjectBuilder);
		}
		// build the jsonArray
		JsonArray jsonArray = jsonArrayBuilder.build();
		logger.log(Level.INFO, "jsonArray created: {0}", jsonArray);
		writeJsonFile(jsonFilePath, jsonArray);

	}

	/**
	 * Method to write json array in a json file
	 * 
	 * @param jsonFilePath
	 * @param jsonArray
	 */
	private static void writeJsonFile(Path jsonFilePath, JsonArray jsonArray) {
		try {
			// Check if the file and the "File" directory already exists
			boolean fileExists = Files.exists(jsonFilePath);
			checkFileDirectoryExists(FileSystems.getDefault().getPath(FILE_DIRECTORY), logger);
			// If the file already exists, delete it before writing the new data.
			if (fileExists) {
				logger.log(Level.INFO, "JsonFile already exists and its to be deleted: {0}", jsonFilePath);
				Files.delete(jsonFilePath);
			}

			// Write the Json array in the file
			Files.writeString(jsonFilePath, jsonArray.toString());
			logger.setLevel(Level.FINE);
			logger.log(Level.FINE, "Flows stored in the JSON file: {0}", jsonFilePath);
			System.out.println("Flows stored in the JSON file: " + jsonFilePath);

		} catch (IOException e) {
			logger.setLevel(Level.WARNING);
			logger.log(Level.WARNING, "IOException in writeJsonFile: ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Method to write an XML document with the information of the accounts
	 * ArrayList
	 * 
	 * @param xmlFilePath
	 * @param accounts
	 */
	private static void writeAccountsToXML(Path xmlFilePath, Collection<Account> accounts) {
		try {
			// Create a new instance of DocumentBuilderFactory
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();

			// Create a new document XML
			Document doc = docBuilder.newDocument();

			// Create the root element
			Element rootElement = doc.createElement("accounts");
			doc.appendChild(rootElement);

			// Add items for each account
			for (Account account : accounts) {
				Element accountElement = doc.createElement("account");
				rootElement.appendChild(accountElement);

				Element accountNumberElement = doc.createElement("accountNumber");
				accountNumberElement.appendChild(doc.createTextNode(String.valueOf(account.getAccountNumber())));
				accountElement.appendChild(accountNumberElement);

				Element labelElement = doc.createElement("label");
				labelElement.appendChild(doc.createTextNode(account.getLabel()));
				accountElement.appendChild(labelElement);

				Element balanceElement = doc.createElement("balance");
				balanceElement.appendChild(doc.createTextNode(String.valueOf(account.getBalance())));
				accountElement.appendChild(balanceElement);

				Element clientElement = doc.createElement("client");
				accountElement.appendChild(clientElement);

				// Adds the child nodes of the Client element
				Element nameElement = doc.createElement("name");
				nameElement.appendChild(doc.createTextNode(account.getClient().getName()));
				clientElement.appendChild(nameElement);

				Element firstnameElement = doc.createElement("firstname");
				firstnameElement.appendChild(doc.createTextNode(account.getClient().getFirstname()));
				clientElement.appendChild(firstnameElement);

				Element clientNumberElement = doc.createElement("clientNumber");
				clientNumberElement
						.appendChild(doc.createTextNode(String.valueOf(account.getClient().getClientNumber())));
				clientElement.appendChild(clientNumberElement);

				// Adds the account element to the root element
				rootElement.appendChild(accountElement);
			}
			logger.log(Level.INFO, "Xml Documment created");
			writeXmlFile(xmlFilePath, doc);
		} catch (ParserConfigurationException e) {
			logger.setLevel(Level.WARNING);
			logger.log(Level.WARNING, "ParserConfigurationException in writeAccountsToXML: ", e);
			e.printStackTrace();
		}

	}

	/**
	 * method to write the XML document into a file
	 * 
	 * @param xmlFilePath
	 * @param doc
	 */
	private static void writeXmlFile(Path xmlFilePath, Document doc) {
		try {
			// Check if the file already exists
			boolean fileExists = Files.exists(xmlFilePath);
			checkFileDirectoryExists(FileSystems.getDefault().getPath(FILE_DIRECTORY), logger);
			// If the file already exists, delete it before writing the new data.
			if (fileExists) {
				logger.log(Level.INFO, "The XML file exists, so delete it: {0}", xmlFilePath);
				Files.delete(xmlFilePath);
			}

			// Saves the XML document into the specified file
			TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFilePath.toFile());

			// Set the output format
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Perform the transformation and save the file
			transformer.transform(source, result);
			logger.setLevel(Level.FINE);
			logger.log(Level.FINE, "Accounts stored in the XML file: {0}", xmlFilePath);
			System.out.println("Accounts stored in the XML file: " + xmlFilePath);

		} catch (TransformerException | IOException e) {
			logger.setLevel(Level.WARNING);
			logger.log(Level.WARNING, "Exception in writeXMLFile: ", e);
			e.printStackTrace();
		}

	}

}
