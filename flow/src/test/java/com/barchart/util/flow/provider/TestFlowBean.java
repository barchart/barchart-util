package com.barchart.util.flow.provider;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.Flow.Builder;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.Point;
import com.barchart.util.flow.api.State;

public class TestFlowBean {

	enum E implements Event<E> {
		INITIAL, //
		EVENT_1, //
		EVENT_2, //
		EVENT_3, //
		TERMINAL, //
	}

	enum E_empty implements Event<E_empty> {
	}

	@SuppressWarnings("rawtypes")
	static class E_wrong implements Event {
		@Override
		public String name() {
			return null;
		}

		@Override
		public int ordinal() {
			return 0;
		}
	}

	enum S implements State<S> {
		CREATED, //
		STATE_1, //
		STATE_2, //
		STATE_3, //
		TERMINATED, //
	}

	enum S_empty implements State<S_empty> {
	}

	@SuppressWarnings("rawtypes")
	static class S_wrong implements State {
		@Override
		public String name() {
			return null;
		}

		@Override
		public int ordinal() {
			return 0;
		}
	}

	static class T {
	}

	static final PointMatrix<E, S> M = new PointMatrix<E, S>(E.class, S.class);

	static final Logger log = LoggerFactory.getLogger(TestFlowBean.class);

	Flow<E, S, T> flow1(final Listener<E, S, T> listener) {

		final Builder<E, S, T> builder = FlowBean.newBuilder(E.class, S.class);

		builder.debug(true);
		builder.enforce(true);

		builder.listener(listener);

		builder.terminal(E.TERMINAL);
		builder.terminal(S.TERMINATED);

		builder.at(S.CREATED).//
				on(E.EVENT_1).to(S.STATE_1).//
				on(E.EVENT_2).to(S.STATE_2);

		builder.at(S.STATE_1).//
				on(E.EVENT_1).to(S.STATE_1).//
				on(E.EVENT_2).to(S.STATE_2).//
				on(E.EVENT_3).to(S.TERMINATED);

		builder.at(S.STATE_2).//
				on(E.EVENT_3).to(S.STATE_1).//
				on(E.EVENT_2).to(S.STATE_2);

		final Flow<E, S, T> flow = builder.build();

		log.info("{}", flow);

		return flow;

	}

	@Test(expected = IllegalStateException.class)
	public void flow1_UnexpectedEvent() throws Exception {
		final Flow<E, S, T> flow = flow1(null);
		final Context<E, S, T> context = flow.contextBuilder().build(new T());
		flow.fire(E.INITIAL, context);
	}

	@Test()
	public void flow1_Transitions() throws Exception {

		final Wrapper<Point<E, S>> pastWrap = new Wrapper<Point<E, S>>();
		final Wrapper<Point<E, S>> nextWrap = new Wrapper<Point<E, S>>();

		final Listener<E, S, T> listener = new Listener.Adapter<E, S, T>() {
			@Override
			public void enter(final Point<E, S> past, final Point<E, S> next,
					final Context<E, S, T> context) throws Exception {
				pastWrap.value(past);
				nextWrap.value(next);
			}

			@Override
			public void leave(final Point<E, S> past, final Point<E, S> next,
					final Context<E, S, T> context) throws Exception {
				pastWrap.value(past);
				nextWrap.value(next);
			}
		};

		final Flow<E, S, T> flow = flow1(listener);
		final Context<E, S, T> context = flow.contextBuilder().build(new T());

		flow.fire(E.EVENT_1, context);
		assertEquals(M.point(E.INITIAL, S.CREATED), pastWrap.value());
		assertEquals(M.point(E.EVENT_1, S.STATE_1), nextWrap.value());

		flow.fire(E.EVENT_1, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_1, S.STATE_1), nextWrap.value());

