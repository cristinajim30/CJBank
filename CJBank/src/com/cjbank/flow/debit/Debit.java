package com.cjbank.flow.debit;

import java.time.LocalDate;

import com.cjbank.flow.Flow;

//1.3.3 Creation of the Debit class
public class Debit extends Flow {

	public Debit(String comment, int identifier, double amount, int targetAccountNumber, boolean effect,
			LocalDate date) {
		super(comment, identifier, amount, targetAccountNumber, effect, date);
	}

}
