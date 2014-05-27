package com.barchart.util.value.api;


public interface TimeInterval extends Existential {

	Time start();

	Time stop();
	
	@Override
	boolean isNull();
	
	TimeInterval NULL = new TimeInterval() {

		@Override
		public Time start() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time stop() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
