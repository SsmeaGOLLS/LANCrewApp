package eamv.dmu17he.lancrewapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;


import java.time.LocalDateTime;

/**
 * Created by alexj on 1/11/2018.
 */
@android.arch.persistence.room.Entity
public class Entity {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "pokeCounter")
    int pokeCounter;
    @ColumnInfo(name = "time")
    LocalDateTime time;
    @ColumnInfo(name = "comment")
    String comment;
    @ColumnInfo(name = "hallName")
    String hallName;
    @ColumnInfo(name = "column")
    int column;
    @ColumnInfo(name = "row")
    int row;
    @ColumnInfo(name = "userName")
    String userName;
    public Entity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        this.hallName = hallName;
        this.column = column;
        this.row = row;
        this.userName = userName;
        this.time = time;
        this.comment = comment;
        this.pokeCounter = pokeCounter;
    }
}
