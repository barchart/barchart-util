package com.barchart.util.value.api;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Time value.
 * <p>
 * Represented by 2 components:
 * <li>milliseconds in the UTC time zone
 * <li>default time zone for this instant, UTC is default
 * <p>
 * Depending on implementation, periodic time zone update is required for accurate time zone interpretation.
 * <li>see <a href="http://joda-time.sourceforge.net/tz_update.html">Joda Time updater</a>
 * <li>see <a href= "http://www.oracle.com/technetwork/java/javase/tzupdater-readme-136440.html" >JDK updater</a>
 */
public interface Time extends Comparable<Time>, Existential {
	
	/**
	 * Number of milliseconds since January 1, 1970, 00:00:00 UTC.
	 */
	long millisecond();

	/**
	 * Default time zone for this instant, UTC default.
	 * <p>
	 * Compatible with both JDK and JodaTime.
	 * <p>
	 * See<a href="https://en.wikipedia.org/wiki/Tz_database">TZ Database</a>
	 * 
	 * @see TimeZone
	 */
	TimeZone zone();

	/**
	 * @return the java.util.Date representation of this instant in time.
	 */
	Date asDate();

	/**
	 * @param format
	 * @return a String representation of this instant in time in the time zone of the DateFormat.
	 */
	String format(DateFormat format);

	/**
	 * Ordering based on {@link Time} based on {@link #millisUTC()}.
	 */
	@Override
	int compareTo(Time thatTime);

	/**
	 * TODO ensure we can guarantee same hash as JDK 8 / JodaTime.
	 */
	@Override
	int hashCode();

	/**
	 * Equality of {@link Time} based on {@link #millisUTC()}.
	 */
	@Override
	boolean equals(Object thatTime);

	@Override
	boolean isNull();
	
	/** Special time value @see {isNull} */
	Time NULL = new Time() {
		
		@Override
		public long millisecond() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TimeZone zone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Date asDate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String format(DateFormat format) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Time thatTime) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
