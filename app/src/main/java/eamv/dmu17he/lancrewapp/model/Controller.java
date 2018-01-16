package eamv.dmu17he.lancrewapp.model;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by alexj on 1/11/2018.
 */

public class Controller {
    ArrayList<Hall> halls = new ArrayList<Hall>();

    // set comment , row, column, hallname,

    public void createHall(String hallName, int numberOfColumns, int numberOfRows){
        Hall hall = new Hall(hallName, numberOfColumns, numberOfRows);
        halls.add(hall);

        String log="hallName: "+halls.get(0).getHallName()+" rows: "+halls.get(0).getNumberOfRows()+" collums"+halls.get(0).getNumberOfColumns();
        Log.d("waw", log);
    }

    public void createWakeUp(String userName, Calendar time, String comment){
        for(Hall hall: halls){
            hall.getSpaceByUserName(userName).setWakeUp(new WakeUp(time, comment));
        }
    }

    public void deleteHall(String hallname){
        for(int x = 0 ; x < halls.size() ; x++){
            Log.d("hs", hallname);
            Log.d("h", halls.get(x).getHallName());
            if (halls.get(x).getHallName().equals(hallname)){
                Log.d("der", "use the force");
                halls.remove(x);
                for(Hall hall : halls)
                    Log.d("der", hall.getHallName());
                return;
            }
        }
    }

    public void editHall(String oldHallName, String newHallName, int newNumberOfColumns, int newNumberOfRows){
        Hall hall = getHallByName(oldHallName);
        int numberOfColumns = hall.getNumberOfColumns();
        int numberOfRows = hall.getNumberOfRows();
        hall.setHallName(newHallName);
        hall.setNumberOfColumns(newNumberOfColumns);
        hall.setNumberOfRows(newNumberOfRows);
        hall.editNumberOfSpaces(numberOfColumns, numberOfRows, newNumberOfColumns, newNumberOfRows);
    }



    //hej mathias

    



    public int getNumberOfHalls(){
        return halls.size();
    }

    public int getNumberOfSpacesInHall(int hallIndex){
        return halls.get(hallIndex).getSizeOfSpaces();
    }

    public ArrayList<Integer> getColumnRowNumber(String hallName, String userName){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        Space tempSpace = getHallByName(hallName).getSpaceByUserName(userName);
        temp.add(tempSpace.getColumn());
        temp.add(tempSpace.getRow());
        return temp;
    }

    public ArrayList<Integer> getRowsAndColumnsByHallName(String hallName) {
        ArrayList<Integer> listToBeReturned= new ArrayList<Integer>();
        for(Hall hall: halls){
            if (hall.getHallName().equals(hallName)) {
                int numberOfRows = hall.getNumberOfRows();
                listToBeReturned.add(numberOfRows);
                int numberOfColumns = hall.getNumberOfColumns();
                listToBeReturned.add(numberOfColumns);
            }
        }
        return listToBeReturned;
    }

    public String getHallName(int hallIndex) {
        return halls.get(hallIndex).getHallName();
    }

    public ArrayList<String> getHallNames() {
        ArrayList<String> hallNames = new ArrayList<String>();
        for(Hall hall : halls)
            hallNames.add(hall.getHallName());
        return hallNames;
    }



    public Calendar getTimeByHallNameUserName(String hallName, String userName){
        return getHallByName(hallName).getSpaceByUserName(userName).getTime();
    }

    private Hall getHallByName(String hallName){
        for(Hall h: halls){
            if(h.getHallName() == hallName){
                return h;
            }
        }
        return null;
    }

    // hallName, time, row, column

    private void addEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        SleepingSpaceEntity h = new SleepingSpaceEntity(hallName, column, row, userName, time, comment, pokeCounter);
    }

    private void getEntity(String hallName, int column, int row, String userName, LocalDateTime time, String comment, int pokeCounter){
        SleepingSpaceEntity h = new SleepingSpaceEntity(hallName, column, row, userName, time, comment, pokeCounter);
    }






























    private static final Controller ourInstance = new Controller();

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
    }
}
