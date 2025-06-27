package com.api.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
  public ZonedDateTimeDeserializer() {
    super(ZonedDateTime.class);
  }

  @Override
  public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String str = p.getText();
    return ZonedDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}