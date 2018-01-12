package eamv.dmu17he.lancrewapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;


import java.time.LocalDateTime;

/**
 * Created by alexj on 1/11/2018.
 */
@android.arch.persistence.room.Entity
public class SleepingSpaceEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "pokeCounter")
    private int pokeCounter;
    @ColumnInfo(name = "time")
    private LocalDateTime time;
    @ColumnInfo(name = "comment")
    private String comment;
    @ColumnInfo(name = "hallName")
    private String hallName;
    @ColumnInfo(name = "column")
    private int column;
    @ColumnInfo(name = "row")
    private int row;
    @ColumnInfo(name = "userName")
    private String userName;

    public SleepingSpaceEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        this.setHallName(hallName);
        this.setColumn(column);
        this.setRow(row);
        this.setUserName(userName);
        this.setTime(time);
        this.setComment(comment);
        this.setPokeCounter(pokeCounter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPokeCounter() {
        return pokeCounter;
    }

    public void setPokeCounter(int pokeCounter) {
        this.pokeCounter = pokeCounter;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
