package com.barchart.util.flow.example;

import static com.barchart.util.flow.example.MarketEvent.*;
import static com.barchart.util.flow.example.MarketState.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Context.Builder;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.Transit;
import com.barchart.util.flow.provider.Provider;

public class Case_01 {

	static final Logger log = LoggerFactory.getLogger(Case_01.class);

	public static void main(final String... args) throws Exception {

		final Executor executor = Executors.newSingleThreadExecutor();

		final Listener<MarketEvent, MarketState, MarketEntity> listener = //
		new Listener.Adapter<MarketEvent, MarketState, MarketEntity>() {

			@Override
			public void enter(
					final Transit<MarketEvent, MarketState> transit,
					final Context<MarketEvent, MarketState, MarketEntity> context)
					throws Exception {
				log.info("Enter {} {}", transit, context);
			}

			@Override
			public void leave(
					final Transit<MarketEvent, MarketState> transit,
					final Context<MarketEvent, MarketState, MarketEntity> context)
					throws Exception {
				log.info("Leave {} {}", transit, context);
			}

		};

		final Flow.Builder<MarketEvent, MarketState, MarketEntity> flowBuilder = Provider
				.flowBuilder(MarketEvent.class, MarketState.class);

		flowBuilder.executor(executor);
		flowBuilder.listener(listener);

		flowBuilder.at(STATE_1).on(EVENT_1).to(STATE_2);
		flowBuilder.at(STATE_2).on(EVENT_2).to(STATE_3);

		final Flow<MarketEvent, MarketState, MarketEntity> flow = flowBuilder
				.build();

		final MarketEntity market = new MarketEntity();

		final Builder<MarketEvent, MarketState, MarketEntity> contextBuilder = flow
				.contextBuilder();

		final Context<MarketEvent, MarketState, MarketEntity> context = contextBuilder
				.build(market);

		log.info("context {}", context);

		flow.fire(EVENT_1, context);

	}

}
