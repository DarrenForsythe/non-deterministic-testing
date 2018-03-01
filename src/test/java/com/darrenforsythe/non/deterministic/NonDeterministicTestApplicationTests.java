package com.darrenforsythe.non.deterministic;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.IdGenerator;

import com.darrenforsythe.non.deterministic.NonDeterministicTestApplicationTests.DeterministricConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeterministricConfiguration.class)
public class NonDeterministicTestApplicationTests {

	@Autowired
	private HardToTestService hardToTestService;

	private Instant startTime;

	@Before
	public void setup() throws Exception {
		startTime = hardToTestService.getInstant();
	}

	@Test
	public void assertDifferentVariables() {
		assertThat(hardToTestService.getUUID()).isEqualByComparingTo(hardToTestService.getUUID())
				.isEqualByComparingTo(UUID.nameUUIDFromBytes("IALwaysReturnTheSame".getBytes()));
		assertThat(startTime).isEqualByComparingTo(hardToTestService.getInstant()).isEqualByComparingTo(Instant.EPOCH);
	}

	@Configuration
	static class DeterministricConfiguration {
		@Bean
		@Primary
		public IdGenerator idGenerator() {
			return () -> UUID.nameUUIDFromBytes("IALwaysReturnTheSame".getBytes());
		}

		@Bean
		@Primary
		public CurrentTimeGenerator currentTimeGenerator() {
			return () -> Instant.EPOCH.toEpochMilli();
		}

		@Bean
		public HardToTestService hardToTestService(CurrentTimeGenerator timeGenerator, IdGenerator idGenerator) {
			return new HardToTestService(timeGenerator, idGenerator);
		}
	}

}
