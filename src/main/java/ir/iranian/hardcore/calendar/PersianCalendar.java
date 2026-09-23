package ir.iranian.hardcore.calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Astronomical & Mathematical Persian Solar Hijri (Jalali) Calendar.
 * Supports exact Gregorian <-> Persian Solar conversion, leap year computation,
 * and historical Iranian festival detection.
 */
public class PersianCalendar {

    public static final String[] MONTH_NAMES = {
        "Farvardin", "Ordibehesht", "Khordad",
        "Tir", "Mordad", "Shahrivar",
        "Mehr", "Aban", "Azar",
        "Dey", "Bahman", "Esfand"
    };

    /**
     * Determines if a Persian Solar year is a leap year (366 days).
     * Calculates the exact number of days between 1 Farvardin of year and year+1.
     */
    public static boolean isLeapYear(int jalaliYear) {
        int[] g1 = toGregorianDate(jalaliYear, 1, 1);
        int[] g2 = toGregorianDate(jalaliYear + 1, 1, 1);
        long days1 = countGregorianDays(g1[0], g1[1], g1[2]);
        long days2 = countGregorianDays(g2[0], g2[1], g2[2]);
        return (days2 - days1) == 366;
    }

    private static long countGregorianDays(int y, int m, int d) {
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int y2 = y - 1;
        long total = y2 * 365L + (y2 / 4) - (y2 / 100) + (y2 / 400);
        boolean isLeap = (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
        if (isLeap) daysInMonth[2] = 29;
        for (int i = 1; i < m; i++) {
            total += daysInMonth[i];
        }
        total += d;
        return total;
    }

    /**
     * Returns the length of a Persian month (1-12) for a given year.
     */
    public static int getMonthLength(int jalaliYear, int jalaliMonth) {
        if (jalaliMonth >= 1 && jalaliMonth <= 6) {
            return 31;
        } else if (jalaliMonth >= 7 && jalaliMonth <= 11) {
            return 30;
        } else if (jalaliMonth == 12) {
            return isLeapYear(jalaliYear) ? 30 : 29;
        }
        throw new IllegalArgumentException("Invalid Jalali month: " + jalaliMonth);
    }

    /**
     * Converts Gregorian date to Persian Solar Hijri date.
     */
    public static PersianDate toPersianDate(int gYear, int gMonth, int gDay, int hour, int minute, int second) {
        int[] gDaysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isGLeap = (gYear % 4 == 0 && gYear % 100 != 0) || (gYear % 400 == 0);
        if (isGLeap) {
            gDaysInMonth[2] = 29;
        }

        int gy2 = gYear - 1600;
        int days = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400;
        for (int i = 1; i < gMonth; i++) {
            days += gDaysInMonth[i];
        }
        days += gDay - 1;

        days -= 79;
        int jNp = days / 12053;
        days %= 12053;
        int jy = 979 + 33 * jNp + 4 * (days / 1461);
        days %= 1461;

        if (days >= 366) {
            jy += (days - 1) / 365;
            days = (days - 1) % 365;
        }

        int jm;
        int jd;
        if (days < 186) {
            jm = 1 + days / 31;
            jd = 1 + days % 31;
        } else {
            days -= 186;
            jm = 7 + days / 30;
            jd = 1 + days % 30;
        }

        String monthName = MONTH_NAMES[jm - 1];
        String festival = getFestivalName(jm, jd);
        boolean leap = isLeapYear(jy);

        return new PersianDate(jy, jm, jd, hour, minute, second, monthName, festival, leap);
    }

    /**
     * Converts a Persian Solar Hijri date back to Gregorian (Year, Month, Day).
     */
    public static int[] toGregorianDate(int jy, int jm, int jd) {
        int jy2 = jy - 979;
        int jm2 = jm - 1;
        int jd2 = jd - 1;

        int jDayNo = 365 * jy2 + (jy2 / 33) * 8 + ((jy2 % 33 + 3) / 4);
        for (int i = 0; i < jm2; i++) {
            jDayNo += (i < 6) ? 31 : 30;
        }
        jDayNo += jd2;

        int gDayNo = jDayNo + 79;
        int gy = 1600 + 400 * (gDayNo / 146097);
        gDayNo %= 146097;

        boolean leap = true;
        if (gDayNo >= 36525) {
            gDayNo--;
            gy += 100 * (gDayNo / 36524);
            gDayNo %= 36524;
            if (gDayNo >= 365) {
                gDayNo++;
            } else {
                leap = false;
            }
        }

        gy += 4 * (gDayNo / 1461);
        gDayNo %= 1461;

        if (gDayNo >= 366) {
            leap = false;
            gDayNo--;
            gy += gDayNo / 365;
            gDayNo %= 365;
        }

        int[] gDaysInMonth = {0, 31, leap ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int gm = 1;
        while (gDayNo >= gDaysInMonth[gm]) {
            gDayNo -= gDaysInMonth[gm];
            gm++;
        }
        int gd = gDayNo + 1;

        return new int[]{gy, gm, gd};
    }

    /**
     * Returns current Persian Date based on system clock (Asia/Tehran timezone).
     */
    public static PersianDate now() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Asia/Tehran"));
        return toPersianDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND)
        );
    }

    /**
     * Returns the authentic Iranian festival for a given Persian month and day.
     */
    public static String getFestivalName(int month, int day) {
        if (month == 1 && day == 1) return "Nowruz (Sal-e No-ye Bastani-ye Irani)";
        if (month == 1 && day == 6) return "Zadrooz-e Zartosht";
        if (month == 1 && day == 13) return "Sizdah-Bedar (Rooz-e Tabiat)";
        if (month == 4 && day == 10) return "Jashn-e Tirgan (Tir-e Arash-e Kamangir)";
        if (month == 7 && day == 16) return "Jashn-e Mehregan (Pirouzi-ye Kaveh bar Zahhak)";
        if (month == 8 && day == 10) return "Jashn-e Abangan (Setayesh-e Aab-haye Rovan)";
        if (month == 9 && day == 30) return "Shab-e Yalda (Jashn-e Chelleh va Zayesh-e Khorshid)";
        if (month == 11 && day == 10) return "Jashn-e Sadeh (Peydayesh-e Atash)";
        if (month == 12 && day == 5) return "Sepandarmazgan (Rooz-e Zamin va Zan dar Iran-e Bastan)";
        return "";
    }
}
