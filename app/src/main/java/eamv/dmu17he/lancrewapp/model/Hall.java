package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by alexj on 1/11/2018.
 */
public class Hall {
    private int id;
    private String hallName;
    private ArrayList<Space> spaces = new ArrayList<Space>();
    private int numberOfColumns;
    private int numberOfRows;

    public Hall(String hallName, int numberOfColumns, int numberOfRows){
        this.hallName = hallName;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;

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

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
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

    public Space getSpaceByUserName(String userName){
        for(Space s : spaces){
            if (s.getUserName() == userName)
                return s;
        }
        return null;
    }

    public void editNumberOfSpaces(int numberOfColumns, int numberOfRows, int newNumberOfColumns, int newNumberOfRows) {

        for(Space space : spaces){
            if(space.getRow() > newNumberOfRows || space.getColumn() > newNumberOfColumns){
                spaces.remove(space);
            }
        }


        for (int column = newNumberOfColumns; column< newNumberOfColumns; column++){
            for (int row = newNumberOfRows; row<newNumberOfRows; row++){
                spaces.add(new Space(column, row));
            }
        }
    }
}
