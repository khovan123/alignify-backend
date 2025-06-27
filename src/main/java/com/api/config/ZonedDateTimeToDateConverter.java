package com.api.config;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
  @Override
  public Date convert(ZonedDateTime source) {
    return Date.from(source.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
  }
}
