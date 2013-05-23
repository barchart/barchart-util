/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.value.api.Scaled;

class ScaledValueSer<T extends Scaled<T, F>, F extends Scaled<F, ?>>
		extends ScalarSerializerBase<T> {

	protected ScaledValueSer(final Class<T> klaz) {
		super(klaz);
	}

	static Logger log = LoggerFactory.getLogger(ScaledValueSer.class);

	@Override
	public void serialize(final T value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {

		final StringBuilder text = new StringBuilder(64);
		text.append(value.mantissa());
		text.append(ValueModule.EXP);
		text.append(value.exponent());
		jgen.writeString(text.toString());

	}

}
