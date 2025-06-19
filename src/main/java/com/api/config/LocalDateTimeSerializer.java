package com.api.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
  private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

  public LocalDateTimeSerializer() {
    super(LocalDateTime.class);
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    ZonedDateTime vnTime = value.atZone(ZoneId.systemDefault()).withZoneSameInstant(VN_ZONE);
    gen.writeString(vnTime.toLocalDateTime().toString());
  }
}
