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
			return false;
		}

		@Override
		public int compareTo(Bool that) {
			return that.isNull() ? 0 : -1;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
}
