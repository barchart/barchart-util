
    Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>

    All rights reserved. Licensed under the OSI BSD License.

    http://www.opensource.org/licenses/bsd-license.php

-->
## info

barchart utilities projects - functional java

Monad implementation of the Null Pattern in Java to replicate capabilities of languages like Haskell in processing monads

The null ptr is sometimes described as "the billion dollar mistake" for that matter, and this framework eliminates that issue

This is what I empirically learned while refactoring the whole of the hazelcast session server into a close functional paradigm:

- Monad getters can be thought of as private methods although we are in a pure functional domain now.

- Monad setters are execution flow end points generally setting a result to be dispatched at the end of processing.

- Monad setters can be included inside a class placeholder to hold the results.

A typical example for a setter is:

```
	private class ResultHolder {

		private SessionResult result;

		public SessionResult getResult() {
			return result;
		}

		public void setResult(final SessionResult result) {
			this.result = result;
		}

		ResultHolder(final SessionResult result) {
			this.result = result;
		}

		final MonadicSetter<ResultHolder, Session> setResult =
				new MonadicSetter<ResultHolder, Session>(this) {

					@Override
					protected void set(final ResultHolder state,
							final Session session) {

						state.result = new SessionResult(session);
					}
				};
	}
```

A typical example for a getter is:

```
	private final MonadicGetter<String, byte[]> sessionIdToBytes =
			new MonadicGetter<String, byte[]>() {

				@Override
				protected byte[] get(final String sessionId) {
					return encodeUTF8(sessionId);
				}
			};
```

The processing is either accomplished 100% through monads or not. A practical complex example used in the hz server implementation:

```
	@Override
	public SessionFuture claim(final User user, final String group) {

		// Init the result placeholder

		final ResultHolder state =
				new ResultHolder(new SessionResult(Status.INVALID));

		// This setter will accomplish a notification and be used as end point

		final MonadicSetter<ResultHolder, Session> notifyOfLockout =
				new MonadicSetter<ResultHolder, Session>(state) {

					@Override
					protected void set(final ResultHolder state,
							final Session session) {

						session.setStatus(Status.LOCKOUT);
						state.setResult(new SessionResult(session));
					}
				};

		try {

			// Init the parameter class and use the monads to set its state with info from outside

			final UserGroup ug = new UserGroup();

			getUserObject.apply(user).bind(ug.setUser);
			getStringObject.apply(group).bind(ug.setGroup);

			userGroupToKey.apply(ug)                                // Grabs the param object and modify it to a key
					.bind(userGroupKeyToSessionKey)					// Modify that key to another key
					.bind(bytesToSession)							// Grab that key and give us a session object
					.bind(removeSession)							// Remove that session object
					.bind(notifyOfLockout);							// Notify of a lockout and end point of the processing

		// Useless but the rules of Java force us to have a try/catch block here
		} catch (final Failure e) {
		}

		// Following this just a creation of a new session
		return create(user, group);
	}
```

### repo

maven central has
[latest version of these artifacts]
(http://search.maven.org/#search%7Cga%7C1%7Ccom.barchart.util)
