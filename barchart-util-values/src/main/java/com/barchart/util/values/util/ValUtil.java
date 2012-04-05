/**
 * 
 */
package com.barchart.util.values.util;

import com.barchart.util.math.MathExtra;
import com.barchart.util.values.lang.ScaledDecimal;

/**
 * Utility class for deriving primitive representations of Value objects.
 * 
 * @author g-litchfield
 * 
 */
public class ValUtil {

	private ValUtil() {

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static long asLong(final ScaledDecimal<?, ?> sd)
			throws ArithmeticException {
		long m = sd.mantissa();
		int e = sd.exponent();

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

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static int asInt(final ScaledDecimal<?, ?> sd)
			throws ArithmeticException {

		return MathExtra.castLongToInt(asLong(sd));

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static double asDouble(final ScaledDecimal<?, ?> sd)
			throws ArithmeticException {

		return sd.mantissa() * Math.pow(10, sd.exponent());

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static float asFloat(final ScaledDecimal<?, ?> sd)
			throws ArithmeticException {

		return (float) asDouble(sd);

	}

}
