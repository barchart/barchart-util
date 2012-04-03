/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.lang;

import static java.lang.Math.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueFreezer;

@NotMutable
public abstract class ScaledDecimalValue<T extends ScaledDecimal<T, F> & Value<T>, F extends ScaledDecimal<F, F> & Value<F>>
		extends ValueFreezer<T> implements ScaledDecimal<T, F> {

	protected abstract T result(long mantissa, int exponent);

	@Deprecated
	@Override
	public final double asDouble() {
		return mantissa() * Math.pow(10, exponent());
	}

	@Deprecated
	@Override
	public final float asFloat() {
		return (float) asDouble();
	}

	@Deprecated
	@Override
	public final long asLong() throws ArithmeticException {

		long m = mantissa();
		int e = exponent();

		while (e > 0) {
			m = MathExtra.longMult10(m);
			e--;
		}
		while (e < 0) {
			m /= 10;
			e++;
		}

		return m;

	}

	@Deprecated
	@Override
	public final int asInt() throws ArithmeticException {

		return MathExtra.castLongToInt(asLong());

	}

	// yes, same type
	@SuppressWarnings("unchecked")
	@Override
	public final T norm() {

		long m = mantissa();
		int e = exponent();

		final int p = e;

		while (m % 10 == 0) {
			m /= 10;
			e++;
		}

		if (p == e) {
			return (T) this;
		} else {
			return result(m, e);
		}

	}

	// yes, same type
	@SuppressWarnings("unchecked")
	@Override
	public final T scale(final int exponent) throws ArithmeticException {

		long m = mantissa();
		int e = exponent();

		if (e == exponent) {
			return (T) this;
		}

		while (e > exponent) {
			m = MathExtra.longMult10(m);
			e--;
		}
		while (e < exponent) {
			m /= 10;
			e++;
		}

		return result(m, e);

	}

	@Override
	public final int compareTo(final T that) {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		if (e1 != e2) {
			if ((m1 ^ m2) < 0) {
				// different sign
				return m1 >= 0 ? +1 : -1;
			} else {
				// identical sign
				final int e = max(e1, e2);
				while (e > e1) {
					m1 /= 10L;
					e1++;
				}
				while (e > e2) {
					m2 /= 10L;
					e2++;
				}
			}

		}

		return m1 < m2 ? -1 : (m1 == m2 ? 0 : +1);

	}

	@Override
	public final long count(final T that) throws ArithmeticException {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		if (e1 != e2) {
			final int e = Math.min(e1, e2);
			while (e < e1) {
				m1 = MathExtra.longMult10(m1);
				e1--;
			}
			while (e < e2) {
				m2 = MathExtra.longMult10(m2);
				e2--;
			}
		}

		// return safeCastToInt(m1 / m2);
		return (m1 / m2);

	}

	@Override
	public final int hashCode() {
		final long m = mantissa();
		final int e = exponent();
		return e ^ (int) (m ^ (m >>> 32));
	}

	@Override
	public String toString() {
		return String.format("Decimal > %9d %3d", // 
				mantissa(), exponent());
	}

	// static final long SIGN = 1L << Long.SIZE;

	@Override
	public final T add(final T that) throws ArithmeticException {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		if (e1 != e2) {
			final int e = min(e1, e2);
			while (e < e1) {
				m1 = MathExtra.longMult10(m1);
				e1--;
			}
			while (e < e2) {
				m2 = MathExtra.longMult10(m2);
				e2--;
			}
		}

		return result(MathExtra.longAdd(m1, m2), e1);

	}

	@Override
	public final T sub(final T that) throws ArithmeticException {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		if (e1 != e2) {
			final int e = min(e1, e2);
			while (e < e1) {
				m1 = MathExtra.longMult10(m1);
				e1--;
			}
			while (e < e2) {
				m2 = MathExtra.longMult10(m2);
				e2--;
			}
		}

		return result(MathExtra.longSub(m1, m2), e1);

	}

	@Override
	public final T mult(final long factor) throws ArithmeticException {

		return result(MathExtra.longMult(mantissa(), factor), exponent());

	}

	@Override
	public final T div(final long factor) throws ArithmeticException {

		long m = this.mantissa();
		int e = this.exponent();

		while (true) {
			long r = m * 10L;
			if (r / 10L != m) {
				break;
			}
			m = r;
			e--;
		}

		return result(m / factor, e);

	}

	@Override
	public final T neg() {

		return result(-mantissa(), exponent());

	}

	@Override
	public final boolean equalsScale(final T that) {
		return this.exponent() == that.exponent();
	}

	@Override
	public final boolean isZero() {
		return this.mantissa() == 0L;
	}

	@Override
	public final T mult(final F that) throws ArithmeticException {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		return result(MathExtra.longMult(m1, m2), e1 + e2);

	}

	@Override
	public final T div(final F that) throws ArithmeticException {

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		while (true) {
			long r = m1 * 10L;
			if (r / 10L != m1) {
				break;
			}
			m1 = r;
			e1--;
		}

		return result(m1 / m2, e1 - e2);

	}

	@Override
	public boolean equals(final Object thatValue) {
		throw new UnsupportedOperationException(
				"must override in sub type in order to narrow closure");
	}

}
