package com.barchart.util.flow.provider;

import static com.barchart.util.flow.provider.TestEvent.*;
import static com.barchart.util.flow.provider.TestState.*;
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

		final Flow.Builder<TestEvent, TestState, TargetEntity> builder = Provider
				.flowBuilder(TestEvent.class, TestState.class);
		assertNotNull(builder);

		builder.at(STATE_1).on(EVENT_1).to(STATE_2);
		builder.at(STATE_2).on(EVENT_2).to(STATE_3);

		builder.executor(executor);

		final Flow<TestEvent, TestState, TargetEntity> flow = builder.build();
		assertNotNull(flow);

		final TargetEntity market = new TargetEntity();

		final Builder<TestEvent, TestState, TargetEntity> contextBuilder = flow
				.contextBuilder();
		assertNotNull(contextBuilder);

		final Context<TestEvent, TestState, TargetEntity> context = contextBuilder
				.build(market);
		assertNotNull(context);

	}

}
