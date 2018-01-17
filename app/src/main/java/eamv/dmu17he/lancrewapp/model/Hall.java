package eamv.dmu17he.lancrewapp.model;

public class Hall {
    @com.google.gson.annotations.SerializedName("id")
    private String id;

    private String hallName;
    private int numberOfColumns;
    private int numberOfRows;

    public Hall(String hallName, int numberOfColumns, int numberOfRows){
        this.hallName = hallName;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }
    public Hall(){

    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getHallName() {return hallName;}
    public void setHallName(String hallName) {this.hallName = hallName;}

    public int getNumberOfColumns() {return numberOfColumns;}
    public void setNumberOfColumns(int numberOfColumns) {this.numberOfColumns = numberOfColumns;}

    public int getNumberOfRows() {return numberOfRows;}

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    @Override
    public String toString() {
        return hallName;
    }

}
