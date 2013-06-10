package com.barchart.util.flow.provider;

import static com.barchart.util.flow.provider.TestProvider.TestEvent.*;
import static com.barchart.util.flow.provider.TestProvider.TestState.*;
import static org.junit.Assert.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Context.Builder;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.State;

public class TestProvider {

	enum TestEvent implements Event<TestEvent> {
		EVENT_1, //
		EVENT_2, //
	}

	enum TestState implements State<TestState> {
		STATE_1, //
		STATE_2, //
		STATE_3, //
	}

	static class Target {

	}

	@Test
	public void flow() throws Exception {

		final Executor executor = Executors.newSingleThreadExecutor();

		final Flow.Builder<TestEvent, TestState, Target> builder = Provider
				.flowBuilder(TestEvent.class, TestState.class);
		assertNotNull(builder);

		builder.at(STATE_1).on(EVENT_1).to(STATE_2);
		builder.at(STATE_2).on(EVENT_2).to(STATE_3);

		builder.executor(executor);

		final Flow<TestEvent, TestState, Target> flow = builder.build();
		assertNotNull(flow);

		final Target market = new Target();

		final Builder<TestEvent, TestState, Target> contextBuilder = flow
				.contextBuilder();
		assertNotNull(contextBuilder);

		final Context<TestEvent, TestState, Target> context = contextBuilder
				.build(market);
		assertNotNull(context);

	}

}
