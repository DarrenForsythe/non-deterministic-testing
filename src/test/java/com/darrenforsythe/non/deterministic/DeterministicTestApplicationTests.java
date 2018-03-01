package com.darrenforsythe.non.deterministic;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeterministicTestApplicationTests {

	@Autowired
	private HardToTestService hardToTestService;

	private Instant startTime;

	@Before
	public void setup() throws Exception {
		startTime = hardToTestService.getInstant();
		Thread.sleep(100L);
	}

	@Test
	public void assertDifferentVariables() {
		assertThat(hardToTestService.getUUID()).isNotEqualByComparingTo(hardToTestService.getUUID());
		assertThat(startTime).isLessThan(hardToTestService.getInstant());
	}

}
