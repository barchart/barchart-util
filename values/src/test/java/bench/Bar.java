package bench;

import com.barchart.util.values.api.PriceValue;

/**
 * Market bar with price values as array wrappers.
 */
public class Bar {

	private static final int COUNT = 4;

	private final long[] mantissa = new long[COUNT];
	private final byte[] exponent = new byte[COUNT];

	/** wrapper of bar with offset 0 */
	public PriceValue open() {
		return null;
	}

	/** wrapper of bar with offset 1 */
	public PriceValue high() {
		return null;
	}

	/** wrapper of bar with offset 2 */
	public PriceValue low() {
		return null;
	}

	/** wrapper of bar with offset 3 */
	public PriceValue close() {
		return null;
	}

}
