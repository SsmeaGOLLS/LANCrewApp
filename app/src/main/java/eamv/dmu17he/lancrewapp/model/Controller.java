package eamv.dmu17he.lancrewapp.model;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alexj on 1/11/2018.
 */

public class Controller {
    ArrayList<Hall> halls = new ArrayList<Hall>();
    EntityTranslator et = EntityTranslator.getInstance();

    // set comment , row, column, hallname,

    public void makeHall(String hallName, int numberOfColumns, int numberOfRows){
        halls.add(new Hall(hallName, numberOfColumns, numberOfRows));
        String log="hallName: "+halls.get(0).getHallName()+" rows: "+halls.get(0).getNumberOfRows()+" collums"+halls.get(0).getNumberOfColumns();
        Log.d("waw", log);

    }

    public void makeWakeUp(String hallName, String userName, Calendar time, String comment){
        getHallByName(hallName).getSpaceByUserName(userName).setWakeUp(new WakeUp(time, comment));
        et.addWakeUp(userName, time, comment);
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

    public String getHallName(int hallIndex) {
        return halls.get(hallIndex).getHallName();
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
