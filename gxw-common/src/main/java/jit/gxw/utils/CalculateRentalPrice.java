package jit.gxw.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@Component
public class CalculateRentalPrice {



    public BigDecimal calculateRentalPrice(LocalDateTime startDate, LocalDateTime endDate, BigDecimal dailyRate, BigDecimal monthlyRate) {
        long start = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end=endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long days = (long) Math.ceil((double) (end - start) /(1000 * 60 * 60 * 24));
        return monthlyRate.multiply(BigDecimal.valueOf(days / 31)).add(dailyRate.multiply(BigDecimal.valueOf((days % 31))));
    }
}
