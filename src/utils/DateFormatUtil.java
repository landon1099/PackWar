package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    private final static DateFormat yyyy = new SimpleDateFormat("yyyy");
    private final static DateFormat MM = new SimpleDateFormat("MM");

    public static String getYYYY() {
        Date date = new Date(System.currentTimeMillis());
        return yyyy.format(date);
    }

    public static String getMM() {
        Date date = new Date(System.currentTimeMillis());
        return MM.format(date);
    }

    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }

}
