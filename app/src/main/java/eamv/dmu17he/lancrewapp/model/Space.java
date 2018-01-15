package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by alexj on 1/11/2018.
 */

public class Space {
    private int id;
    private WakeUp wakeUp;
    private String userName;
    private int column;
    private int row;

    public Space(int column, int row){
        this.setColumn(column);
        this.setRow(row);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WakeUp getWakeUp() {
        return wakeUp;
    }

    public void setWakeUp(WakeUp wakeUp) {
        this.wakeUp = wakeUp;
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

    public Calendar getTime(){
        return this.wakeUp.getTime();
    }
}
