package ir.iranian.hardcore.calendar;

/**
 * Immutable Representation of a Persian Solar Hijri (Jalali) Date & Time.
 */
public class PersianDate {

    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;
    private final String monthName;
    private final String festival;
    private final boolean leapYear;

    public PersianDate(int year, int month, int day, int hour, int minute, int second,
                       String monthName, String festival, boolean leapYear) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.monthName = monthName;
        this.festival = festival;
        this.leapYear = leapYear;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getSecond() { return second; }
    public String getMonthName() { return monthName; }
    public String getFestival() { return festival; }
    public boolean hasFestival() { return festival != null && !festival.isEmpty(); }
    public boolean isLeapYear() { return leapYear; }

    /**
     * Standard Persian format: YYYY/MM/DD
     */
    public String toNumericString() {
        return String.format("%04d/%02d/%02d", year, month, day);
    }

    /**
     * Formal Persian date string (Finglish): e.g. "1 Farvardin 1405"
     */
    public String toFormalString() {
        return day + " " + monthName + " " + year;
    }

    /**
     * Time string: HH:mm:ss
     */
    public String toTimeString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public String toString() {
        String base = toFormalString() + " - " + toTimeString();
        if (hasFestival()) {
            base += " [" + festival + "]";
        }
        return base;
    }
}
