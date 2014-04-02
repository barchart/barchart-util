package com.barchart.util.value.api;


public interface TimeInterval extends Existential {

	Time start();

	Time stop();
	
	@Override
	boolean isNull();
	
	TimeInterval NULL = new TimeInterval() {

		@Override
		public Time start() {
			return Time.NULL;
		}

		@Override
		public Time stop() {
			return Time.NULL;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
