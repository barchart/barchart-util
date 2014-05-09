package com.barchart.util.rx.subjects;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.test.concurrent.TestObserver;

public class TestCurrentSubject {

	@Test
	public void testBefore() throws Exception {

		final CurrentSubject<Integer> s = CurrentSubject.create();

		final TestObserver<Integer> c = TestObserver.create();

		s.subscribe(c);

		assertFalse(c.completed);

		s.onNext(1);

		assertTrue(c.completed);
		assertEquals(1, c.results.size());
		assertEquals(1, (int) c.results.get(0));

	}

	@Test
	public void testDuring() throws Exception {

		final CurrentSubject<Integer> s = CurrentSubject.create();

		final TestObserver<Integer> c = TestObserver.create();

		s.onNext(1);
		s.onNext(2);

		s.subscribe(c);

		assertTrue(c.completed);
		assertEquals(1, c.results.size());
		assertEquals(2, (int) c.results.get(0));

	}

	@Test
	public void testAfter() throws Exception {

		final CurrentSubject<Integer> s = CurrentSubject.create();

		final TestObserver<Integer> c = TestObserver.create();

		s.onNext(1);
		s.onNext(2);
		s.onNext(3);
		s.onCompleted();

		s.subscribe(c);

		assertTrue(c.completed);
		assertEquals(1, c.results.size());
		assertEquals(3, (int) c.results.get(0));

	}

	@Test
	public void testCurrent() throws Exception {

		final CurrentSubject<Integer> s = CurrentSubject.create();

		assertNull(s.peek());
		s.onNext(1);
		assertEquals(1, (int) s.peek());
		s.onNext(2);
		assertEquals(2, (int) s.peek());
		s.onNext(3);
		assertEquals(3, (int) s.peek());
		s.onCompleted();
		assertEquals(3, (int) s.peek());

	}

}
