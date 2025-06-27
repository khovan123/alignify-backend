package com.api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ReadingConverter
public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
  @Override
  public LocalDateTime convert(Date source) {
    return source.toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
  }
}
