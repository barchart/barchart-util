package com.barchart.util.common.collections;

/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Original source is from Guava, but was removed by the Guava team.
 * Modified for use by Barchart.
 */

/**
 * A mutable object that may contain a reference to another object. An instance
 * of this type either holds some non-null reference, or holds nothing; it is
 * never said to "hold {@code null}". The instance that is held can be set,
 * unset or changed at any time.
 *
 * <p>
 * This class is the mutable version of {@link Optional}. It is also very
 * similar to {@link AtomicReference} (without the atomic operations and
 * thread-safety).
 *
 * <p>
 * One common use of a {@code Holder} is to receive a result from an anonymous
 * inner class:
 * 
 * <pre class="code">
 *    {@code
 *
 *   final Holder<Result> resultHolder = Holder.absent();
 *   doIt(new InnerClass() {
 *     public void foo() {
 *       Result result = ...
 *       resultHolder.set(result);
 *     }
 *   });}
 * </pre>
 *
 * <b>Note:</b> two {@code Holder} instances are never considered equal, even if
 * they are holding the same instance at the time.
 *
 * @author Kurt Alfred Kluever
 * @author Kevin Bourrillion
 * @since Guava release 10
 */
public final class Holder<T> {
	// Static factories

	/**
	 * Returns a new, modifiable holder that initially does not hold any
	 * instance.
	 */
	public static <T> Holder<T> absent() {
		return fromNullable(null);
	}

	/**
	 * Returns a new, modifiable holder that initially holds the instance {@code
	 * initialReference}.
	 */
	public static <T> Holder<T> of(T initialReference) {
		return fromNullable(checkNotNull(initialReference));
	}

	/**
	 * If {@code nullableReference} is non-null, returns a new {@code Holder}
	 * instance containing that reference; otherwise returns
	 * {@link Holder#absent}.
	 */
	public static <T> Holder<T> fromNullable(T nullableReference) {
		return new Holder<T>(nullableReference);
	}

	private T instance;

	private Holder(T initialReference) {
		this.instance = initialReference;
	}

	public boolean isPresent() {
		return instance != null;
	}

	public T get() {
		return instance;
	}

	/**
	 * Sets the contents of this holder to the given non-null reference.
	 */
	public void set(T instance) {
		setNullable(checkNotNull(instance));
	}

	/**
	 * Removes the reference from the holder if one exists.
	 */
	public void clear() {
		setNullable(null);
	}

	/**
	 * Sets the contents of this holder to the given instance if it is non-null;
	 * clears it otherwise.
	 */
	public void setNullable(T instance) {
		this.instance = instance;
	}

	@Override
	public String toString() {
		return isPresent() ? "Holder.of(" + instance + ")" : "Holder.absent()";
	}

	private static <T> T checkNotNull(T t) {
		if (t == null) {
			throw new NullPointerException();
		}
		return t;
	}
}