package com.barchart.util.monad;

// MJS: Haskell like free standing function in Java

@SuppressWarnings("serial")
public class Failure extends Exception {

	public Failure(String string) {
		super(string);
	}
}
