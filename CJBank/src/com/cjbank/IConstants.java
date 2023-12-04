package com.cjbank;

public interface IConstants {
	public final String FILE_DIRECTORY = "Files";
	public final String FILE_JSON = "flows.json";
	public final String FILE_XML = "accounts.xml";
	public final String DATE_FORMATTER = "yyyy-MM-dd";
	// constants to flows.json
	public final String FLOW_COMMENT = "comment";
	public final String FLOW_IDENTIFIER = "identifier";
	public final String FLOW_AMOUNT = "amount";
	public final String FLOW_TARGETACCOUNTNUMBER = "targetAccountNumber";
	public final String FLOW_EFFECT = "effect";
	public final String FLOW_DATE = "date";
	public final String FLOW_ACCOUNTNUMBERISSUER = "accountNumberIssuer";

	public static final String LOGGER_NAME = "Log Cjbank: ";
	public static final String FICH_LOG = "logfile.log";

	public static void checkFileDirectoryExists() {
	}
}
