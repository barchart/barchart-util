package com.barchart.util.value.api;

public interface Bool extends Comparable<Bool>, Existential {

	boolean value();

	@Override
	int compareTo(Bool that);

	@Override
	int hashCode();

	@Override
	boolean equals(Object that);

	@Override
	boolean isNull();
	
	/** Special time value @see {isNull} */
	Bool NULL = new Bool() {

		@Override
		public boolean value() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Bool that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
}
