package com.barchart.util.flow.provider;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;

import org.junit.Test;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow.Builder;
import com.barchart.util.flow.api.State;

public class TestFlowBean {

	enum E implements Event<E> {
		INITIAL, //
		EVENT_2, //
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
		STATE_2, //
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
	public void builderParams() throws Exception {

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

			builder.initial(E.EVENT_2);
			builder.initial(S.STATE_2);

			builder.terminal(E.TERMINAL);
			builder.terminal(S.TERMINATED);

			final FlowBean<E, S, T> flow = (FlowBean<E, S, T>) builder.build();

			assertEquals(true, flow.isDebug);
			assertEquals(true, flow.isEnforce);
			assertEquals(true, flow.isLocking);

			assertEquals(E.class, flow.eventClass);
			assertEquals(S.class, flow.stateClass);

			assertNotNull(flow.executor);

			assertEquals(E.EVENT_2, flow.initialEvent);
			assertEquals(S.STATE_2, flow.initialState);

			assertEquals(E.TERMINAL, flow.terminalEvent);
			assertEquals(S.TERMINATED, flow.terminalState);

		}

	}

}
