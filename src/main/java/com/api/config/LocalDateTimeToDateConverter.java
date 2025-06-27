package com.api.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
  @Override
  public Date convert(LocalDateTime source) {
    return Date.from(source.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
  }
}
