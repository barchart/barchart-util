package com.barchart.util.value.api;

import java.util.TimeZone;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.NotMutable;

/**
 * Time value.
 * <p>
 * Represented by 2 components:
 * <li>milliseconds in the UTC time zone
 * <li>time zone in which to interpret time
 * <p>
 * Depending on implementation, periodic time zone update is required for
 * accurate time zone interpretation.
 * <li>see <a href="http://joda-time.sourceforge.net/tz_update.html">Joda Time
 * updater</a>
 * <li>see <a href=
 * "http://www.oracle.com/technetwork/java/javase/tzupdater-readme-136440.html"
 * >JDK updater</a>
 */
@NotMutable
@ProviderType
public interface Time extends Comparable<Time>, Existential {

	/** Special time value @see {isNull} */
	Time NULL = FactoryLoader.load().newTime(0, "");
	
	/**
	 * Time zone ID form the TZ database.
	 * <p>
	 * Compatible with both JDK and JodaTime.
	 * <p>
	 * The return value is guaranteed to be interned, see
	 * {@link String#intern()}, and can be used for "==" instance equality.
	 * <p>
	 * See<a href="https://en.wikipedia.org/wiki/Tz_database">TZ Database</a>
	 * 
	 * @see TimeZone
	 */
	String zone();

	/**
	 * Number of milliseconds since January 1, 1970, 00:00:00 UTC.
	 */
	long millisecond();

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
	 * <p>
	 * TODO should zone be part of equals?
	 */
	@Override
	boolean equals(Object thatTime);

	@Override
	boolean isNull();
	
}
