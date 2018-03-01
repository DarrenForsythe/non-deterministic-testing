# Non Deterministic Testing #

Spring comes with some super helpful interfaces such as `IdGenerator`. This can easily help to make your testing less brittle.

## IdGenerator ##

This is a helpful interface, and incredibly basic that returns a `UUID`

```java
package org.springframework.util;

import java.util.UUID;

/**
 * Contract for generating universally unique identifiers {@link UUID (UUIDs)}.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public interface IdGenerator {

	/**
	 * Generate a new identifier.
	 * @return the generated identifier
	 */
	UUID generateId();

}
```

In addition, Spring gives you the a few concrete implementations, the most commonly used would be the `JdkIdGenerator`

```java

/**
 * An {@link IdGenerator} that calls {@link java.util.UUID#randomUUID()}.
 *
 * @author Rossen Stoyanchev
 * @since 4.1.5
 */
public class JdkIdGenerator implements IdGenerator {

	@Override
	public UUID generateId() {
		return UUID.randomUUID();
	}

}
```

If we program to the interface we can easily implement a different version for test time that will always return the same value allowing our tests to be non brittle in the case of large integration or end to end tests that we know what the value should be, that it's not just a `UUID`.

### Implementation ### 


```java
	@Bean
	public IdGenerator idGenerator() {
		return new JdkIdGenerator();
	}
```

Testing would use our own implementation to be injected. 

```java
		@Bean
		@Primary
		public IdGenerator idGenerator() {
			return () -> UUID.nameUUIDFromBytes("IALwaysReturnTheSame".getBytes());
		}
```


## CurrentTime ##

Times are hateful in Java. No matter where you are using them you shoudl always wrap up the generation of them. `CurrentTimeGenerator` applies the exact same ideas to creating an `Instant` but could be done for any type e.g. `long` or `LocalDateTime` etc. etc.


### Implementation ###

```java
@FunctionalInterface
public interface CurrentTimeGenerator {

	long currentTime();

}
```

Simple interface that returns a long, the same return type of `System.currentTImeMillis`.


```java

	@Bean
	public CurrentTimeGenerator currentTimeGenerator() {
		return () -> System.currentTimeMillis();
	}
```

and for testing we just change the implemetnation to return the Unix Epoch.

```java

		@Bean
		@Primary
		public CurrentTimeGenerator currentTimeGenerator() {
			return () -> Instant.EPOCH.toEpochMilli();
		}
```

## Tests ##

The two tests verify that the non deterministic approach can never be truly verified only that the instant is `after` different calls, and the `UUID` is never equal.

The deterministic approach verifies that the calls are always the same, and that we can create a new `UUID` and `Instant` that is equal to our calls allowing easier verification.

