/** */
package com.darrenforsythe.non.deterministic;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

@Service
public class HardToTestService {

    private final CurrentTimeGenerator timeGenerator;
    private final IdGenerator idGenerator;

    public HardToTestService(CurrentTimeGenerator timeGenerator, IdGenerator idGenerator) {
        this.timeGenerator = timeGenerator;
        this.idGenerator = idGenerator;
    }

    public UUID getUUID() {
        return idGenerator.generateId();
    }

    public Instant getInstant() {
        return Instant.ofEpochMilli(timeGenerator.currentTime());
    }
}
