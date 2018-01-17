package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by alexj on 1/11/2018.
 */

public class Space {
    private String id;
    private String wakeUpID;
    private String userName;
    private int column;
    private int row;
    private String hallName;

    public Space(int column, int row){
        this.setColumn(column);
        this.setRow(row);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getWakeUpID() {
        return wakeUpID;
    }
    public void setWakeUpID(String wakeUpID) {
        this.wakeUpID = wakeUpID;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public String getHallName() {return hallName;}
    public void setHallName(String hallName) {this.hallName = hallName;}

    //public Calendar getTime(){return this.wakeUp.getTime();}
}
