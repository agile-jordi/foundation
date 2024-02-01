package com.agilogy.foundation.java.apiserver.api;

import com.agilogy.foundation.java.domain.TimeEntry;
import com.agilogy.json.JsonCodecTestHelper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class TimeEntryJsonCodecTest {

  private final Instant t1 = Instant.now().truncatedTo(ChronoUnit.MILLIS);
  private final Instant t0 = t1.minusSeconds(3600 * 5);

  @Test
  public void testCodec() {
    JsonCodecTestHelper.testCodec(new TimeEntry("Jordi", "Foundation", t0, t1), JsonCodecs.timeEntry);
  }
}