package com.agilogy.foundation.java.apiserver.api;

import com.agilogy.foundation.java.domain.TimeEntry;
import static com.agilogy.json.Field.field;
import com.agilogy.json.JsonCodec;
import static com.agilogy.json.JsonCodecBuilder.objectCodec;

public final class JsonCodecs {
  static JsonCodec<TimeEntry> timeEntry = objectCodec(
        TimeEntry::new,
        field("employeeId", TimeEntry::employeeId),
        field("projectId", TimeEntry::projectId),
        field("start", TimeEntry::start, JsonCodec.instantAsEpochMillis),
        field("end", TimeEntry::end, JsonCodec.instantAsEpochMillis)
  );

}
