package com.barchart.util.guice.encryption;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class EncryptedAnno implements Encrypted, Serializable {

	private final String value;

	public static Encrypted named(final String name) {
		return new EncryptedAnno(name);
	}

	EncryptedAnno(final String value) {
		if (value == null) {
			throw new NullPointerException("name");
		}
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	@Override
	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Encrypted)) {
			return false;
		}

		final Encrypted other = (Encrypted) o;
		return value.equals(other.value());
	}

	@Override
	public String toString() {
		return "@" + Encrypted.class.getName() + "(value=" + value + ")";
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Encrypted.class;
	}

	private static final long serialVersionUID = 0;
}
