package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;
import java.time.Duration;

import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;

public class Calc {

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {

        Duration duration = Duration.between(parseZonedDateTime(start), parseZonedDateTime(end));

        return calculateForDuration(duration, dailyRate);

    }

    ZonedDateTime parseZonedDateTime(String str) {

        String[] parts = str.split(" ");

        LocalDate localDate = LocalDate.parse(parts[0]);
        LocalTime localTime = LocalTime.parse(parts[1]);
        ZoneId zoneId = ZoneId.of(parts[2]);

        return ZonedDateTime.of(localDate, localTime, zoneId);

    }

    BigDecimal calculateForDuration(Duration duration, BigDecimal dailyRate) {

        if (duration.isNegative()) return (new BigDecimal(0)).setScale(2, RoundingMode.HALF_UP);

        long days = duration.toDays();
        Duration remainingDuration = duration.minusDays(days);
        long remainingHours = remainingDuration.toHours();

        BigDecimal dayPart = dailyRate.multiply(new BigDecimal(days));

        BigDecimal remainingPart;

        if (remainingDuration.isZero()) {
            remainingPart = new BigDecimal(0);
        }
        else if (remainingHours <= 8) {
            remainingPart = dailyRate.divide(new BigDecimal(3), RoundingMode.HALF_UP);
        }
        else if (remainingHours <= 12) {
            remainingPart = dailyRate.divide(new BigDecimal(2), RoundingMode.HALF_UP);
        }
        else {
            remainingPart = dailyRate;
        }

        return dayPart.add(remainingPart);

    }


}
