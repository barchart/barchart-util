package com.barchart.util.flow.example;

import static com.barchart.util.flow.example.Case_01.MarketEvent.*;
import static com.barchart.util.flow.example.Case_01.MarketState.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Context.Builder;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.State;
import com.barchart.util.flow.api.Transit;
import com.barchart.util.flow.provider.Provider;

public class Case_01 {

	static final Logger log = LoggerFactory.getLogger(Case_01.class);

	enum MarketEvent implements Event<MarketEvent> {
		EVENT_1, //
		EVENT_2, //
	}

	enum MarketState implements State<MarketState> {
		STATE_1, //
		STATE_2, //
		STATE_3, //
	}

	static class Market {
	}

	public static void main(final String... args) throws Exception {

		final Executor executor = Executors.newSingleThreadExecutor();

		final Listener<MarketEvent, MarketState, Market> listener = //
		new Listener.Adapter<MarketEvent, MarketState, Market>() {
			@Override
			public void enter(final Transit<MarketEvent, MarketState> transit,
					final Context<MarketEvent, MarketState, Market> context)
					throws Exception {
				log.info("Enter {}", transit, context);
			}

			@Override
			public void leave(final Transit<MarketEvent, MarketState> transit,
					final Context<MarketEvent, MarketState, Market> context)
					throws Exception {
				log.info("Leave {}", transit, context);
			}
		};

		final Flow.Builder<MarketEvent, MarketState, Market> builder = Provider
				.flowBuilder(MarketEvent.class, MarketState.class);

		builder.executor(executor);
		builder.listener(listener);

		builder.at(STATE_1).on(EVENT_1).to(STATE_2);
		builder.at(STATE_2).on(EVENT_2).to(STATE_3);

		final Flow<MarketEvent, MarketState, Market> flow = builder.build();

		final Market market = new Market();

		final Builder<MarketEvent, MarketState, Market> contextBuilder = flow
				.contextBuilder();

		final Context<MarketEvent, MarketState, Market> context = contextBuilder
				.build(market);

		log.info("context {}", context);

		flow.fire(EVENT_1, context);

	}

}
