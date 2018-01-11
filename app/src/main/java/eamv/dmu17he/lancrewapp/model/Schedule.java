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
    private int event_ID;

    @ColumnInfo(name = "startTime")
    private LocalDateTime startTime;

    @ColumnInfo (name = "endTime")
    private LocalDateTime endTime;

    @ColumnInfo (name = "date")
    private LocalDateTime date;

    @ColumnInfo (name = "title")
    private String title;

    @ColumnInfo (name = "name")
    private String name;

    public int getId(){
        return event_ID;
    }

    public void setId(int id){
        this.event_ID = id;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){this.title = title;}

    public String setName() { return name;}

    public void setName (String name) { this.name = name; }

}
