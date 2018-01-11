package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by alexj on 1/11/2018.
 */
public class Hall {
    private int id;
    private String name;
    private ArrayList<Space> spaces = new ArrayList<Space>();
    private int numberOfColumns;
    private int numberOfRows;

    public Hall(String name, int numberOfColumns, int numberOfRows){
        this.setName(name);
        this.setNumberOfColumns(numberOfColumns);
        this.setNumberOfRows(numberOfRows);

        for(int row = 0; row < this.getNumberOfRows(); row++){
            for(int column = 0; column<numberOfColumns; column++){

                getSpaces().add(new Space(column, row));
            }
        }
    }

    public int getSizeOfSpaces(){
        return spaces.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<Space> spaces) {
        this.spaces = spaces;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public LocalDateTime getSpaceByColumnRow(int column, int row){
        for(Space s : spaces){
            if (s.getRow() == row && s.getColumn() == column)
                return s.getTime();
        }
        return null;
    }
}
