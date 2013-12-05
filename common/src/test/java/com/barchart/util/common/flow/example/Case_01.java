package com.barchart.util.common.flow.example;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.common.flow.api.Context;
import com.barchart.util.common.flow.api.Flow;
import com.barchart.util.common.flow.api.Listener;
import com.barchart.util.common.flow.api.Point;
import com.barchart.util.common.flow.provider.Provider;

public class Case_01 {

	static final Logger log = LoggerFactory.getLogger(Case_01.class);

	public static void main(final String... args) throws Exception {

		final Executor executor = Executors
				.newSingleThreadExecutor(new ThreadFactory("# pool"));

		final Listener<MarketEvent, MarketState, MarketEntity> listener = //
		new Listener.Adapter<MarketEvent, MarketState, MarketEntity>() {

			@Override
			public void enter(
					final Point<MarketEvent, MarketState> past,
					final Point<MarketEvent, MarketState> next,
					final Context<MarketEvent, MarketState, MarketEntity> context)
					throws Exception {
				log.info("Listener: Hello.");
			}

			@Override
			public void leave(
					final Point<MarketEvent, MarketState> past,
					final Point<MarketEvent, MarketState> next,
					final Context<MarketEvent, MarketState, MarketEntity> context)
					throws Exception {
				log.info("Listener: Adios.");
			}

		};

		final Flow.Builder<MarketEvent, MarketState, MarketEntity> flowBuilder = Provider
				.flowBuilder(MarketEvent.class, MarketState.class);

		flowBuilder.debug(true);
		flowBuilder.enforce(true);
		flowBuilder.locking(true);

		flowBuilder.executor(executor);
		flowBuilder.listener(listener);

		flowBuilder.at(MarketState.STATE_1).on(MarketEvent.EVENT_1).to(MarketState.STATE_2);
		flowBuilder.at(MarketState.STATE_2).on(MarketEvent.EVENT_2).to(MarketState.STATE_3);

		final Flow<MarketEvent, MarketState, MarketEntity> flow = flowBuilder
				.build();

		log.info("flow: \n{}", flow);

		final MarketEntity market = new MarketEntity();

		final Context.Builder<MarketEvent, MarketState, MarketEntity> contextBuilder = flow
				.contextBuilder();

		final Context<MarketEvent, MarketState, MarketEntity> context = contextBuilder
				.build(market);

		log.info("context {}", context);

		flow.fire(MarketEvent.EVENT_1, context);

		Thread.sleep(1 * 1000);

	}

}