		flow.fire(E.EVENT_1, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_1, S.STATE_1), nextWrap.value());

		flow.fire(E.EVENT_2, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_2, S.STATE_2), nextWrap.value());

		flow.fire(E.EVENT_3, context);
		assertEquals(M.point(E.EVENT_2, S.STATE_2), pastWrap.value());
		assertEquals(M.point(E.EVENT_3, S.STATE_1), nextWrap.value());

		flow.fire(E.EVENT_1, context);
		assertEquals(M.point(E.EVENT_3, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_1, S.STATE_1), nextWrap.value());

		flow.fire(E.EVENT_3, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_3, S.TERMINATED), nextWrap.value());

		/** Ignore all after terminate. */
		flow.fire(E.EVENT_1, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_3, S.TERMINATED), nextWrap.value());
		flow.fire(E.EVENT_2, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_3, S.TERMINATED), nextWrap.value());
		flow.fire(E.EVENT_3, context);
		assertEquals(M.point(E.EVENT_1, S.STATE_1), pastWrap.value());
		assertEquals(M.point(E.EVENT_3, S.TERMINATED), nextWrap.value());

	}

	@Test(expected = IllegalStateException.class)
	public void noEmptyEvents() throws Exception {
		FlowBean.newBuilder(E_empty.class, S.class);
	}

	@Test(expected = IllegalStateException.class)
	public void noEmptyStates() throws Exception {
		FlowBean.newBuilder(E.class, S_empty.class);
	}

	@Test(expected = IllegalStateException.class)
	public void noWrongEvents() throws Exception {
		FlowBean.newBuilder(E_wrong.class, S.class);
	}

	@Test(expected = IllegalStateException.class)
	public void noWrongStates() throws Exception {
		FlowBean.newBuilder(E.class, S_wrong.class);
	}

	@Test(expected = IllegalStateException.class)
	public void inconsistentTerminalEventState() throws Exception {
		final Builder<E, S, Object> builder = FlowBean.newBuilder(E.class,
				S.class);
		builder.terminal(E.TERMINAL);
		builder.build();
	}

	@Test()
	public void matrix() throws Exception {
		final Builder<E, S, T> builder = FlowBean.newBuilder(E.class, S.class);
		final FlowBean<E, S, T> flow = (FlowBean<E, S, T>) builder.build();
		assertNotNull(flow.matrix);
		assertEquals(Util.enumValues(E.class).length
				* Util.enumValues(S.class).length, flow.matrix.size());
	}

	@Test()
	public void ensureParams() throws Exception {

		final Builder<E, S, T> builder = FlowBean.newBuilder(E.class, S.class);

		{

			final FlowBean<E, S, T> flow = (FlowBean<E, S, T>) builder.build();

			assertEquals(false, flow.isDebug);
			assertEquals(false, flow.isEnforce);
			assertEquals(false, flow.isLocking);

			assertEquals(E.class, flow.eventClass);
			assertEquals(S.class, flow.stateClass);

			assertNull(flow.executor);

			assertEquals(E.INITIAL, flow.initialEvent);
			assertEquals(S.CREATED, flow.initialState);

			assertNull(flow.terminalEvent);
			assertNull(flow.terminalState);

		}

		{

			builder.debug(true);
			builder.enforce(true);
			builder.locking(true);

			builder.executor(Executors.newSingleThreadExecutor());

			builder.initial(E.EVENT_1);
			builder.initial(S.STATE_1);

			builder.terminal(E.TERMINAL);
			builder.terminal(S.TERMINATED);

			final FlowBean<E, S, T> flow = (FlowBean<E, S, T>) builder.build();

			assertEquals(true, flow.isDebug);
			assertEquals(true, flow.isEnforce);
			assertEquals(true, flow.isLocking);

			assertEquals(E.class, flow.eventClass);
			assertEquals(S.class, flow.stateClass);

			assertNotNull(flow.executor);

			assertEquals(E.EVENT_1, flow.initialEvent);
			assertEquals(S.STATE_1, flow.initialState);

			assertEquals(E.TERMINAL, flow.terminalEvent);
			assertEquals(S.TERMINATED, flow.terminalState);

		}

	}

}
