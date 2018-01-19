package eamv.dmu17he.lancrewapp.model;

import java.util.Calendar;

import eamv.dmu17he.lancrewapp.activities.BookingsListOverviewActivity;

/**
 * Created by smeag on 16-01-2018.
 */

public class BookingListViewItem {
    private String time;
    private int row;
    private int column;
    private String hallName;
    private String nickName;
    private String comment;
    private int poke;
    private String wakeUpID;
    private BookingsListOverviewActivity activity;

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public int getRow() {return row;}
    public void setRow(int row) {this.row = row;}

    public int getColumn() {return column;}
    public void setColumn(int column) {this.column = column;}

    public String getHallName() {return hallName;}
    public void setHallName(String hallName) {this.hallName = hallName;}

    public String getNickName() {return nickName;}
    public void setNickName(String nickName) {this.nickName = nickName;}

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public int getPoke() {return poke;}
    public void setPoke(int poke) {this.poke = poke;}

    public String getWakeUpID() {
        return wakeUpID;
    }

    public void setWakeUpID(String wakeUpID) {
        this.wakeUpID = wakeUpID;
    }

    public BookingsListOverviewActivity getActivity() {
        return activity;
    }

    public void setActivity(BookingsListOverviewActivity activity) {
        this.activity = activity;
    }
}
