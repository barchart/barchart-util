## barchart-util-concurrent

barchart-util-concurrent provides the basic building blocks for asynchronous programming in
Java. It has similar goals to Guava's ListenableFuture with a simpler API. It provides an
implementation of some Future<E> classes, with the ability to attach a callback listener that
will be notified as soon as a result is available.

Quick class overview:

* FutureCallback - Core Future API with result listener callback
* FutureListener - Listener API
* FutureNotifierBase - Implementation of FutureCallback that just serves as a proxy for
linking an executing task with its callbacks. Custom future notifiers should extend this class.
* FutureNotifier - Generic implementation of FutureNotifierBase.
* FutureCallbackTask - Subclass of FutureTask<E> class that implements FutureCallback<E>
* FutureCallbackExecutor - A ThreadPoolExecutor implementation that returns FutureCallbackTasks
for easy listener attachment
* FutureCallbackList - A convenience class that wraps a set of FutureCallbacks and fires a
single callback when all tasks have completed or failed

All classes in [com.barchart.util.concurrent](https://github.com/barchart/barchart-util/tree/master/barchart-util-concurrent/src/main/java/com/barchart/util/concurrent).

Usage example:

```java
public void init() {

     login(user, pass).addResultListener(new FutureListener<User> {
          public void resultAvailable(Future<User> future) {
               try {
                    User user = future.get();
                    System.out.println("Logged in as " + user.getUsername());
               } catch (ExecutionException ee) {
                    System.out.println("Login failed: " + ee.getCause());
               }
          }
     });

}

public FutureCallback<User, ?> login(final String user, final String pass) {

     final FutureCallback<User, ?> future = new FutureNotifier<User>();

     new Thread() {
          public void run() {
               // Load a user via a blocking call (i.e. database query)
               User user = loadFromDB(user, pass);
               if (user != null)
                    future.succeed(user);
               else
                    future.fail(new AuthenticationException("Username not found"));
          }
     }.start();

     return future;

}
```
