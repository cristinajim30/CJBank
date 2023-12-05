package com.cjbank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class with Common constants and a method
public class Commons {
	public static final String FILE_DIRECTORY = "Files";
	public static final String FILE_JSON = "flows.json";
	public static final String FILE_XML = "accounts.xml";
	public static final String DATE_FORMATTER = "yyyy-MM-dd";
	// constants to flows.json
	public static final String FLOW_COMMENT = "comment";
	public static final String FLOW_IDENTIFIER = "identifier";
	public static final String FLOW_AMOUNT = "amount";
	public static final String FLOW_TARGETACCOUNTNUMBER = "targetAccountNumber";
	public static final String FLOW_EFFECT = "effect";
	public static final String FLOW_DATE = "date";
	public static final String FLOW_ACCOUNTNUMBERISSUER = "accountNumberIssuer";

	public static final String LOGGER_NAME = "Log Cjbank: ";
	public static final String FICH_LOG = "logfile.log";

	/**
	 * Method that creates the File directory if it doesn't exist
	 */
	public static void checkFileDirectoryExists(Path path, Logger logger) {
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
				logger.log(Level.INFO, "Directorio creado: {0}", path);
			} catch (IOException e) {
				logger.log(Level.WARNING, "IOException in checkFileDirectoryExists: ", e);
				e.printStackTrace();
			}
		}
	}
}
