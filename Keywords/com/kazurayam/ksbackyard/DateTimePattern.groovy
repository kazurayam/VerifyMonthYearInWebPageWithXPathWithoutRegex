package com.kazurayam.ksbackyard

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset

public class DateTimePattern {

	public DateTimePattern() {}

	public static String today() {
		// Todays date
		LocalDate today = LocalDate.now(ZoneOffset.UTC)
		// Todays Day as 2 gigits. e.g, "16"
		String dayToday = today.format(DateTimeFormatter.ofPattern("dd", Locale.JAPAN))
		// Todays Month as 1-2 digits. E.g, '5' as May
		String monthToday = today.format(DateTimeFormatter.ofPattern("M", Locale.JAPAN))
		// Todays Year as 4 digits. E.g, '2020'
		String yearToday = today.format(DateTimeFormatter.ofPattern("yyyy", Locale.JAPAN))
		// construct the pattern to check elements with
		String patternToday = "${yearToday}/${monthToday}/${dayToday}"
		return patternToday
	}

	public static String todayPlusDays(int days) {
		// Todays date
		LocalDate today = LocalDate.now()
		// Day of Today+30days
		String dayFuture = today.plusDays(days).format(DateTimeFormatter.ofPattern("dd", Locale.JAPAN))
		// Month of Today+30days
		String monthFuture = today.plusDays(days).format(DateTimeFormatter.ofPattern("M", Locale.JAPAN))
		// Year of Today+30days
		String yearFuture = today.plusDays(days).format(DateTimeFormatter.ofPattern("yyyy", Locale.JAPAN))
		//
		String patternFuture = "${yearFuture}/${monthFuture}/${dayFuture}"
		return patternFuture
	}
}
