package com.barchart.util.flow.provider;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.State;
import com.barchart.util.flow.provider.Provider;

public class TestProvider {

	enum E implements Event<E> {
		INITIAL, //
		EVENT_2, //
		TERMINAL, //
	}

	enum S implements State<S> {
		CREATED, //
		STATE_2, //
		TERMINATED, //
	}

	static class T {
	}

	static final Logger log = LoggerFactory.getLogger(TestProvider.class);

	@Test
	public void flow() throws Exception {

		final Flow.Builder<E, S, T> builder = Provider.flowBuilder(E.class,
				S.class);

		builder.enforce(true);

		builder.at(S.CREATED).on(E.INITIAL).to(S.STATE_2);
		builder.at(S.STATE_2).on(E.EVENT_2).to(S.TERMINATED);

		final Flow<E, S, T> flow = builder.build();
		assertNotNull(flow);

		final T market = new T();

		final Context.Builder<E, S, T> contextBuilder = flow.contextBuilder();
		assertNotNull(contextBuilder);

		final Context<E, S, T> context = contextBuilder.build(market);
		assertNotNull(context);

	}

}
