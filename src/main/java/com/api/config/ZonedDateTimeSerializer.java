package com.api.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {
  public ZonedDateTimeSerializer() {
    super(ZonedDateTime.class);
  }

  @Override
  public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    // Định dạng ISO chuẩn, có offset
    gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
  }
}