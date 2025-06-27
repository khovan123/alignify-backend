package com.api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Date;

@ReadingConverter
public class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
  @Override
  public ZonedDateTime convert(Date source) {
    return source.toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
  }
}
