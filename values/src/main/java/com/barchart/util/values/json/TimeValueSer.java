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

import com.barchart.util.values.api.TimeValue;

class TimeValueSer extends ScalarSerializerBase<TimeValue> {

	protected TimeValueSer(final Class<TimeValue> klaz) {
		super(klaz);
	}

	static Logger log = LoggerFactory.getLogger(TimeValueSer.class);

	@Override
	public void serialize(final TimeValue value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {

		jgen.writeString(Long.toString(value.asMillisUTC()));

	}

}