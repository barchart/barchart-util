/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import static java.lang.Math.min;

import java.math.BigDecimal;

import com.barchart.util.value.api.Scaled;

public abstract class BaseScaled<T extends Scaled<T>> implements Scaled<T> {

	protected abstract T result(long mantissa, int exponent);

	// yes, same type
	@SuppressWarnings("unchecked")
	@Override
	public final T norm() {

		long m = mantissa();

		if(m == 0) {
			return (T) this;
		}

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
	public final T round(final int maxSigDigits) {

		if(maxSigDigits < 0) {
			throw new IllegalArgumentException("Max significant digits must be positive");
		}

		long m = mantissa();
		int e = exponent();

		/* If rounding is needed */
		if(e < 0 && e < (maxSigDigits * -1)) {

			int round = ((maxSigDigits * -1) - e) - 1;

			// long max is 19 digit
			if (round >= 19) {
				int i = -(19 + maxSigDigits + e);
				m = m / (int) Math.pow(10, i);
				e = e + i;
				round = ((maxSigDigits * -1) - e) - 1;
			}

			/* Truncate unused decimals */
			long powDif = 1;
			for (int i = 0; i < round; i++) {
				powDif = MathExtra.longMult10(powDif);
			}
			m /= powDif;

			/* Store 1's digit, determines rounding */
			long rnd = m - ((m / 10) * 10);

			/* Truncate last decimal */
			m /= 10;

			/* Increment if rounding up */
			if(rnd >= 5) {
				m++;
			}

			e = maxSigDigits * -1;
		}

		return result(m, e);

	}

	@Override
	public final int compareTo(final T that) {

		// TODO How do we handle NULL values?

		int e1 = this.exponent();
		int e2 = that.exponent();

		long m1 = this.mantissa();
		long m2 = that.mantissa();

		if (e1 != e2) {
			if ((m1 ^ m2) < 0) { // different sign
				return m1 >= 0 ? +1 : -1;
			} else { // identical sign

				while(e1 > 0) {
					e1--;
					m1 = MathExtra.longMult10(m1);
				}

				while(e1 < 0) {
					e1++;
					m2 = MathExtra.longMult10(m2);
				}

				while(e2 > 0) {
					e2--;
					m2 = MathExtra.longMult10(m2);
				}

				while(e2 < 0) {
					e2++;
					m1 = MathExtra.longMult10(m1);
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
		final int e = exponent();
		final double d = mantissa() * Math.pow(10.0, e);

		final int numberOfDecimalPlaces = e < 0 ? -e : 0;
		switch (numberOfDecimalPlaces) {
		case 0:
			return String.format("%.0f", d);
		case 1:
			return String.format("%.1f", d);
		case 2:
			return String.format("%.2f", d);
		case 3:
			return String.format("%.3f", d);
		case 4:
			return String.format("%.4f", d);
		case 5:
			return String.format("%.5f", d);
		default:
			return String.format("%.6f", d);
		}

	}

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

		if(factor == 0) {
			throw new ArithmeticException("Division by zero");
		}

		long m = this.mantissa();
		int e = this.exponent();

		if(m == 0) {
			return result(m, e);
		}

		while (true) {
			final long r = m * 10L;
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
	public final T abs() {
		return result(Math.abs(mantissa()), exponent());
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
	public final T mult(final Scaled<?> that) throws ArithmeticException {

		final int e1 = this.exponent();
		final int e2 = that.exponent();

		final long m1 = this.mantissa();
		final long m2 = that.mantissa();

		return result(MathExtra.longMult(m1, m2), e1 + e2);

	}

	@Override
	public final T mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
		return mult(factor).round(maxSigDigits);
	}

	@Override
	public final T div(final Scaled<?> that) throws ArithmeticException {

		int e1 = this.exponent();
		final int e2 = that.exponent();

		long m1 = this.mantissa();
		final long m2 = that.mantissa();

		if(m1 == 0) {
			return result(m1, e1);
		}

		while (true) {
			final long r = m1 * 10L;
			if (r / 10L != m1) {
				break;
			}
			m1 = r;
			e1--;
		}

		return result(m1 / m2, e1 - e2);

	}

	@Override
	public T div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
		return div(factor).round(maxSigDigits);
	}

	@Override
	public boolean equals(final Object thatValue) {
		throw new UnsupportedOperationException(
				"must override in sub type in order to narrow closure");
	}

	@Override
	public T add(long increment) {

		int exO = exponent();
		int ex = exponent();
		long ma = mantissa();

		if(ex == 0) {
			return result(MathExtra.longAdd(mantissa(), increment), ex);
		} else if(ex > 0) {

			while(ex > 0) {
				ma = MathExtra.longMult10(ma);
				ex--;
			}

			return result(MathExtra.longAdd(ma, increment), 0);

		} else {

			while(ex < 0) {
				increment = MathExtra.longMult10(increment);
				ex++;
			}

			return result(MathExtra.longAdd(ma, increment), exO);

		}

	}

	@Override
	public T sub(long decrement) {

		int exO = exponent();
		int ex = exponent();
		long ma = mantissa();

		if(ex == 0) {
			return result(MathExtra.longSub(mantissa(), decrement), ex);
		} else if(ex > 0) {

			while(ex > 0) {
				ma = MathExtra.longMult10(ma);
				ex--;
			}

			return result(MathExtra.longSub(ma, decrement), ex);

		} else {

			while(ex < 0) {
				decrement = MathExtra.longMult10(decrement);
				ex++;
			}

			return result(MathExtra.longSub(ma, decrement), exO);

		}
	}

	@Override
	public boolean greaterThan(final T that) {
		return this.compareTo(that) > 0;
	}

	@Override
	public boolean greaterThanOrEquals(final T that) {
		return this.compareTo(that) >= 0;
	}

	@Override
	public boolean lessThan(final T that) {
		return this.compareTo(that) < 0;
	}

	@Override
	public boolean lessThanOrEquals(final T that) {
		return this.compareTo(that) <= 0;
	}

	@Override
	public double asDouble() {

		final double result = new BigDecimal(mantissa())
				.movePointRight(exponent())
				.doubleValue();

		if(Double.isInfinite(result)) {
			throw new ArithmeticException("Overflow exception");
		}

		return result;
	}

}
