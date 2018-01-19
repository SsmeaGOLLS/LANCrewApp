package eamv.dmu17he.lancrewapp.helper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jonas on 17-01-2018.
 */

public class MessageTime {

    private static String getTime(Calendar cal)
    {
        String time="";

        int hour = cal.get(Calendar.HOUR_OF_DAY)+1;
        int minutes = cal.get(Calendar.MINUTE);
        time+= ((hour<10) ? "0" : "")+hour+":";
        time+= ((minutes<10) ? "0" : "")+minutes;

        return time;
    }

    private static String getDate(Calendar cal)
    {
        String date="";

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        date+= ((day<10) ? "0" : "")+day+"/";
        date+= ((month<10) ? "0" : "")+month;

        return date;
    }

    private static String getYear(Calendar cal)
    {
        int year = Calendar.getInstance().get(Calendar.YEAR)-cal.get(Calendar.YEAR);
        return year+" year"+ ((year>1) ? "s" : "" ) +" ago";
     }

    public static String display(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(Calendar.getInstance().getTimeInMillis() - cal.getTimeInMillis() > 31556952000L)
            return getYear(cal);

        if(Calendar.getInstance().getTimeInMillis() - cal.getTimeInMillis() > 86400000 )
            return getDate(cal);

        return getTime(cal);
    }
}
