package eamv.dmu17he.lancrewapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Created by alexj on 1/11/2018.
 */
@Entity
public class Hall {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    ArrayList<Space> spaces = new ArrayList<Space>();
     int numberOfColumns;
     int numberOfRows;
     public Hall(String name, int numberOfColumns, int numberOfRows){
         this.name = name;
         this.numberOfColumns = numberOfColumns;
         this.numberOfRows = numberOfRows;

         for(int row = 0; row < this.numberOfRows; row++){
             for(int column = 0; column<numberOfColumns; column++){

                 spaces.add(new Space(column, row));
             }
         }
     }

}
