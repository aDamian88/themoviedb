package gr.advantage.adam.themoviedb.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {

    public static Date getDateFromString(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getYear(String dateStr){

        Date helpDate = getDateFromString(dateStr);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy");
        return dateFormatter.format(helpDate);
    }
}
