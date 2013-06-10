package com.barchart.util.flow.provider;

import static com.barchart.util.flow.provider.TheEvent.*;
import static com.barchart.util.flow.provider.TheState.*;
import static org.junit.Assert.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Context.Builder;
import com.barchart.util.flow.api.Flow;

public class TestProvider {

	@Test
	public void flow() throws Exception {

		final Executor executor = Executors.newSingleThreadExecutor();

		final Flow.Builder<TheEvent, TheState, TheTarget> builder = Provider
				.flowBuilder(TheEvent.class, TheState.class);
		assertNotNull(builder);

		builder.at(STATE_1).on(EVENT_1).to(STATE_2);
		builder.at(STATE_2).on(EVENT_2).to(STATE_3);

		builder.executor(executor);

		final Flow<TheEvent, TheState, TheTarget> flow = builder.build();
		assertNotNull(flow);

		final TheTarget market = new TheTarget();

		final Builder<TheEvent, TheState, TheTarget> contextBuilder = flow
				.contextBuilder();
		assertNotNull(contextBuilder);

		final Context<TheEvent, TheState, TheTarget> context = contextBuilder
				.build(market);
		assertNotNull(context);

	}

}
