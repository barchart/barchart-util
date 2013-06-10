package com.barchart.util.functional;

/**
 * 
 * A functor is a container, and the function of functors is to take an ordinary
 * function, which we write: f: a → b (pronounced
 * "the function f from (type) a to (type) b" and means that f takes an argument
 * of type a and returns a result of type b), and give a function g: Functor a →
 * Functor b.
 * 
 * Functor basically is a container for the function fmap: Functor f ⇒ (a → b) →
 * (f a → f b)
 * 
 * @author m-sokolowski
 * 
 * @param <F>
 * @param <T>
 */

public interface Functor<F, T> {

	public <T1> Applicable<? extends Functor<F, T>, ? extends Functor<F, T1>> fmap(
			Applicable<T, T1> f) throws Failure;

	public T arg() throws Failure;
}
