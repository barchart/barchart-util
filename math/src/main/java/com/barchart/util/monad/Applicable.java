package com.barchart.util.monad;

// MJS: Haskell like free standing function in Java

public interface Applicable<T1, T2> {
	public T2 apply(T1 arg) throws Failure;
}
