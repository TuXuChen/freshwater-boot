package org.freshwater.boot.common.utils;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

/**
 * 时间工具类
 * @author tuxuchen
 * @date 2022/8/29 10:21
 */
public class DateUtils {

  private DateUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 时间区间类型
   */
  public enum RangeType {
    DAY, WEEK, MONTH, QUARTER, YEAR
  }

  /**
   * 获取时间的开始时间
   * @param date
   * @param type
   * @return
   */
  public static LocalDateTime getStartTime(LocalDate date, RangeType type) {
    if(date == null) {
      return null;
    }
    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.now());
    return getStartTime(dateTime, type);
  }

  /**
   * 获取时间的开始时间
   * @param time
   * @param type
   * @return
   */
  public static LocalDateTime getStartTime(LocalDateTime time, RangeType type) {
    if(time == null) {
      return null;
    }
    Validate.notNull(type, "时间区间类型不能为空");
    LocalDateTime dateTime = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 0, 0);
    switch (type) {
      case DAY:
        break;
      case WEEK:
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, 1);
        break;
      case MONTH:
        dateTime = dateTime.with(TemporalAdjusters.firstDayOfMonth());
        break;
      case QUARTER:
        int month = dateTime.getMonth().getValue();
        int startMonth = (month - 1) / 3 * 3  + 1;
        dateTime = dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(ChronoField.MONTH_OF_YEAR, startMonth);
        break;
      case YEAR:
        dateTime = dateTime.with(TemporalAdjusters.firstDayOfYear());
        break;
      default:
        break;
    }
    return dateTime;
  }

  /**
   * 获取时间的结束时间
   * @param date
   * @param type
   * @return
   */
  public static LocalDateTime getEndTime(LocalDate date, RangeType type) {
    if(date == null) {
      return null;
    }
    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.now());
    return getEndTime(dateTime, type);
  }

  /**
   * 获取时间的结束时间
   * @param time
   * @param type
   * @return
   */
  public static LocalDateTime getEndTime(LocalDateTime time, RangeType type) {
    if (time == null) {
      return null;
    }
    Validate.notNull(type, "时间区间类型不能为空");
    LocalDateTime dateTime = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59);
    switch (type) {
      case DAY:
        break;
      case WEEK:
        dateTime = dateTime.with(ChronoField.DAY_OF_WEEK, 7);
        break;
      case MONTH:
        dateTime = dateTime.with(TemporalAdjusters.lastDayOfMonth());
        break;
      case QUARTER:
        int month = dateTime.getMonth().getValue();
        int endMonth = ((month - 1) / 3 + 1) * 3;
        dateTime = dateTime.with(TemporalAdjusters.firstDayOfMonth())
            .with(ChronoField.MONTH_OF_YEAR, endMonth)
            .with(TemporalAdjusters.lastDayOfMonth());
        break;
      case YEAR:
        dateTime = dateTime.with(TemporalAdjusters.lastDayOfYear());
        break;
      default:
        break;
    }
    return dateTime;
  }
}
