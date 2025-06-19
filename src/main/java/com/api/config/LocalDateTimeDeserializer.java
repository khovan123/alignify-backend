package com.api.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
  private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

  public LocalDateTimeDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String str = p.getText();
    LocalDateTime ldt = LocalDateTime.parse(str);
    ZonedDateTime vnTime = ldt.atZone(VN_ZONE);
    return vnTime.toLocalDateTime();
  }
}
