package com.barchart.util.functional;

/**
 * 
 * Join function that takes a composed monad M (M a) and returns the simplified
 * type of the composition, or M a.
 * 
 * @author m-sokolowski
 * 
 * @param <F>
 * @param <T>
 */
public interface Joinable<F, T> extends Functor<F, T> {
	public Functor<F, ?> join() throws Failure;
}
