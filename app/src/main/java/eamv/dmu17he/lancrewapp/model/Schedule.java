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

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "startTime")
    private String startTime;

    @ColumnInfo (name = "endTime")
    private String endTime;

    @ColumnInfo (name = "date")
    private String date;

    @ColumnInfo (name = "title")
    private String title;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "gaName")
    private String gaName;

    public int getId(){
        return id;
    }

    public void setId(int id){this.id = id;}

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

    public String getName() { return name;}

    public void setName (String name) { this.name = name; }

    public String getGaName() { return gaName;}

    public void setGaName (String gaName) { this.gaName = gaName; }

    @Override
    public String toString(){
        return title + "\n" + date + "\n" + startTime + "\n" + endTime + "\n" + name + "\n GA: " + gaName;
    }

}
