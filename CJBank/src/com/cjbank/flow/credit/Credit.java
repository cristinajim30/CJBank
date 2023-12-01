package com.cjbank.flow.credit;

import java.time.LocalDate;

import com.cjbank.flow.Flow;

//1.3.3 Creation of the Credit class
public class Credit extends Flow {

	public Credit(String comment, int identifier, double amount, int targetAccountNumber, boolean effect,
			LocalDate date) {
		super(comment, identifier, amount, targetAccountNumber, effect, date);
	}

}
