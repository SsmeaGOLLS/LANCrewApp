package eamv.dmu17he.lancrewapp.model;

/**
 * Created by jonas on 11-01-2018.
 */
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class Schedule {

    @com.google.gson.annotations.SerializedName("id")
    private String mId;
    public String getId() { return mId; }
    public final void setId(String id) { mId = id; }

    @PrimaryKey(autoGenerate = true)
    private int idS;

    @ColumnInfo(name = "startTime")
    private String startTime;

    @ColumnInfo (name = "endTime")
    private String endTime;

    @ColumnInfo (name = "date")
    private String date;

    @ColumnInfo (name = "title")
    private String title;

    @ColumnInfo (name = "nickName")
    private String nickName;

    @ColumnInfo (name = "gaName")
    private String gaName;

    public int getIdS(){
        return idS;
    }

    public void setIdS(int id){this.idS = id;}

    public String getStartTime(){
        return startTime;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){this.title = title;}

    public String getNickName() { return nickName;}

    public void setNickName (String nickName) { this.nickName = nickName; }

    public String getGaName() { return gaName;}

    public void setGaName (String gaName) { this.gaName = gaName; }

    public String getInfo(){
        return "Date: " + date + "\nStart Time: " + startTime + "\nEnd Time: " + endTime + "\nNick: " + nickName + "\nGA: " + gaName + "\n";
    }

    @Override
    public String toString(){
        return title;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Schedule && ((Schedule) o).mId == mId;
    }
}



