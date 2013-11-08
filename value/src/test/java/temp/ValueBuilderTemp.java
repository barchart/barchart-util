package temp;

import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.impl.DefFraction;
import com.barchart.util.value.impl.NulFraction;

public class ValueBuilderTemp {

	public static Fraction newFraction(final int base, final int exponent) {
		return new DefFraction(base, exponent);
	}

	public static final Fraction NULL_FRACTION = //
	new NulFraction();

}
